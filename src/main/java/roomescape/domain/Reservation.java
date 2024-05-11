package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final Member member;
    private final Theme theme;
    private final ReservationDate date;
    private final ReservationTime time;

    public Reservation(Long id, Member member, Theme theme, ReservationDate date, ReservationTime time) {
        this.id = id;
        this.member = member;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Reservation(Long id, Long memberId, String name, String role, String email, String password,
                       Long themeId, String themeName, String description, String thumbnail,
                       String date, Long timeId, String time) {
        this(id, new Member(memberId, name, role, email, password),
                new Theme(themeId, themeName, description, thumbnail), new ReservationDate(date),
                new ReservationTime(timeId, time));
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public LocalDate getDate() {
        return date.getDate();
    }

    public ReservationTime getReservationTime() {
        return time;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public LocalTime getTimeStartAt() {
        return time.getStartAt();
    }

    public Member getMember() {
        return member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
