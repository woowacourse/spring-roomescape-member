package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;
import roomescape.member.domain.Member;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

public class Reservation {

    private final Long id;
    private final LocalDate date;
    private final Member member;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(final Long id, final LocalDate date, Member member, final ReservationTime time,
                        final Theme theme) {
        validateFields(date);
        this.id = id;
        this.date = date;
        this.member = member;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation load(final Long id, final LocalDate date, final Member member, final ReservationTime time,
                                   final Theme theme) {
        return new Reservation(id, date, member, time, theme);
    }

    public static Reservation create(final LocalDate date, final Member member, final ReservationTime time,
                                     final Theme theme) {
        validateDateTime(date, time);
        return new Reservation(null, date, member, time, theme);
    }

    private static void validateDateTime(final LocalDate localDate, final ReservationTime reservationTime) {
        LocalDateTime dateTime = LocalDateTime.of(localDate, reservationTime.getStartAt());
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException(ExceptionCause.RESERVATION_IMPOSSIBLE_FOR_PAST);
        }
    }

    public void validateFields(LocalDate date) {
        if (date == null) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_RESERVATION_DATE);
        }
        if (member == null) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_RESERVATION_MEMBER);
        }
        if (theme == null) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_THEME);
        }
        if (time == null) {
            throw new BadRequestException(ExceptionCause.EMPTY_VALUE_RESERVATION_TIME);
        }
    }

    public Long getId() {
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

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
