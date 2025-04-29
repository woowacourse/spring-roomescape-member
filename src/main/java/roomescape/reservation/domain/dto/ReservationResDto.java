package roomescape.reservation.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import roomescape.reservationTime.domain.dto.ReservationTimeResDto;

public class ReservationResDto {

    @JsonProperty
    private final Long id;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final LocalDate date;

    @JsonProperty("time")
    private final ReservationTimeResDto reservationTimeResDto;

    public ReservationResDto(Long id, String name, LocalDate date, ReservationTimeResDto reservationTimeResDto) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTimeResDto = reservationTimeResDto;
    }
}
