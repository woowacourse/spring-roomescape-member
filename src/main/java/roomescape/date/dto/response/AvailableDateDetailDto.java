package roomescape.date.dto.response;

import java.time.LocalDate;

public record AvailableDateDetailDto(
        LocalDate date
){
    public static AvailableDateDetailDto from(LocalDate localDate) {
        return new AvailableDateDetailDto(localDate);
    }
}
