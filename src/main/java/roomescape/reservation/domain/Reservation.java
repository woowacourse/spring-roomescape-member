package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Reservation {

    private final Long id;
    private final ReserverName reserverName;
    private final ReservationDate reservationDate;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(
            Long id,
            String reserverName,
            LocalDate reservationDate,
            ReservationTime reservationTime,
            Theme theme
    ) {
        this.id = Objects.requireNonNull(id, "id는 null일 수 없습니다.");
        this.reserverName = new ReserverName(Objects.requireNonNull(
                reserverName,
                "예약자 이름은 null일 수 없습니다."
        ));
        this.reservationDate = new ReservationDate(Objects.requireNonNull(
                reservationDate,
                "예약일은 null일 수 없습니다."
        ));
        this.reservationTime = Objects.requireNonNull(reservationTime, "예약 시간은 null일 수 없습니다.");
        this.theme = Objects.requireNonNull(theme, "테마는 null일 수 없습니다.");
    }

    public Long getId() {
        return id;
    }

    public String getReserverName() {
        return reserverName.getName();
    }

    public LocalDate getDate() {
        return reservationDate.getDate();
    }

    public LocalTime getStartAt() {
        return reservationTime.getStartAt();
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Long getTimeId() {
        return reservationTime.getId();
    }

    public Theme getTheme() {
        return theme;
    }
}
