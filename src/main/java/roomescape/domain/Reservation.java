package roomescape.domain;

import java.time.LocalDate;

public record Reservation(long id, LocalDate date, Member member, ReservationTime time, Theme theme) {
}
