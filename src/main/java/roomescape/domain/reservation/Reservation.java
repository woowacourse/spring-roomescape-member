package roomescape.domain.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.domain.member.Member;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

public class Reservation {
    private static final int IN_ADVANCE_RESERVATION_DAYS = 1;

    private final Long id;
    @NotNull(message = "예약자는 필수입니다.")
    private final Member member;

    @NotBlank(message = "예약 날짜는 필수입니다.")
    private final LocalDate date;

    @NotBlank(message = "예약 시간은 필수입니다.")
    private final ReservationTime time;

    @NotBlank(message = "예약 테마는 필수입니다.")
    private final Theme theme;

    public Reservation(Member member, LocalDate date, ReservationTime time, Theme theme) {
        this(null, member, date, time, theme);
    }

    public Reservation(Long id, Reservation reservation) {
        this(id, reservation.member, reservation.date, reservation.time, reservation.theme);
    }

    public Reservation(Long id, Member member, LocalDate date, ReservationTime time, Theme theme) {
        Objects.requireNonNull(date, "예약 날짜는 필수입니다.");
        validateReservationDateInAdvance(date, time.getStartAt());

        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateReservationDateInAdvance(LocalDate date, LocalTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        LocalDateTime baseDateTime = LocalDateTime.now().plusDays(IN_ADVANCE_RESERVATION_DAYS);
        if (reservationDateTime.isBefore(baseDateTime)) {
            throw new IllegalArgumentException(String.format("예약은 최소 %d일 전에 해야합니다.", IN_ADVANCE_RESERVATION_DAYS));
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

    public LocalTime getStartAtTime() {
        return time.getStartAt();
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Theme getTheme() {
        return theme;
    }
}
