package roomescape.dto.reservationTime;

import java.time.format.DateTimeFormatter;
import java.util.List;
import roomescape.domain.ReservationTime;

public record AvailableReservationTimesResponse(
    List<AvailableReservationTimeResponse> times
) {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        List<ReservationTime> allTimes;
        List<ReservationTime> availableTimes;

        public Builder allTimes(List<ReservationTime> allTimes) {
            this.allTimes = allTimes;
            return this;
        }

        public Builder availableTimes(List<ReservationTime> availableTimes) {
            this.availableTimes = availableTimes;
            return this;
        }

        public AvailableReservationTimesResponse build() {
            return new AvailableReservationTimesResponse(
                allTimes.stream()
                    .map(time -> new AvailableReservationTimeResponse(
                        time.getId(),
                        TIME_FORMATTER.format(time.getStartAt()),
                        availableTimes.contains(time))
                    ).toList());
        }
    }

    public static AvailableReservationTimesResponse of(
        List<ReservationTime> allTimes,
        List<ReservationTime> availableTimes
    ) {
        return new AvailableReservationTimesResponse(
            allTimes.stream()
                .map(time -> {
                    boolean available = availableTimes.contains(time);
                    return new AvailableReservationTimeResponse(
                        time.getId(),
                        TIME_FORMATTER.format(time.getStartAt()),
                        available);
                }).toList());
    }
}
