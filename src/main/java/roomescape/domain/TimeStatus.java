package roomescape.domain;

public enum TimeStatus {
    DRAFT, AVAILABLE, HOLD, DELETED;

    public boolean isUnavailable() {
        return this != AVAILABLE;
    }
}
