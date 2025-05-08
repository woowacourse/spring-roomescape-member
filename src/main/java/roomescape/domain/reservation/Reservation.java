package roomescape.domain.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.domain.member.Member;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.theme.Theme;

public class Reservation {

    private final Long id;
    private final Member member;
    private final ReservationDate reservationDate;
    private final ReservationTime reservationTime;
    private final Theme theme;

    public Reservation(
            Long id,
            final Member member,
            LocalDate reservationDate,
            ReservationTime reservationTime,
            Theme theme
    ) {
        this.id = Objects.requireNonNull(id, "id는 null일 수 없습니다.");
        this.member = Objects.requireNonNull(
                member,
                "예약자 이름은 null일 수 없습니다."
        );
        this.reservationDate = new ReservationDate(Objects.requireNonNull(
                reservationDate,
                "예약일은 null일 수 없습니다."
        ));
        this.reservationTime = Objects.requireNonNull(reservationTime, "예약 시간은 null일 수 없습니다.");
        this.theme = Objects.requireNonNull(theme, "테마는 null일 수 없습니다.");
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getDate() {
        return reservationDate.date();
    }

    public LocalTime getStartAt() {
        return reservationTime.getStartAt();
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public Long getTimeId() {
        return reservationTime.getId();
    }

    public Theme getTheme() {
        return theme;
    }
}
