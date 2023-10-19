package org.mai.roombooking.entities.dto;

import lombok.Data;

@Data
public class RoomDTO {
    private Long roomId;
    private String roomName;
    private int capacity;
    private boolean hasComputers;
}