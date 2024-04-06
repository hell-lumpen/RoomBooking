package org.mai.roombooking.controllers;

import lombok.AllArgsConstructor;
import org.mai.roombooking.dtos.TagDTO;
import org.mai.roombooking.entities.Tag;
import org.mai.roombooking.repositories.TagRepository;
import org.mai.roombooking.services.ValidationService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tag")
public class TagController {
    private final TagRepository tagRepository;
    private final ValidationService validationService;

    /**
     * Получение списка всех доступных тегов
     * @return Список доступных тегов
     */
    @GetMapping("/get/all")
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return ResponseEntity.ok(tagRepository.findAll().stream().map(TagDTO::new).toList());
    }

    /**
     * Метод обновления/добавления нового тега
     * @param dto Данные тега
     * @return добавленнный/обнавленный тег
     */
    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<TagDTO> update(@RequestBody @NonNull TagDTO dto) {
        validationService.validate(dto);
        return ResponseEntity.ok(new TagDTO(tagRepository.save(new Tag(dto))));
    }

    /**
     * Метод удаления тега
     * @param id идентификатор тега
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<String> delete(@PathVariable @NonNull Long id) {
        try {
            tagRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } catch (EmptyResultDataAccessException ex) {
            return new ResponseEntity<>("Поптыка удаления несуществующего объекта",
                    HttpStatusCode.valueOf(400));
        }
    }
}
