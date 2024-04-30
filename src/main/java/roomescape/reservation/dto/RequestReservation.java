package roomescape.reservation.dto;

import java.time.LocalDate;

public record RequestReservation(String name, LocalDate date, Long timeId) {
}
