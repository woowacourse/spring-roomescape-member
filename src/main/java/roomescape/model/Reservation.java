package roomescape.model;

import java.time.LocalDate;

public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        validateRequiredFields(id, name, date, reservationTime, theme);

        this.id = id;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        validateRequiredFields(name, date, reservationTime, theme);

        this.id = null;
        this.name = name;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public void validateReservationDateInFuture() {
        if (!this.date.isAfter(LocalDate.now())) {
            throw new IllegalStateException("과거 및 당일 예약은 불가능합니다."); //TODO : exception 뭐로 하지?
        }
    }

    private void validateRequiredFields(Long id, String name, LocalDate date, ReservationTime reservationTime,
                                        Theme theme) {
        if (id == null) {
            throw new IllegalArgumentException("id 값은 null 일 수 없습니다.");
        }

        validateRequiredFields(name, date, reservationTime, theme);
    }

    private void validateRequiredFields(String name, LocalDate date, ReservationTime reservationTime,
                                        Theme theme) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자명은 null이거나 공백일 수 없습니다");
        }

        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 null이거나 공백일 수 없습니다");
        }

        if (reservationTime == null) {
            throw new IllegalArgumentException("예약 시각은 null 일 수 없습니다.");
        }

        if (theme == null) {
            throw new IllegalArgumentException("테마는 null 일 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return this.reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}
