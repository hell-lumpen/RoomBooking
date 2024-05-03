package org.mai.roombooking.controllers;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mai.roombooking.dtos.RoomDTO;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.entities.Equipment;
import org.mai.roombooking.entities.Room;
import org.mai.roombooking.repositories.EquipmentRepository;
import org.mai.roombooking.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<Equipment> getLike() {
        return equipmentRepository.findAll();

    }


}
