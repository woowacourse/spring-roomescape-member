package roomescape.reservation.domain;

public final class ReservationName {
    private final String name;

    public ReservationName(String name) {
        if(name == null || name.trim().isEmpty()){
            throw new IllegalArgumentException("예약자 이름은 필수 입력입니다.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
