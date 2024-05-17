package roomescape.domain.reservation;

public enum Purpose {
    CREATE,
    SEARCH;

    public boolean isCreate() {
        return this == CREATE;
    }
}
