package roomescape.model;

import java.time.LocalTime;
import roomescape.common.exception.InvalidInputException;

public record AvailableReservationTime(
        Long id,
        LocalTime startAt,
        Boolean alreadyBooked
) {
    public AvailableReservationTime {
        validateRequiredFields(id, startAt, alreadyBooked);
    }

    private void validateRequiredFields(Long id, LocalTime statAt, Boolean alreadyBooked) {
        if (id == null) {
            throw new InvalidInputException("id는  null 일 수 없습니다.");
        }

        if (statAt == null) {
            throw new InvalidInputException("시작 시간은 null 일 수 없습니다.");
        }

        if (alreadyBooked == null) {
            throw new InvalidInputException("예약 여부 결과는 null 일 수 없습니다.");
        }
    }
}
