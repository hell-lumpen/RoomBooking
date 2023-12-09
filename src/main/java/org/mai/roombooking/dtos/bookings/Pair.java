package org.mai.roombooking.dtos.bookings;

import lombok.Data;
import lombok.NonNull;
import org.mai.roombooking.dtos.PairDTO;
import org.mai.roombooking.dtos.bookings.RoomBookingDTO;

import java.util.List;
import java.util.Map;

@Data
public class Pair {
    PairDTO name;
    List<RoomBookingDTO> bookings;

    public Pair(@NonNull Map.Entry<PairDTO, List<RoomBookingDTO>> entry){
        name = entry.getKey();
        bookings = entry.getValue();
    }
}