package roomescape.reservation.domain;

import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {

    private static final int MAX_NAME_LENGTH = 10;

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    public Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
        validateName(name);
        validateNotNull(date, "예약 날짜는 필수입니다.");
        validateNotNull(time, "예약 시간은 필수입니다.");
        validateNotNull(theme, "예약 테마는 필수입니다.");

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(
            String name,
            LocalDate date,
            ReservationTime time,
            Theme theme,
            LocalDateTime now
    ) {
        Reservation reservation = new Reservation(null, name, date, time, theme);
        reservation.validateNotPast(now);
        return reservation;
    }

    private void validateNotPast(LocalDateTime now) {
        validateNotNull(now, "현재 시각(now)은 필수입니다.");

        if (time.getStartAt() == null) {
            throw new IllegalStateException("예약 시간 데이터가 올바르지 않습니다. (startAt is null)");
        }

        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException(
                    String.format("과거 시각으로는 예약할 수 없습니다. (요청 일시: %s)", reservationDateTime)
            );
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 비어 있을 수 없습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("이름은 %d글자 이하여야 합니다. (현재 이름의 글자 수: %d)", MAX_NAME_LENGTH, name.length())
            );
        }
    }

    private void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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
}
