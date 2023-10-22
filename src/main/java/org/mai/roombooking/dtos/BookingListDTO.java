package org.mai.roombooking.dtos;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.*;

@Data
@NoArgsConstructor
public class BookingListDTO {
    List<Pair> data;

    public BookingListDTO(@NonNull Set<Map.Entry<String, List<RoomBookingDTO>>> set) {
        data = new ArrayList<>();
        for (var entry : set) {
            data.add(new Pair(entry));
        }
    }

    private class Pair {
        String name;
        List<RoomBookingDTO> bookings;

        Pair(@NonNull Map.Entry<String, List<RoomBookingDTO>> entry){
            name = entry.getKey();
            bookings = entry.getValue();
        }
    }
}
