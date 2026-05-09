package roomescape.domain.reservation.entity;

import java.time.LocalDate;
import roomescape.domain.reservationtime.entity.ReservationTime;
import roomescape.domain.theme.entity.Theme;

public class Reservation {

    private Long id;

    private final String username;

    private final Theme theme;

    private final LocalDate date;

    private final ReservationTime time;

    public Reservation(Long id, String username, Theme theme, LocalDate date, ReservationTime time) {
        validateId(id);
        validateUsername(username);
        validateTheme(theme);
        validateDate(date);
        validateTime(time);

        this.id = id;
        this.username = username;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public static Reservation create(String username, Theme theme, LocalDate date, ReservationTime time) {
        return new Reservation(null, username, theme, date, time);
    }

    public void assignId(Long id) {
        validateAssignableId(id);
        this.id = id;
    }

    private void validateId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("id는 양수여야 합니다.");
        }
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username은 비어 있을 수 없습니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("theme은 null일 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("date는 null일 수 없습니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (time == null) {
            throw new IllegalArgumentException("time은 null일 수 없습니다.");
        }
    }

    private void validateAssignableId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id는 null일 수 없습니다.");
        }

        validateId(id);

        if (this.id != null) {
            throw new IllegalStateException("이미 id가 할당된 예약입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }
}
