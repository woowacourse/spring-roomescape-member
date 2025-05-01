package roomescape.model;

import java.time.LocalTime;

public record AvailableReservationTime(
        Long id,
        LocalTime startAt,
        Boolean alreadyBooked
) {
    public AvailableReservationTime {
        validateRequiredFields(id, startAt, alreadyBooked);
    }

    private void validateRequiredFields(Long id, LocalTime startAt, Boolean alreadyBooked) {
        if (id == null) {
            throw new IllegalArgumentException("id는  null 일 수 없습니다.");
        }

        if (startAt == null) {
            throw new IllegalArgumentException("시작 시간은 null 일 수 없습니다.");
        }

        if (alreadyBooked == null) {
            throw new IllegalArgumentException("예약 여부 결과는 null 일 수 없습니다.");
        }
    }

}
