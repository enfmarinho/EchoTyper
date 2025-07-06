package br.ufrn.EchoTyper.register.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.register.dto.RegisterGroupMapper;
import br.ufrn.EchoTyper.register.dto.RegisterGroupRequestDTO;
import br.ufrn.EchoTyper.register.dto.RegisterGroupResponseDTO;
import br.ufrn.EchoTyper.register.dto.RegisterMapper;
import br.ufrn.EchoTyper.register.dto.RegisterRequestDTO;
import br.ufrn.EchoTyper.register.dto.RegisterResponseDTO;
import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.model.RegisterGroup;
import br.ufrn.EchoTyper.register.repository.RegisterGroupRepository;
import br.ufrn.EchoTyper.register.repository.RegisterRepository;
import jakarta.transaction.Transactional;

public abstract class RegisterService<RegisterImpl extends Register, RegisterGroupImpl extends RegisterGroup> {
    private RegisterRepository<RegisterImpl> registerRepository;
    private RegisterGroupRepository<RegisterGroupImpl> registerGroupRepository;
    private RegisterMapper<RegisterImpl> registerMapper;
    private RegisterGroupMapper<RegisterGroupImpl, RegisterImpl> registerGroupMapper;

    public RegisterService(RegisterRepository<RegisterImpl> registerRepository,
            RegisterGroupRepository<RegisterGroupImpl> registerGroupRepository,
            RegisterMapper<RegisterImpl> registerMapper,
            RegisterGroupMapper<RegisterGroupImpl, RegisterImpl> registerGroupMapper) {
        this.registerRepository = registerRepository;
        this.registerGroupRepository = registerGroupRepository;
        this.registerMapper = registerMapper;
        this.registerGroupMapper = registerGroupMapper;
    }

    public RegisterResponseDTO createRegister(RegisterRequestDTO registerRequestDTO) {
        RegisterImpl newRegister;
        if (registerRequestDTO.groupId() == null) {
            newRegister = registerMapper.toEntity(registerRequestDTO);
        } else {
            RegisterGroup group = registerGroupRepository.findById(registerRequestDTO.groupId()).orElseGet(() -> null);
            newRegister = registerMapper.toEntity(registerRequestDTO, group);
        }
        registerRepository.save(newRegister);
        return registerMapper.toResponseDTO(newRegister);
    }

    @Transactional
    public RegisterResponseDTO updateRegister(Long id, RegisterRequestDTO registerRequestDTO) {
        RegisterImpl register = registerRepository.findById(id).get();
        if (register == null) {
            throw new RuntimeException("Register not found");
        }
        register.setTitle(registerRequestDTO.title());
        register.setTranscription(registerRequestDTO.transcription());
        register.setSummary(registerRequestDTO.summary());
        register.setAnnotations(registerRequestDTO.annotations());
        register.setContent(registerRequestDTO.content());
        Optional<RegisterGroupImpl> groupOpt = getGroupObjById(id);
        RegisterGroup group = groupOpt.orElseThrow(() -> new IllegalArgumentException("Group does not exist"));
        register.setGroup(group);
        return registerMapper.toResponseDTO(register);
    }

    // ! : This doesnt guarantee ACID, since it is self-invocational. See the 'potential pitfalls' section:
    // https://www.baeldung.com/transaction-configuration-with-jpa-and-spring
    public RegisterImpl updateRegister(RegisterImpl newRegister) {
        registerRepository.save(newRegister);
        return newRegister;
    }

    public RegisterResponseDTO getRegisterById(Long id) {
        return registerRepository.findById(id).map((register) -> registerMapper.toResponseDTO(register)).orElse(null);
    }

    public List<RegisterResponseDTO> getAllRegisters() {
        return registerRepository.findAll().parallelStream().map((register) -> registerMapper.toResponseDTO(register))
                .toList();
    }

    // Writing a JPQL query is better than handlng these entity objects, but
    // performing queries envolving LOBs is really annoying
    @Transactional
    public void deleteRegister(Long id) {
        if (!registerRepository.existsById(id)) {
            throw new RuntimeException("Register not found");
        }
        Register register = registerRepository.findById(id).get();
        if (register.getGroup() != null) {
            removeRegisterFromGroup(id, register.getGroup().getId());
        }
        registerRepository.deleteById(id);
    }

    @Transactional
    public List<RegisterResponseDTO> getGrouplessRegisters() {
        return registerRepository.findAll().parallelStream().filter((register) -> register.getGroup() == null)
                .map((register) -> registerMapper.toResponseDTO(register)).toList();
    }

    public List<RegisterGroupResponseDTO> getAllGroups() {
        return registerGroupRepository.findAll().stream()
                .map((registerGroup) -> registerGroupMapper.toResponseDTO(registerGroup)).toList();
    }

    @Transactional
    public RegisterGroupResponseDTO createGroup(RegisterGroupRequestDTO registerDTO) {
        Set<RegisterImpl> registers = new HashSet<>();
        for (Long registerId : registerDTO.registerIds()) {
            registers.add(getRegisterObjById(registerId));
        }
        RegisterGroupImpl registerGroup = registerGroupMapper.toEntity(registerDTO, registers);
        registerGroupRepository.save(registerGroup);
        for (Long registerIds : registerDTO.registerIds()) {
            addRegisterToGroup(registerIds, registerGroup.getId());
        }
        return registerGroupMapper.toResponseDTO(registerGroup);
    }

    // Hook that'll perform custom logic when adding a register subclass to a group
    protected abstract void addRegisterToGroupHook(RegisterGroupImpl group, RegisterImpl register);

    @Transactional
    public RegisterGroupResponseDTO addRegisterToGroup(Long registerId, Long registerGroupId) {
        // TODO : exception handling
        RegisterGroupImpl registerGroup = getGroupObjById(registerGroupId)
                .orElseThrow(() -> new IllegalArgumentException("Group does not exist"));
        RegisterImpl register = registerRepository.findById(registerId)
                .orElseThrow(() -> new IllegalArgumentException("Register does not exist"));
        registerGroup.getRegisters().add(register);
        register.setGroup(registerGroup);
        updateRegister(register);
        addRegisterToGroupHook(registerGroup, register);
        registerGroupRepository.save(registerGroup);
        return registerGroupMapper.toResponseDTO(registerGroup);
    }

    // Hook that'll perform custom logic when removing a register subclass to a
    // group
    protected abstract void removeRegisterFromGroupHook(RegisterGroupImpl group, RegisterImpl register);

    @Transactional
    public RegisterGroupResponseDTO removeRegisterFromGroup(Long registerId, Long registerGroupId) {
        RegisterGroupImpl group = registerGroupRepository.findById(registerGroupId).get();
        RegisterImpl register = getRegisterObjById(registerGroupId);
        group.getRegisters().remove(register);
        register.setGroup(null);
        updateRegister(register);
        registerGroupRepository.save(group);
        // ** Update register group template method
        removeRegisterFromGroupHook(group, register);
        return registerGroupMapper.toResponseDTO(group);
    }

    // Hoook to override the logic performed to a register when its group is deleted
    protected abstract void deleteRegisterGroupHook(RegisterGroup group, Register register);

    @Transactional
    public void deleteRegisterGroup(Long groupId) {
        RegisterGroupImpl group = registerGroupRepository.findById(groupId).get();
        for (RegisterImpl register : group.getRegisters()) {
            removeRegisterFromGroup(register.getId(), group.getId());
            // ** Delete group template method
            deleteRegisterGroupHook(group, register);
        }
        registerGroupRepository.delete(group);
    }

    @Transactional
    public RegisterGroupResponseDTO getGroupById(Long id) {
        return registerGroupRepository.findById(id)
                .map((registerGroup) -> registerGroupMapper.toResponseDTO(registerGroup)).orElse(null);
    }

    @Transactional
    public List<String> getSummariesByGroup(Long groupId) {
        Collection<RegisterImpl> registers = getGroupsRegistersObjs(groupId);
        List<String> summaries = new ArrayList<>();
        for (Register register : registers) {
            summaries.add(register.getSummary());
        }
        return summaries;
    }

    @Transactional
    public List<JsonNode> getRegisterContentByGroup(Long groupId) {
        Collection<RegisterImpl> registers = getGroupsRegistersObjs(groupId);
        List<JsonNode> contentList = new ArrayList<>();
        for (Register register : registers) {
            contentList.add(register.getContent());
        }
        return contentList;
    }

    @Transactional
    public List<RegisterResponseDTO> getRegistersByGroup(Long groupId) {
        Collection<RegisterImpl> registers = getGroupsRegistersObjs(groupId);
        List<RegisterResponseDTO> registerResponses = new ArrayList<>();
        for (Register register : registers) {
            registerResponses.add(registerMapper.toResponseDTO(register));
        }
        return registerResponses;
    }

    protected Collection<RegisterImpl> getGroupsRegistersObjs(Long groupId) {
        Optional<RegisterGroupImpl> groupOpt = getGroupObjById(groupId);
        if (groupOpt.isEmpty()) {
            return new ArrayList<>();
        }
        // ! : Protecting the reference might cause bugs
        return new ArrayList<RegisterImpl>(groupOpt.get().getRegisters());
    }

    protected Optional<RegisterGroupImpl> getGroupObjById(Long id) {
        return registerGroupRepository.findById(id);
    }

    protected RegisterImpl getRegisterObjById(Long id) {
        return registerRepository.findById(id).orElseGet(() -> null);
    }
}
