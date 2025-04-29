package roomescape.reservation.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import roomescape.error.ReservationException;
import roomescape.reservationtime.domain.ReservationTime;

@Getter
@AllArgsConstructor(onConstructor_ = @__(@JsonCreator))
public class Reservation {
    private final Long id;

    @NonNull
    private final String name;

    @NonNull
    private final LocalDate date;

    @NonNull
    private final ReservationTime time;

    public Reservation(final String name, final LocalDate date, final ReservationTime reservationTime) {
        validateFutureOrPresent(LocalDateTime.of(date, reservationTime.getStartAt()));
        this.id = null;
        this.name = name;
        this.date = date;
        this.time = reservationTime;
    }

    private void validateFutureOrPresent(final LocalDateTime dateTime) {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new ReservationException("예약은 현재 시간 이후로 가능합니다.");
        }
    }
}
