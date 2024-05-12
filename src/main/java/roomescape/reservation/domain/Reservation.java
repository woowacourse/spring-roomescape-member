package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.member.domain.Member;

public class Reservation {
    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(
            final Long id,
            final Member member,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme
    ) {
        validateDate(date);
        validateIsNotBeforeReservation(date, time);
        this.id = id;
        this.member = member;
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
            final long id, final Member member, final String date,
            final ReservationTime time, final Theme theme
    ) {
        LocalDate parsedDate = LocalDate.parse(date);
        return new Reservation(id, member, parsedDate, time, theme);
    }

    public static Reservation of(final Member member, final LocalDate date, final ReservationTime time,
                                 final Theme theme) {
        return new Reservation(null, member, date, time, theme);
    }

    public Reservation assignId(final long id) {
        return new Reservation(id, member, date, time, theme);
    }

    public long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public long getMemberId() {
        return member.getId();
    }

    public String getName() {
        return member.getNameValue();
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public long getTimeId() {
        return time.getId();
    }

    public Theme getTheme() {
        return theme;
    }

    public long getThemeId() {
        return theme.getId();
    }
}
