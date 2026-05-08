package roomescape.domain;

public enum ReservationStatus {
    DRAFT, AVAILABLE, HOLD, DELETED;

    public boolean isUnavailable() {
        return this != AVAILABLE;
    }
}
