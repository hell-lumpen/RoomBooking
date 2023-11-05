package org.mai.roombooking.controllers;

import org.mai.roombooking.dtos.TagDTO;
import org.mai.roombooking.entities.Tag;
import org.mai.roombooking.repositories.TagRepository;
import org.mai.roombooking.services.ValidationService;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class TagController {
    private final TagRepository tagRepository;
    private final ValidationService validationService;

    public TagController(TagRepository tagRepository, ValidationService validationService) {
        this.tagRepository = tagRepository;
        this.validationService = validationService;
    }

    @GetMapping("/get/all")
    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream().map(TagDTO::new).toList();
    }

    @PostMapping("/update")
    public TagDTO update(@RequestBody @NonNull TagDTO dto) {
        validationService.validate(dto);
        return new TagDTO(tagRepository.save(new Tag(dto)));
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable @NonNull Long id) {
        tagRepository.deleteById(id);
    }
}
