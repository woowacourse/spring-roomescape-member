package roomescape.reservation.model;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.member.domain.Member;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.theme.model.Theme;

public class Reservation {
    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(final Long id,
                       final Member member,
                       final LocalDate date,
                       final ReservationTime reservationTime,
                       final Theme theme) {
        validateReservationMemberIsNull(member);
        validateReservationDateIsNull(date);
        validateReservationTimeIsNull(reservationTime);
        validateReservationThemeIsNull(theme);

        this.id = id;
        this.member = member;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public static Reservation of(final Long id,
                                 final Reservation reservation) {
        return new Reservation(
                id,
                reservation.getMember(),
                reservation.getDate(),
                reservation.getReservationTime(),
                reservation.getTheme());
    }

    private void validateReservationMemberIsNull(final Member member) {
        if (member == null) {
            throw new IllegalArgumentException("예약 생성 시 예약자는 필수입니다.");
        }
    }

    private void validateReservationDateIsNull(final LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 생성 시 예약 날짜는 필수입니다.");
        }
    }

    private void validateReservationTimeIsNull(final ReservationTime reservationTime) {
        if (reservationTime == null) {
            throw new IllegalArgumentException("예약 생성 시 예약 시간은 필수입니다.");
        }
    }

    private void validateReservationThemeIsNull(final Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("예약 생성 시 예약 테마는 필수입니다.");
        }
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
    }

    public boolean isSameTime(final ReservationTime reservationTime) {
        return this.reservationTime.isSameTo(reservationTime.getId());
    }

    public boolean isSameTimeId(final Long timeId) {
        return this.reservationTime.isSameTo(timeId);
    }

    public boolean isSameTheme(final Long themeId) {
        return this.theme.isSameTo(themeId);
    }

    public boolean isSameDate(final LocalDate date) {
        return Objects.equals(this.date, date);
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

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public boolean equals(final Object o) {
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
        return Objects.hash(id);
    }
}
