package org.mai.roombooking.dtos;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

@Data
public class Pair {
    String name;

    @Override
    public String toString() {
        return "Pair{" +
                "name='" + name + '\'' +
                ", bookings=" + bookings +
                '}';
    }

    List<RoomBookingDTO> bookings;

    public Pair(@NonNull Map.Entry<String, List<RoomBookingDTO>> entry){
        name = entry.getKey();
        bookings = entry.getValue();
    }
}