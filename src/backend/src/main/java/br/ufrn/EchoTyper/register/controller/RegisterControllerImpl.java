package br.ufrn.EchoTyper.register.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.ufrn.EchoTyper.register.service.RegisterService;
import jakarta.validation.Valid;
import br.ufrn.EchoTyper.register.dto.RegisterResponseDTO;
import br.ufrn.EchoTyper.register.dto.RegisterGroupRequestDTO;
import br.ufrn.EchoTyper.register.dto.RegisterGroupResponseDTO;
import br.ufrn.EchoTyper.register.dto.RegisterRequestDTO;

@RestController
public class RegisterControllerImpl implements RegisterController {
    @Autowired
    RegisterService registerService;

    @Override
    public ResponseEntity<List<RegisterResponseDTO>> getAllRegisters() {
        return ResponseEntity.ok().body(registerService.getAllRegisters());
    }

    @Override
    public ResponseEntity<RegisterResponseDTO> getRegisterById(Long id) {
        return ResponseEntity.ok().body(registerService.getRegisterById(id));
    }

    @Override
    public ResponseEntity<RegisterResponseDTO> createRegister(RegisterRequestDTO createRegisterDTO) {
        return ResponseEntity.ok().body(registerService.createRegister(createRegisterDTO));
    }

    @Override
    public ResponseEntity<RegisterResponseDTO> updateRegister(Long id, RegisterRequestDTO updateRegisterDTO) {
        return ResponseEntity.ok().body(registerService.updateRegister(id, updateRegisterDTO));
    }

    @Override
    public ResponseEntity<RegisterResponseDTO> deleteRegister(Long id) {
        registerService.deleteRegister(id);
        return ResponseEntity.ok().build();
    }
    @Override
    public ResponseEntity<List<RegisterResponseDTO>> getGrouplessRegisters() {
        return ResponseEntity.ok().body(registerService.getGrouplessRegisters());
    }

    @Override
    public ResponseEntity<List<RegisterGroupResponseDTO>> getAllGroups() {
        return ResponseEntity.ok().body(registerService.getAllGroups());
    }

    @Override
    public ResponseEntity deleteGroup(Long groupId) {
        registerService.deleteRegisterGroup(groupId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<RegisterGroupResponseDTO> addRegister(Long registerId, Long groupId) {
        return ResponseEntity.ok().body(registerService.addRegisterToGroup(registerId, groupId));
    }

    @Override
    public ResponseEntity<RegisterGroupResponseDTO> removeRegister(Long registerId, Long groupId) {
        return ResponseEntity.ok().body(registerService.removeRegisterFromGroup(registerId, groupId));
    }

    @Override
    public ResponseEntity<RegisterGroupResponseDTO> createGroup(@Valid RegisterGroupRequestDTO createGroupDTO) {
        return ResponseEntity.ok().body(registerService.createGroup(createGroupDTO));
    }

    @Override
    public ResponseEntity<RegisterGroupResponseDTO> getGroupById(Long id) {
        return ResponseEntity.ok().body(registerService.getGroupById(id));
    }

    // @Override
    // public ResponseEntity<RegisterGroupResponseDTO> updateGroup(Long id,
    //         @Valid RegisterGroupRequestDTO registerDTO) {
    //     return ResponseEntity.ok().body(registerService.updateRegisterGroup(id, registerDTO));
    // }

    @Override
    public ResponseEntity<List<RegisterResponseDTO>> getRegistersByGroup(Long id) {
        return ResponseEntity.ok().body(registerService.getRegistersByGroup(id));
    }
}