package roomescape.reservation.domain;

import java.util.Objects;
import java.time.LocalDate;
import java.time.LocalDateTime;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.CustomBadRequest;
import roomescape.member.domain.Member;

public class Reservation {

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;
    private final LocalDateTime createdAt;

    public Reservation(Long id, Member member, LocalDate date, ReservationTime time, Theme theme, LocalDateTime createdAt) {
        validateDateTime(date, time, createdAt);

        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.createdAt = createdAt;
    }

    private void validateDateTime(LocalDate date, ReservationTime time, LocalDateTime createdAt) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(createdAt)) {
            throw new CustomException(CustomBadRequest.PAST_TIME_SLOT_RESERVATION);
        }
    }

    public boolean isSameDateTime(Reservation reservation) {
        return date.isEqual(reservation.date) && time.equals(reservation.time);
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Long getThemeId() {
        return theme.getId();
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
