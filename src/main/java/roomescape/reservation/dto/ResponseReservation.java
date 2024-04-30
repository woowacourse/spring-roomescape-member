package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.time.dto.ResponseTime;

public record ResponseReservation(Long id, String name, LocalDate date, ResponseTime time) {
}
