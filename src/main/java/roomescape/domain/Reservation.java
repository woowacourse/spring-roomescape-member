package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Reservation {

    private static final LocalDate LIMIT_DATE = LocalDate.now();
    private static final LocalTime LIMIT_TIME = LocalTime.now();

    private final Long id;
    private final Member member;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Member member, ReservationDate date, ReservationTime time, Theme theme) {
        this(null, member, date, time, theme);
    }

    public Reservation(Long id, Member member, ReservationDate date, ReservationTime time, Theme theme) {
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public void validateDateTime() {
        if (date.isBefore(LIMIT_DATE)) {
            throw new IllegalStateException("예약 날짜는 예약 가능한 기간보다 이전일 수 없습니다.");
        }

        if (date.isLimitDate(LIMIT_DATE) && time.isBeforeOrSame(LIMIT_TIME)) {
            throw new IllegalStateException("예약 시간은 예약 가능한 시간 이전이거나 같을 수 없습니다.");
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
        return Objects.hash(id);
    }
}
