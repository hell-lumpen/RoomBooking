package org.mai.roombooking.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mai.roombooking.entities.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDTO {
    @NotNull
    @NotBlank
    private String description;

    @Pattern(regexp = "^(ЛР|ПЗ|ЛК)$")
    private String tag;

    private String employee;

    @NotNull
    @NotBlank
    private String room;

    private String groupBookingId;

    @NotNull
    @NotEmpty
    private List<String> groups;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
