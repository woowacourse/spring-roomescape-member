package roomescape.model;

import roomescape.common.exception.InvalidInputException;

public record AvailableReservationTime(
        ReservationTime reservationTime,
        Boolean alreadyBooked
) {
    public AvailableReservationTime {
        validateRequiredFields(reservationTime, alreadyBooked);
    }

    private void validateRequiredFields(ReservationTime reservationTime, Boolean alreadyBooked) {
        if (reservationTime.getId() == null) {
            throw new InvalidInputException("id는  null 일 수 없습니다.");
        }

        if (reservationTime.getStartAt() == null) {
            throw new InvalidInputException("시작 시간은 null 일 수 없습니다.");
        }

        if (alreadyBooked == null) {
            throw new InvalidInputException("예약 여부 결과는 null 일 수 없습니다.");
        }
    }
}
