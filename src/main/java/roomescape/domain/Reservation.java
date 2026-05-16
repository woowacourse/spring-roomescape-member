package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import roomescape.global.exception.reservation.CancelledReservationException;
import roomescape.global.exception.reservation.InvalidReservationException;
import roomescape.global.exception.reservation.SameReservationScheduleException;

@Getter
public class Reservation {

    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 20;
    private static final String NAME_PATTERN = "^[가-힣a-zA-Z ]+$";

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final ReservationStatus status;

    private Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme,
                        ReservationStatus status) {
        validateName(name);
        validateNotNull(date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.status = status;
    }

    public static Reservation createNew(String name, LocalDate date, ReservationTime time, Theme theme) {
        return new Reservation(null, name, date, time, theme, ReservationStatus.RESERVED);
    }

    public static Reservation from(Long id, String name, LocalDate date, ReservationTime time, Theme theme,
                                   ReservationStatus status) {
        return new Reservation(id, name, date, time, theme, status);
    }

    public Reservation changeSchedule(LocalDate date, ReservationTime time) {
        validateReserved();
        validateDifferentSchedule(date, time);
        return new Reservation(id, name, date, time, theme, status);
    }

    public Reservation cancel() {
        validateReserved();
        return new Reservation(id, name, date, time, theme, ReservationStatus.CANCELLED);
    }

    public boolean hasSameSchedule(LocalDate date, ReservationTime time) {
        return this.date.equals(date) && this.time.hasSameStartAt(time);
    }

    public boolean isExpired(LocalDate today, LocalTime now) {
        return this.date.isBefore(today) || this.date.equals(today) && this.time.isBefore(now);
    }

    private void validateDifferentSchedule(LocalDate date, ReservationTime time) {
        if (hasSameSchedule(date, time)) {
            throw new SameReservationScheduleException("이미 같은 일정으로 예약되어 있습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidReservationException("이름은 비어있을 수 없습니다.");
        }
        if (name.length() < MIN_NAME_LENGTH) {
            throw new InvalidReservationException("이름은 " + MIN_NAME_LENGTH + "자 이상이어야 합니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidReservationException("이름은 " + MAX_NAME_LENGTH + "자 이하여야 합니다.");
        }
        if (!name.matches(NAME_PATTERN)) {
            throw new InvalidReservationException("이름은 완성형 한글, 영문, 공백만 허용합니다.");
        }
    }

    private void validateNotNull(LocalDate date, ReservationTime time, Theme theme) {
        if (date == null || time == null || theme == null) {
            throw new InvalidReservationException("예약 날짜, 시간, 테마는 필수입니다.");
        }
    }

    private void validateReserved() {
        if (status == ReservationStatus.CANCELLED) {
            throw new CancelledReservationException("이미 취소된 예약입니다.");
        }
    }
}
