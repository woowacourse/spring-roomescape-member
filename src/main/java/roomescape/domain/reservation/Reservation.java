package roomescape.domain.reservation;

import roomescape.domain.exception.InvalidDomainObjectException;
import roomescape.domain.member.Member;
import roomescape.domain.theme.Theme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {
    private final Long id;
    private final Member member;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Member member, LocalDate date, ReservationTime time, Theme theme) {
        this(null, member, new ReservationDate(date), time, theme);
    }

    public Reservation(Long id, Member member, ReservationDate date, ReservationTime time, Theme theme) {
        validate(member, date, time, theme);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validate(Member member, ReservationDate date, ReservationTime time, Theme theme) {
        validateMember(member);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
    }

    private void validateMember(Member member) {
        if (Objects.isNull(member)) {
            throw new InvalidDomainObjectException("멤버는 null이 될 수 없습니다.");
        }
    }

    private void validateDate(ReservationDate date) {
        if (Objects.isNull(date)) {
            throw new InvalidDomainObjectException("날짜는 null이 될 수 없습니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (Objects.isNull(time)) {
            throw new InvalidDomainObjectException("시간은 null이 될 수 없습니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (Objects.isNull(theme)) {
            throw new InvalidDomainObjectException("테마는 null이 될 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(this.date.getStartAt(), this.time.getStartAt());
    }

    public boolean isBeforeNow() {
        LocalDateTime reservationDataTime = LocalDateTime.of(date.getStartAt(), time.getStartAt());
        LocalDateTime currentDateTime = LocalDateTime.now();
        return reservationDataTime.isBefore(currentDateTime);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Reservation that = (Reservation) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
