package br.ufrn.EchoTyper.register.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.ufrn.EchoTyper.register.dto.RegisterGroupRequestDTO;
import br.ufrn.EchoTyper.register.dto.RegisterGroupResponseDTO;
import br.ufrn.EchoTyper.register.dto.RegisterRequestDTO;
import br.ufrn.EchoTyper.register.dto.RegisterResponseDTO;
import jakarta.validation.Valid;


@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/register")
public interface RegisterController {
        @GetMapping("")
        public ResponseEntity<List<RegisterResponseDTO>> getAllRegisters();

        @GetMapping("/groupless")
        public ResponseEntity<List<RegisterResponseDTO>> getGrouplessRegisters();

        @GetMapping("/{id}")
        public ResponseEntity<RegisterResponseDTO> getRegisterById(@PathVariable("id") Long id);

        @PostMapping("/create")
        public ResponseEntity<RegisterResponseDTO> createRegister(@Valid @RequestBody RegisterRequestDTO createRegisterDTO);

        @PutMapping("/update/{id}")
        public ResponseEntity<RegisterResponseDTO> updateRegister(@PathVariable("id") Long id,
                        @Valid @RequestBody RegisterRequestDTO updateRegisterDTO);

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<RegisterResponseDTO> deleteRegister(@PathVariable("id") Long id);

        @GetMapping("/groups")
        public ResponseEntity<List<RegisterGroupResponseDTO>> getAllGroups();

        // @PutMapping("/groups/update/{id}")
        // public ResponseEntity<RegisterGroupResponseDTO> updateGroup(@PathVariable("id") Long id,
        //                 @Valid @RequestBody RegisterGroupRequestDTO registerDTO);

        @GetMapping("/groups/{id}")
        public ResponseEntity<RegisterGroupResponseDTO> getGroupById(@PathVariable("id") Long id);

        @GetMapping("/groups/{id}/registers")
        public ResponseEntity<List<RegisterResponseDTO>> getRegistersByGroup(@PathVariable("id") Long id);

        @PostMapping("/groups/create")
        public ResponseEntity<RegisterGroupResponseDTO> createGroup(
                        @Valid @RequestBody RegisterGroupRequestDTO createGroupDTO);

        @PutMapping("/groups/add/{registerId}/{groupId}")
        public ResponseEntity<RegisterGroupResponseDTO> addRegister(@PathVariable("registerId") Long registerId,
                        @PathVariable("groupId") Long groupId);

        @PutMapping("/groups/remove/{registerId}/{groupId}")
        public ResponseEntity<RegisterGroupResponseDTO> removeRegister(@PathVariable("registerId") Long registerId,
                        @PathVariable("groupId") Long groupId);

        @DeleteMapping("/groups/delete/{groupId}")
        public ResponseEntity deleteGroup(@PathVariable("groupId") Long groupId);
}
