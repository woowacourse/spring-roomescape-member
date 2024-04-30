package roomescape.reservationtime.dto.response;

public record CreateReservationTimeResponse(Long id, String startAt) {
    public static CreateReservationTimeResponse of(final FindReservationTimeResponse findReservationTimeResponse) {
        return new CreateReservationTimeResponse(
                findReservationTimeResponse.id(),
                findReservationTimeResponse.startAt());
    }
}
