package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Reservation {

    private final Long id;

    @NonNull
    private final Member member;

    @NonNull
    private final LocalDate date;

    @NonNull
    private final ReservationTime time;

    @NonNull
    private final Theme theme;

    public Reservation(@NonNull final Member member, @NonNull final LocalDate date,
                       @NonNull final ReservationTime reservationTime, @NonNull final Theme theme) {
        this.id = null;
        this.member = member;
        this.date = date;
        this.time = reservationTime;
        this.theme = theme;
    }

    public LocalTime extractTime() {
        return time.getStartAt();
    }
}
