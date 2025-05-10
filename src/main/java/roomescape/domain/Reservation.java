package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.exception.InvalidReservationException;

public class Reservation {

    private Long id;
    private final Member member;
    private final ReservationDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(final Long id, final Member member, final ReservationDate date,
                       final ReservationTime reservationTime, final Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(final Member member, final ReservationDate date, final ReservationTime reservationTime,
                       final Theme theme) {
        this.id = null;
        this.member = member;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public void validateDateTime(ReservationDate date, ReservationTime time, LocalDateTime currentDateTime) {
        LocalDate today = currentDateTime.toLocalDate();
        LocalTime todayTime = currentDateTime.toLocalTime();
        if (date.isCurrentDay(today) && time.isBefore(todayTime)) {
            throw new InvalidReservationException("과거 시간으로는 예약할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return getMember().getId();
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getDate() {
        return date.getDate();
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getTimeId() {
        return reservationTime.getId();
    }

    public Long getThemeId() {
        return theme.getId();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Reservation that = (Reservation) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
