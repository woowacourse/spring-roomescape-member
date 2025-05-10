package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.exception.custom.InvalidInputException;
import roomescape.member.domain.Member;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class Reservation {

    private static final int NON_SAVED_STATUS = 0;

    private final long id;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final Member member;

    public Reservation(final long id,
                       final LocalDate date,
                       final ReservationTime time,
                       final Theme theme,
                       final Member member) {
        validateInvalidInput(date, time, theme, member);

        this.id = id;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Reservation(final LocalDate date,
                       final ReservationTime reservationTime,
                       final Theme theme,
                       final Member member) {
        this(NON_SAVED_STATUS, date, reservationTime, theme, member);
    }

    public Reservation(final long id, final Reservation savedReservation) {
        this(id, savedReservation.getDate(), savedReservation.getTime(),
                savedReservation.getTheme(), savedReservation.getMember());
    }

    private void validateInvalidInput(final LocalDate date, final ReservationTime reservationTime,
                                      final Theme theme, final Member member) {
        validateNotNull(date, reservationTime, theme, member);
    }

    private void validateNotNull(final LocalDate date, final ReservationTime reservationTime,
                                 final Theme theme, final Member member) {
        if (date == null) {
            throw new InvalidInputException("예약 날짜는 빈 값이 입력될 수 없습니다");
        }
        if (reservationTime == null) {
            throw new InvalidInputException("예약 시간은 빈 값이 입력될 수 없습니다");
        }
        if (theme == null) {
            throw new InvalidInputException("예약 테마는 빈 값이 입력될 수 없습니다");
        }
        if (member == null) {
            throw new InvalidInputException("멤버는 빈 값이 입력될 수 없습니다");
        }
    }

    public boolean isPastDateAndTime() {
        final LocalDate currentDate = LocalDate.now();

        final boolean isPastDate = date.isBefore(currentDate);
        final boolean isPastTime = date.isEqual(currentDate) && time.isPastTime();

        return isPastDate || isPastTime;
    }

    public long getId() {
        return id;
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

    public Member getMember() {
        return member;
    }
}
