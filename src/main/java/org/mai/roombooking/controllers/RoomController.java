package org.mai.roombooking.controllers;

import org.mai.roombooking.dtos.RoomDTO;
import org.mai.roombooking.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    RoomRepository roomRepository;

    @GetMapping
    public List<RoomDTO> getAllRooms() {
        return roomRepository.findAll().stream().map(RoomDTO::new).toList();
    }
}
