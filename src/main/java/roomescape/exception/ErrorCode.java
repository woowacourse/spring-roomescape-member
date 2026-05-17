package roomescape.exception;

public enum ErrorCode {
    // Reservation
    PAST_DATE_RESERVATION,
    EXPIRED_RESERVATION,
    DUPLICATE_RESERVATION,
    RESERVATION_NOT_FOUND,

    // ReservationTime
    DUPLICATE_TIME,
    TIME_NOT_FOUND,
    TIME_IN_USE,

    // Theme
    DUPLICATE_THEME,
    THEME_NOT_FOUND,
    THEME_IN_USE,

    // Access
    OWNERSHIP_VIOLATION,

    // Input
    INVALID_DATE_RANGE,

    // Framework / HTTP
    MISSING_HEADER,
    INVALID_PARAMETER_TYPE,
    INVALID_REQUEST_BODY,
    VALIDATION_FAILED,
    INTERNAL_ERROR
}
