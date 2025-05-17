package roomescape.business.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    private Long id;

    private final User user;
    private final LocalDate date;
    private final PlayTime playTime;
    private final Theme theme;

    public Reservation(
            final User user,
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        this(null, user, date, playTime, theme);
    }

    private Reservation(
            final Long id,
            final User user,
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        validateNonNull(user, date, playTime, theme);
        this.id = id;
        this.user = user;
        this.date = date;
        this.playTime = playTime;
        this.theme = theme;
    }

    public boolean isBefore(final LocalDateTime dateTime) {
        final LocalDateTime reservationDateTime = LocalDateTime.of(date, playTime.getStartAt());

        return reservationDateTime.isBefore(dateTime);
    }

    private void validateNonNull(
            final User user,
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        if (user == null) {
            throw new IllegalArgumentException("user가 null 입니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("date가 null 입니다.");
        }
        if (playTime == null) {
            throw new IllegalArgumentException("time이 null 입니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("theme가 null 입니다.");
        }
    }

    public static Reservation createWithId(
            final Long id,
            final User user,
            final LocalDate date,
            final PlayTime playTime,
            final Theme theme
    ) {
        if (id == null) {
            throw new IllegalArgumentException("id가 null 입니다.");
        }

        return new Reservation(id, user, date, playTime, theme);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDate() {
        return date;
    }

    public PlayTime getPlayTime() {
        return playTime;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user)
               && Objects.equals(date, that.date) && Objects.equals(playTime, that.playTime)
               && Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, date, playTime, theme);
    }
}
