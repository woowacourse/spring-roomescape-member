package roomescape.reservation.domain;

import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.ValidateException;
import roomescape.theme.domain.Theme;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(final LocalDate date, final ReservationTime reservationTime, final Theme theme) {
        this(null, date, reservationTime, theme);
    }

    public Reservation(final Long id, final LocalDate date, final ReservationTime reservationTime, final Theme theme) {
        this.id = id;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;

        validateBlank();
    }

    private void validateBlank() {
        if (date == null || reservationTime == null || theme == null) {
            throw new ValidateException(ErrorType.RESERVATION_REQUEST_DATA_BLANK,
                    String.format("예약(Reservation) 생성에 유효하지 않은 값(null OR 공백)이 입력되었습니다. [values: %s]", this));
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + reservationTime +
                ", theme=" + theme +
                '}';
    }
}
