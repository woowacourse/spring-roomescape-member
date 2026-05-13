package roomescape.reservation.domain;

import roomescape.exception.BusinessRuleViolationException;
import roomescape.exception.InvalidRequestException;
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
        validateNotNull(now, "현재 시각은 반드시 입력해야 합니다.");

        if (time.getStartAt() == null) {
            throw new InvalidRequestException("예약 시간은 반드시 입력해야 합니다.");
        }

        LocalDateTime reservationDateTime = LocalDateTime.of(date, time.getStartAt());
        if (reservationDateTime.isBefore(now)) {
            throw new BusinessRuleViolationException("과거 시각으로는 예약할 수 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("예약자 이름은 반드시 입력해야 합니다.");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new BusinessRuleViolationException(
                    String.format("이름은 %d글자 이하여야 합니다. (현재 이름의 글자 수: %d)", MAX_NAME_LENGTH, name.length())
            );
        }
    }

    private void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new InvalidRequestException(message);
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
