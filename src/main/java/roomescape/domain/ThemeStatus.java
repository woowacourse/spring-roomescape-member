package roomescape.domain;

public enum ThemeStatus {
    DRAFT, AVAILABLE, DELETED;

    public boolean isUnavailable() {
        return this != AVAILABLE;
    }
}
