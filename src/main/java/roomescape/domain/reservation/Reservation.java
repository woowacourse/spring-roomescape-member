package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import roomescape.domain.member.Member;

public class Reservation {

    private final Long id;
    private final Member member;
    private final Theme theme;
    private final LocalDate date;
    private final ReservationTime time;

    public Reservation(Long id, Member member, Theme theme, LocalDate date,
                       ReservationTime time) {
        this.id = id;
        this.member = member;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Reservation(Long id, Long memberId, String email, String password, String memberName, String role,
                       Long themeId, String themeName, String description, String thumbnail, String date, Long timeId,
                       String time) {
        this(id,
                new Member(memberId, email, password, memberName, role),
                new Theme(themeId, themeName, description, thumbnail),
                LocalDate.parse(date),
                new ReservationTime(timeId, time));
    }

    public static boolean isPreviousDate(LocalDate date, ReservationTime time) {
        return LocalDateTime.of(date, time.getStartAt()).isBefore(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Long getMemberId() {
        return member.getId();
    }

    public String getMemberName() {
        return member.getName();
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getThemeId() {
        return theme.getId();
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return time;
    }

    public LocalTime getLocalTime() {
        return time.getStartAt();
    }

    public Long getTimeId() {
        return time.getId();
    }

    public LocalTime getTimeStartAt() {
        return time.getStartAt();
    }
}
