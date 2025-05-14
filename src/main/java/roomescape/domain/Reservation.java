package roomescape.domain;

import java.time.LocalDate;
import roomescape.exception.custom.InvalidInputException;

public class Reservation {

    private final long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final RoomTheme theme;

    public Reservation(final long id,
                       final Member member,
                       final LocalDate date,
                       final ReservationTime time,
                       final RoomTheme theme) {
        validate(member, date, time, theme);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(final Member member,
                       final LocalDate date,
                       final ReservationTime time,
                       final RoomTheme theme) {
        this(0, member, date, time, theme);
    }

    private void validate(final Member member,
                          final LocalDate date,
                          final ReservationTime reservationTime,
                          final RoomTheme theme) {
        if (member == null) {
            throw new InvalidInputException("멤버는 빈 값이 입력될 수 없습니다");
        }
        if (date == null) {
            throw new InvalidInputException("예약 날짜는 빈 값이 입력될 수 없습니다");
        }
        if (reservationTime == null) {
            throw new InvalidInputException("예약 시간은 빈 값이 입력될 수 없습니다");
        }
        if (theme == null) {
            throw new InvalidInputException("테마는 빈 값이 입력될 수 없습니다");
        }
    }

    public long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public RoomTheme getTheme() {
        return theme;
    }
}
