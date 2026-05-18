package roomescape.reservation.domain;

import roomescape.exception.BusinessRuleViolationException;
import roomescape.exception.InvalidDomainStateException;
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
    private final ReservationStatus status;

    public Reservation(
            Long id,
            String name,
            LocalDate date,
            ReservationTime time,
            Theme theme,
            ReservationStatus status
    ) {
        validateName(name);
        validateNotNull(date, "예약 날짜는 필수입니다.");
        validateNotNull(time, "예약 시간은 필수입니다.");
        validateNotNull(theme, "예약 테마는 필수입니다.");
        validateNotNull(status, "예약 상태는 필수입니다.");

        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.status = status;
    }

    public static Reservation create(
            String name,
            LocalDate date,
            ReservationTime time,
            Theme theme,
            LocalDateTime now
    ) {
        Reservation reservation = new Reservation(null, name, date, time, theme, ReservationStatus.RESERVED);
        reservation.validateNotPast(now);
        return reservation;
    }

    public Reservation update(
            String name,
            LocalDate date,
            ReservationTime time,
            Theme theme,
            LocalDateTime now
    ) {
        if (!isReserved()) {
            throw new BusinessRuleViolationException("이미 취소되었거나 완료된 예약은 수정할 수 없습니다.");
        }

        Reservation updated = new Reservation(this.id, name, date, time, theme, this.status);
        updated.validateNotPast(now);
        return updated;
    }

    public Reservation convertStatusByCurrentTime(LocalDateTime now) {
        if (isReserved() && isCompleted(now)) {
            return new Reservation(this.id, this.name, this.date, this.time, this.theme, ReservationStatus.COMPLETED);
        }

        return this;
    }

    public boolean isCanceled() {
        return this.status == ReservationStatus.CANCELED;
    }

    public void validateCanCancel(LocalDateTime now) {
        if (isCompleted(now)) {
            throw new BusinessRuleViolationException("이미 이용 완료된 예약은 취소할 수 없습니다.");
        }
    }

    private boolean isReserved() {
        return this.status == ReservationStatus.RESERVED;
    }

    private boolean isCompleted(LocalDateTime now) {
        validateNotNull(now, "현재 시각은 반드시 입력해야 합니다.");
        LocalDateTime reservationDateTime = LocalDateTime.of(this.date, this.time.getStartAt());
        return !reservationDateTime.isAfter(now);
    }

    private void validateNotPast(LocalDateTime now) {
        if (isCompleted(now)) {
            throw new BusinessRuleViolationException("과거 시각으로는 예약할 수 없습니다.");
        }
    }

    private void validateName(String name) {
        validateNotNull(name, "예약자 이름은 반드시 입력해야 합니다.");

        if (name.isBlank()) {
            throw new InvalidDomainStateException("예약자 이름은 반드시 입력해야 합니다.");
        }

        if (name.length() > MAX_NAME_LENGTH) {
            throw new BusinessRuleViolationException(
                    String.format("이름은 %d글자 이하여야 합니다. (현재 이름의 글자 수: %d)", MAX_NAME_LENGTH, name.length())
            );
        }
    }

    private void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new InvalidDomainStateException(message);
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

    public ReservationStatus getStatus() {
        return status;
    }
}
