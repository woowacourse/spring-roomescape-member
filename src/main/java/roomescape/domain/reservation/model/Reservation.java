package roomescape.domain.reservation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.domain.member.model.Member;
import roomescape.domain.reservationtime.model.ReservationTime;
import roomescape.domain.theme.model.Theme;

public class Reservation {

    private Long id;
    private final Member member;
    private final ReservationDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(Long id, Member member, ReservationDate date,
        ReservationTime reservationTime, Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(Member member, ReservationDate date, ReservationTime reservationTime,
        Theme theme) {
        this(null, member, date, reservationTime, theme);
    }

    public void validateDateTime(LocalDateTime currentDateTime) {
        if (date.isCurrentDay(currentDateTime.toLocalDate()) &&
            reservationTime.isBefore(currentDateTime.toLocalTime())) {
            throw new IllegalArgumentException("과거 시간으로는 예약할 수 없습니다.");
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberName() {
        return member.getName();
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date.getDate();
    }

    public ReservationDate getReservationDate() {
        return date;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public long getMemberId() {
        return member.getId();
    }

    public long getTimeId() {
        return reservationTime.getId();
    }

    public Theme getTheme() {
        return theme;
    }

    public Long getThemeId() {
        return theme.getId();
    }

    @Override
    public boolean equals(Object o) {
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
