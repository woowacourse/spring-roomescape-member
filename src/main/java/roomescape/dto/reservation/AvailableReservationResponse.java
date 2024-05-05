package roomescape.dto.reservation;

public record AvailableReservationResponse(String startAt, Long timeId, boolean alreadyBooked) {

    public static AvailableReservationResponse of(String startAt, Long timeId, boolean alreadyBooked) {
        return new AvailableReservationResponse(startAt, timeId, alreadyBooked);
    }
}
