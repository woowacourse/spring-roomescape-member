package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import roomescape.date.domain.ReservationDate;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

public class Reservation {
    private Long id;
    private String name;
    private ReservationDate date;
    private ReservationTime time;
    private Theme theme;
    private ReservationStatus status;

    private Reservation(Long id, String name, ReservationDate reservationDate, ReservationTime time, Theme theme, ReservationStatus status) {
        this.id = id;
        this.name = name;
        this.date = reservationDate;
        this.time = time;
        this.theme = theme;
        this.status = status;
    }

    public static Reservation create(String name, ReservationDate reservationDate, ReservationTime time, Theme theme) {
        validate(name, reservationDate, time, theme);
        validatePast(reservationDate.date(), time.startAt());
        return new Reservation(null, name, reservationDate, time, theme, ReservationStatus.RESERVED);
    }

    public static Reservation load(Long id, String name, ReservationDate reservationDate, ReservationTime time, Theme theme, ReservationStatus status) {
        validate(name, reservationDate, time, theme);
        validateId(id);
        return new Reservation(id, name, reservationDate, time, theme, status);
    }

    public void cancel(String requesterName) {
        validateOwner(requesterName);
        validateNotCanceled();
        validateNotPast(date.date(), time.startAt());

        this.status = ReservationStatus.CANCELED;
    }

    public void changeSchedule(String requesterName, ReservationDate newDate, ReservationTime newTime) {
        validateOwner(requesterName);
        validateNotCanceled();
        validateNotPast(date.date(), time.startAt());
        validateNewScheduleIsPast(newDate.date(), newTime.startAt());

        this.date = newDate;
        this.time = newTime;
    }

    public void changeScheduleByManager(ReservationDate newDate, ReservationTime newTime) {
        validateNotCanceled();
        validateNotPast(date.date(), time.startAt());
        validateNewScheduleIsPast(newDate.date(), newTime.startAt());

        this.date = newDate;
        this.time = newTime;
    }

    private static void validate(String name, ReservationDate reservationDate, ReservationTime time, Theme theme) {
        validateName(name);
        validateDate(reservationDate);
        validateTime(time);
        validateTheme(theme);
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수입니다.");
        }
    }

    private static void validateDate(ReservationDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
    }

    private static void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
    }

    private static void validatePast(LocalDate date, LocalTime time) {
        if (isPast(date, time)) {
            throw new IllegalArgumentException("과거 날짜/시간으로는 예약할 수 없습니다.");
        }
    }

    private static void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("테마는 필수입니다.");
        }
    }

    private static void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("예약 ID는 필수입니다.");
        }
    }

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }

    private void validateOwner(String requesterName) {
        if (!isOwner(requesterName)) {
            throw new IllegalArgumentException("본인의 예약만 취소할 수 있습니다.");
        }
    }

    private void validateNotCanceled() {
        if (status == ReservationStatus.CANCELED) {
            throw new IllegalArgumentException("이미 취소된 예약입니다.");
        }
    }

    private void validateNotPast(LocalDate date, LocalTime time) {
        if (isPast(date, time)) {
            throw new IllegalArgumentException("이미 지난 예약입니다.");
        }
    }

    private void validateNewScheduleIsPast(LocalDate date, LocalTime time) {
        if (isPast(date, time)) {
            throw new IllegalArgumentException("이미 지난 날짜/시간을 예약할 수 없습니다.");
        }
    }

    private boolean isOwner(String requesterName) {
        return this.name.equals(requesterName);
    }

    private static boolean isPast(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time).isBefore(LocalDateTime.now());
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public ReservationDate date() {
        return date;
    }

    public ReservationTime time() {
        return time;
    }

    public Theme theme() {
        return theme;
    }

    public ReservationStatus status() {
        return status;
    }

}
