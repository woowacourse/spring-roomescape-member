package roomescape.domain;

public enum ReservationStatus {
    PENDING("대기"),
    CONFIRMED("확인"),
    COMPLETED("완료"),
    CANCELLED("취소");

    private final String name;

    ReservationStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
