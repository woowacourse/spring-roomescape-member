package roomescape.reservation.application;

public interface ReservationReferenceChecker {
    void validateReservationTimeNotReferenced(Long timeId);
    void validateThemeNotReferenced(Long themeId);
}
