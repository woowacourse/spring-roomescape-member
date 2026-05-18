package roomescape.common.exception;

public enum DomainType {

    RESERVATION("예약"),
    RESERVATION_TIME("예약 시간"),
    THEME("테마");

    private final String displayName;

    DomainType(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }

}
