package roomescape.model;

import java.time.LocalDate;
import roomescape.common.exception.InvalidInputException;
import roomescape.common.exception.ReservationDateException;

public class Reservation {

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(
            Long id,
            Member member,
            LocalDate date,
            ReservationTime reservationTime,
            Theme theme
    ) {
        validateRequiredFields(id, member, date, reservationTime, theme);

        this.id = id;
        this.member = member;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public Reservation(
            Member member,
            LocalDate date,
            ReservationTime reservationTime,
            Theme theme) {
        validateRequiredFields(member, date, reservationTime, theme);

        this.id = null;
        this.member = member;
        this.date = date;
        this.reservationTime = reservationTime;
        this.theme = theme;
    }

    public void validateReservationDateInFuture() {
        if (!this.date.isAfter(LocalDate.now())) {
            throw new ReservationDateException("과거 및 당일 예약은 불가능합니다.");
        }
    }

    private void validateRequiredFields(
            Long id,
            Member member,
            LocalDate date,
            ReservationTime reservationTime,
            Theme theme
    ) {
        if (id == null) {
            throw new InvalidInputException("id 값은 null 일 수 없습니다.");
        }

        validateRequiredFields(member, date, reservationTime, theme);
    }

    private void validateRequiredFields(
            Member member,
            LocalDate date,
            ReservationTime reservationTime,
            Theme theme
    ) {
        if (member == null) {
            throw new InvalidInputException("예약자는 null일 수 없습니다");
        }

        if (date == null) {
            throw new InvalidInputException("예약 날짜는 null이거나 공백일 수 없습니다");
        }

        if (reservationTime == null) {
            throw new InvalidInputException("예약 시각은 null 일 수 없습니다.");
        }

        if (theme == null) {
            throw new InvalidInputException("테마는 null 일 수 없습니다.");
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
        return this.reservationTime;
    }

    public Theme getTheme() {
        return theme;
    }
}