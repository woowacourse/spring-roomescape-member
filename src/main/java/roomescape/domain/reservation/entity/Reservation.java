package roomescape.domain.reservation.entity;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.domain.reservationtime.entity.ReservationTime;
import roomescape.domain.theme.entity.Theme;

public class Reservation {

    private Long id;

    private final String username;

    private Theme theme;

    private LocalDate date;

    private ReservationTime time;

    private Reservation(Long id, String username, Theme theme, LocalDate date, ReservationTime time) {
        this.id = id;
        this.username = username;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public static Reservation create(String username, Theme theme, LocalDate date, ReservationTime time) {
        return new Reservation(null, username, theme, date, time);
    }

    public static Reservation of(Long id, String username, Theme theme, LocalDate date, ReservationTime time) {
        return new Reservation(id, username, theme, date, time);
    }

    public void assignId(Long id) {
        validateAssignableId(id);
        this.id = id;
    }

    private void validateAssignableId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("id는 양수여야 합니다.");
        }

        if (this.id != null) {
            throw new IllegalStateException("이미 id가 할당된 예약입니다.");
        }
    }

    public void update(Theme theme, LocalDate date, ReservationTime time) {
        this.theme = theme;
        this.date = date;
        this.time = time;
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

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (this.id == null) {
            return false;
        }
        Reservation reservation = (Reservation) other;
        return Objects.equals(this.id, reservation.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
