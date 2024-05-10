package roomescape.service.dto;

import java.util.List;

public class ReservationTimeInfoDto {

    private final List<ReservationTimeDto> bookedTimes;
    private final List<ReservationTimeDto> notBookedTimes;

    public ReservationTimeInfoDto(List<ReservationTimeDto> bookedTimes, List<ReservationTimeDto> notBookedTimes) {
        this.bookedTimes = bookedTimes;
        this.notBookedTimes = notBookedTimes;
    }

    public List<ReservationTimeDto> getBookedTimes() {
        return bookedTimes;
    }

    public List<ReservationTimeDto> getNotBookedTimes() {
        return notBookedTimes;
    }
}
