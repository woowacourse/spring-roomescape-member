package roomescape.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationTimeStatus {

    private final Map<ReservationTime, ReservationStatus> timesStatus;

    private ReservationTimeStatus(Map<ReservationTime, ReservationStatus> timesStatus) {
        this.timesStatus = timesStatus;
    }

    public static ReservationTimeStatus create(List<ReservationTime> allTimes, List<ReservationTime> bookedTimes) {
        Map<ReservationTime, ReservationStatus> timesStatus = new HashMap<>();
        for (ReservationTime time : allTimes) {
            boolean isBooked = bookedTimes.contains(time);
            ReservationStatus status = isBooked ? ReservationStatus.BOOKED : ReservationStatus.AVAILABLE;
            timesStatus.put(time, status);
        }
        return new ReservationTimeStatus(timesStatus);
    }

    public Map<ReservationTime, ReservationStatus> getTimesStatus() {
        return timesStatus;
    }
}
