package roomescape.dao.dto;

public record AvailableReservationTimeResponse(boolean alreadyBooked, long timeId, String startAt) {

}
