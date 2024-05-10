package roomescape.domain.reservation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.domain.member.MemberName;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.ReservationTime;

public class Reservation {
    private static final int IN_ADVANCE_RESERVATION_DAYS = 1;

    private final Long id;
    @NotBlank(message = "예약자명은 필수입니다.")
    @Size(min = 2, max = 10, message = "이름 길이는 2글자 이상, 10글자 이하여야 합니다.")
    private final MemberName name;

    @NotBlank(message = "예약 날짜는 필수입니다.")
    private final LocalDate date;

    @NotBlank(message = "예약 시간은 필수입니다.")
    private final ReservationTime time;

    @NotBlank(message = "예약 테마는 필수입니다.")
    private final Theme theme;

    public Reservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        this(null, name, date, time, theme);
    }

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        this(id, new MemberName(name), date, time, theme);
    }

    public Reservation(Long id, Reservation reservation) {
        this(id, reservation.name, reservation.date, reservation.time, reservation.theme);
    }

    private Reservation(Long id, MemberName name, LocalDate date, ReservationTime time, Theme theme) {
        Objects.requireNonNull(date, "예약 날짜는 필수입니다.");
        validateReservationInAdvance(date, time.getStartAt());

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    private void validateReservationInAdvance(LocalDate date, LocalTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time);
        LocalDateTime baseDateTime = LocalDateTime.now().plusDays(IN_ADVANCE_RESERVATION_DAYS);
        if (reservationDateTime.isBefore(baseDateTime)) {
            throw new IllegalArgumentException(String.format("예약은 최소 %d일 전에 해야합니다.", IN_ADVANCE_RESERVATION_DAYS));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time.getStartAt();
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Theme getTheme() {
        return theme;
    }
}
