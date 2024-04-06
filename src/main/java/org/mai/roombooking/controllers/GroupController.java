package org.mai.roombooking.controllers;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.mai.roombooking.dtos.GroupDTO;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.repositories.GroupRepository;
import org.mai.roombooking.services.ValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final GroupRepository groupRepository;
    private final ValidationService validationService;
    private final ModelMapper modelMapper;

    @GetMapping("/all")
    public ResponseEntity<List<GroupDTO>> getAll() {
        return ResponseEntity.ok(groupRepository.findAll().stream().map(GroupDTO::new).toList());
    }

    @PostMapping("/update")
    public ResponseEntity<GroupDTO> update(@NonNull @RequestBody GroupDTO dto) {
        validationService.validate(dto);
        Group group = modelMapper.map(dto, Group.class);
        return ResponseEntity.ok(new GroupDTO(groupRepository.save(group)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@NonNull @PathVariable Long id) {
        try { groupRepository.deleteById(id); }
        catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>("Попытка удаления несуществующей брони",
                    HttpStatusCode.valueOf(400));
        }
        return ResponseEntity.ok("Удаление проло успешно");
    }
}
