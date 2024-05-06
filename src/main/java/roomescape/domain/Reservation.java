package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {
    private final Long id;
    private final UserName userName;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(
            final Long id,
            final UserName userName,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        validateDate(date);
        validateIsNotBeforeReservation(date, time);
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateDate(final LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 잘못된 예약 날짜 입력입니다.");
        }
    }

    private void validateIsNotBeforeReservation(final LocalDate date, final ReservationTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("[ERROR] 현재 날짜보다 이전의 예약 날짜를 선택할 수 없습니다.");
        }
    }

    public static Reservation of(
            final long id, final String name, final String date,
            final ReservationTime time, final Theme theme
    ) {
        LocalDate parsedDate = LocalDate.parse(date);
        return new Reservation(id, new UserName(name), parsedDate, time, theme);
    }

    public static Reservation of(
            final String name, final LocalDate date,
            final ReservationTime time, final Theme theme) {
        return new Reservation(null, new UserName(name), date, time, theme);
    }

    public Reservation assignId(final long id) {
        return new Reservation(id, userName, date, time, theme);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return userName.getValue();
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public long getTimeId() {
        return time.getId();
    }

    public long getThemeId() {
        return theme.getId();
    }
}
