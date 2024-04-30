package roomescape.reservation.dto.response;

public record CreateReservationResponse(Long id, String name, String date, CreateTimeOfReservationsResponse time) {
    // TODO: domain을 받느 수정
    public static CreateReservationResponse of(final FindReservationResponse findReservationResponse) {
        return new CreateReservationResponse(
                findReservationResponse.id(),
                findReservationResponse.name(),
                findReservationResponse.date(),
                CreateTimeOfReservationsResponse.of(findReservationResponse.time())
        );
    }
}
