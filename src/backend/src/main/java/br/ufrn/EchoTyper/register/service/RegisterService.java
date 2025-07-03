package br.ufrn.EchoTyper.register.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import br.ufrn.EchoTyper.register.dto.RegisterMapper;
import br.ufrn.EchoTyper.register.dto.RegisterRequestDTO;
import br.ufrn.EchoTyper.register.dto.RegisterResponseDTO;
import br.ufrn.EchoTyper.register.model.Register;
import br.ufrn.EchoTyper.register.repository.RegisterRepository;
import br.ufrn.EchoTyper.registerGroup.dto.RegisterGroupMapper;
import br.ufrn.EchoTyper.registerGroup.dto.RegisterGroupRequestDTO;
import br.ufrn.EchoTyper.registerGroup.dto.RegisterGroupResponseDTO;
import br.ufrn.EchoTyper.registerGroup.model.RegisterGroup;
import br.ufrn.EchoTyper.registerGroup.repository.RegisterGroupRepository;
import jakarta.transaction.Transactional;

public abstract class RegisterService {
    private RegisterRepository registerRepository;
    private RegisterGroupRepository registerGroupRepository;
    private RegisterMapper registerMapper;
    private RegisterGroupMapper registerGroupMapper;

    public RegisterService(RegisterRepository registerRepository,
            RegisterGroupRepository registerGroupRepository,
            RegisterMapper registerMapper,
            RegisterGroupMapper registerGroupMapper) {
        this.registerRepository = registerRepository;
        this.registerGroupRepository = registerGroupRepository;
        this.registerMapper = registerMapper;
        this.registerGroupMapper = registerGroupMapper;
    }

    public RegisterResponseDTO createRegister(RegisterRequestDTO registerRequestDTO) {
        Register newRegister;
        if (registerRequestDTO.groupId() == null) {
            newRegister = registerMapper.toEntity(registerRequestDTO);
        } else {
            RegisterGroup group = registerGroupRepository.findById(registerRequestDTO.groupId()).orElseGet(() -> null);
            newRegister = registerMapper.toEntity(registerRequestDTO, group);
        }
        registerRepository.save(newRegister);
        return registerMapper.toResponseDTO(newRegister);
    }

    public RegisterResponseDTO updateRegister(Long id, RegisterRequestDTO registerRequestDTO) {
        Register register = registerRepository.findById(id).get();
        if (register == null) {
            throw new RuntimeException("Register not found");
        }
        register.setTitle(registerRequestDTO.title());
        register.setTranscription(registerRequestDTO.transcription());
        register.setSummary(registerRequestDTO.summary());
        register.setAnnotations(registerRequestDTO.annotations());
        register.setSubclassesAttributes(registerRequestDTO.content());
        Optional<RegisterGroup> groupOpt = getGroupObjById(id);
        RegisterGroup group = groupOpt.orElseThrow(() -> new IllegalArgumentException("Group does not exist"));
        register.setGroup(group);
        registerRepository.save(register);
        return registerMapper.toResponseDTO(register);
    }

    // Meant to be used only on the resource's service layer
    public Register updateRegister(Register newRegister) {
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
        Set<Register> registers = new HashSet<>();
        for (Long registerId : registerDTO.registerIds()) {
            registers.add(getRegisterObjById(registerId));
        }
        
        RegisterGroup registerGroup = registerGroupMapper.toEntity(registerDTO, registers);
        registerGroupRepository.save(registerGroup);
        for (Long registerIds : registerDTO.registerIds()) {
            addRegisterToGroup(registerIds, registerGroup.getId());
        }
        return registerGroupMapper.toResponseDTO(registerGroup);
    }

    // Hook that'll perform custom logic when adding a register subclass to a group
    protected abstract void addRegisterToGroupHook(RegisterGroup group, Register register);

    @Transactional
    public RegisterGroupResponseDTO addRegisterToGroup(Long registerId, Long registerGroupId) {
        // TODO : exception handling
        RegisterGroup registerGroup = getGroupObjById(registerGroupId)
                .orElseThrow(() -> new IllegalArgumentException("Group does not exist"));
        Register register = registerRepository.findById(registerId)
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
    protected abstract void removeRegisterFromGroupHook(RegisterGroup group, Register register);

    @Transactional
    public RegisterGroupResponseDTO removeRegisterFromGroup(Long registerId, Long registerGroupId) {
        RegisterGroup group = registerGroupRepository.findById(registerGroupId).get();
        Register register = getRegisterObjById(registerGroupId);
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
        RegisterGroup group = registerGroupRepository.findById(groupId).get();
        for (Register register : group.getRegisters()) {
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
        Collection<Register> registers = getGroupsRegistersObjs(groupId);
        List<String> summaries = new ArrayList<>();
        for (Register register : registers) {
            summaries.add(register.getSummary());
        }
        return summaries;
    }

    @Transactional
    public List<JsonNode> getRegisterContentByGroup(Long groupId) {
        Collection<Register> registers = getGroupsRegistersObjs(groupId);
        List<JsonNode> contentList = new ArrayList<>();
        for (Register register : registers) {
            contentList.add(register.getContent());
        }
        return contentList;
    }

    @Transactional
    public List<RegisterResponseDTO> getRegistersByGroup(Long groupId) {
        Collection<Register> registers = getGroupsRegistersObjs(groupId);
        List<RegisterResponseDTO> registerResponses = new ArrayList<>();
        for (Register register : registers) {
            registerResponses.add(registerMapper.toResponseDTO(register));
        }
        return registerResponses;
    }

    protected Collection<Register> getGroupsRegistersObjs(Long groupId) {
        Optional<RegisterGroup> groupOpt = getGroupObjById(groupId);
        if (groupOpt.isEmpty()) {
            return new ArrayList<>();
        }
        // ! : Protecting the reference might cause bugs
        return new ArrayList<Register>(groupOpt.get().getRegisters());
    }

    protected Optional<RegisterGroup> getGroupObjById(Long id) {
        return registerGroupRepository.findById(id);
    }

    protected Register getRegisterObjById(Long id) {
        return registerRepository.findById(id).orElseGet(() -> null);
    }
}
