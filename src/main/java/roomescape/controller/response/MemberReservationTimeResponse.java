package roomescape.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.service.dto.ReservationTimeDto;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class MemberReservationTimeResponse {

    private final long timeId;
    private final LocalTime startAt;
    private final boolean isBooked;

    public MemberReservationTimeResponse(long timeId, LocalTime startAt, boolean isBooked) {
        this.timeId = timeId;
        this.startAt = startAt;
        this.isBooked = isBooked;
    }

    public static MemberReservationTimeResponse from(ReservationTimeDto time, boolean isBooked) {
        return new MemberReservationTimeResponse(
                time.getId(),
                time.getStartAt().truncatedTo(ChronoUnit.SECONDS),
                isBooked);
    }

    public long getTimeId() {
        return timeId;
    }

    @JsonFormat(pattern = "HH:mm")
    public LocalTime getStartAt() {
        return startAt;
    }

    public boolean getIsBooked() {
        return isBooked;
    }

    @Override
    public String toString() {
        return "MemberReservationTimeResponse{" +
                "timeId=" + timeId +
                ", startAt=" + startAt +
                ", isBooked=" + isBooked +
                '}';
    }
}
