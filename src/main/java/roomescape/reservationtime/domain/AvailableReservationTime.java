package roomescape.reservationtime.domain;

import java.util.List;
import roomescape.exception.custom.InvalidInputException;

public class AvailableReservationTime {

    private final ReservationTime reservationTime;
    private final boolean bookedStatus;

    public AvailableReservationTime(final List<ReservationTime> bookedTimes, final ReservationTime reservationTime) {
        validateInvalidInput(bookedTimes, reservationTime);
        this.reservationTime = reservationTime;
        this.bookedStatus = isBooked(bookedTimes, reservationTime);
    }

    private void validateInvalidInput(final List<ReservationTime> bookedTimes, final ReservationTime reservationTime) {
        if (bookedTimes == null) {
            throw new InvalidInputException("예약된 시간 목록은 null이 될 수 없습니다");
        }

        if (reservationTime == null) {
            throw new InvalidInputException("예약 시간은 null이 될 수 없습니다");
        }
    }

    private boolean isBooked(final List<ReservationTime> bookedTimes, final ReservationTime reservationTime) {
        if (bookedTimes.isEmpty()) {
            return false;
        }

        return bookedTimes.contains(reservationTime);
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public boolean getBookedStatus() {
        return bookedStatus;
    }
}
