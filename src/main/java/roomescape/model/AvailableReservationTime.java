package roomescape.model;

import java.time.LocalTime;

public class AvailableReservationTime extends ReservationTime {

    private final Boolean alreadyBooked;

    public AvailableReservationTime(Long id, LocalTime startAt, Boolean alreadyBooked) {
        super(id, startAt);
        validateRequiredFields(alreadyBooked);
        this.alreadyBooked = alreadyBooked;
    }

    private void validateRequiredFields(Boolean alreadyBooked) {
        if (alreadyBooked == null) {
            throw new IllegalArgumentException("예약 여부 결과는 null 일 수 없습니다.");
        }
    }

    public Boolean getAlreadyBooked() {
        return alreadyBooked;
    }
}
