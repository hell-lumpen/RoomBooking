package org.mai.roombooking.controllers;

import lombok.NonNull;
import org.mai.roombooking.dtos.GroupDTO;
import org.mai.roombooking.entities.Group;
import org.mai.roombooking.repositories.GroupRepository;
import org.mai.roombooking.services.ValidationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupController {
    private final GroupRepository groupRepository;
    private final ValidationService validationService;

    public GroupController(GroupRepository groupRepository, ValidationService validationService) {
        this.groupRepository = groupRepository;
        this.validationService = validationService;
    }

    @GetMapping("/get/all")
    public List<GroupDTO> getAll() {
        return groupRepository.findAll().stream().map(GroupDTO::new).toList();
    }

    @PostMapping("/update")
    public GroupDTO update(@NonNull @RequestBody GroupDTO dto) {
        validationService.validate(dto);
        return new GroupDTO(groupRepository.save(new Group(dto)));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@NonNull @PathVariable Long id) {
        groupRepository.deleteById(id);
    }
}
