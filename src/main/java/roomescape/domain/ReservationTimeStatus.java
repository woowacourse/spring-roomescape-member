package roomescape.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationTimeStatus {

    private final Map<ReservationTime, ReservationStatus> timesStatus;

    public ReservationTimeStatus(List<ReservationTime> allTimes, List<ReservationTime> bookedTimes) {
        timesStatus = new HashMap<>();
        for (ReservationTime time : allTimes) {
            boolean isBooked = bookedTimes.contains(time);
            ReservationStatus status = isBooked ? ReservationStatus.BOOKED : ReservationStatus.AVAILABLE;
            timesStatus.put(time, status);
        }
    }

    public Map<ReservationTime, ReservationStatus> getTimesStatus() {
        return timesStatus;
    }
}
