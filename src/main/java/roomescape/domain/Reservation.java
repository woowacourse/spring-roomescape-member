package roomescape.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import roomescape.exception.InvalidOwnershipException;
import roomescape.exception.PastReservationControlException;
import roomescape.exception.PastTimeException;

public record Reservation(Long id, String name, LocalDate date, TimeSlot timeSlot, Theme theme) {

    public Reservation {
        validateName(name);
        validateDate(date);
        validateTimeSlot(timeSlot);
        validateTheme(theme);
    }

    public static Reservation transientOf(String name, LocalDate date, TimeSlot timeSlot, Theme theme) {
        return new Reservation(null, name, date, timeSlot, theme);
    }

    public void validateModifiable(String requesterName, LocalDateTime now) {
        validateOwnership(requesterName);
        validateNotPast(now);
    }

    public Reservation patch(String name, LocalDate date, TimeSlot timeSlot, Theme theme, LocalDateTime now) {
        String patchedName = Objects.requireNonNullElse(name, this.name);
        LocalDate patchedDate = Objects.requireNonNullElse(date, this.date);
        TimeSlot patchedTime = Objects.requireNonNullElse(timeSlot, this.timeSlot);
        Theme patchedTheme = Objects.requireNonNullElse(theme, this.theme);

        validateTargetDateTime(patchedDate, patchedTime, now);
        return new Reservation(this.id, patchedName, patchedDate, patchedTime, patchedTheme);
    }

    private void validateOwnership(String requesterName) {
        if (!this.name.equals(requesterName)) {
            throw new InvalidOwnershipException();
        }
    }

    private void validateNotPast(LocalDateTime now) {
        LocalDateTime reservationDateTime = LocalDateTime.of(this.date, this.timeSlot.startAt());
        if (reservationDateTime.isBefore(now)) {
            throw new PastReservationControlException();
        }
    }

    private void validateTargetDateTime(LocalDate targetDate, TimeSlot targetTimeSlot, LocalDateTime now) {
        LocalDateTime targetDateTime = LocalDateTime.of(targetDate, targetTimeSlot.startAt());
        if (targetDateTime.isBefore(now)) {
            throw new PastTimeException("지난 날짜/시간으로 예약하실 수 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수이며 비어있을 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
    }

    private void validateTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간대입니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }
    }
}
