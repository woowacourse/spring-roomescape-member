package roomescape.reservation.presentation.dto.response;

public record AvailableTimeFindResponse(
        TimeInformation timeInformation,
        boolean isAvailable
) {
}
