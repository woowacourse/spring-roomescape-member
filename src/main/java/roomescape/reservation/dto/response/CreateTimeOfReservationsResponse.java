package roomescape.reservation.dto.response;

public record CreateTimeOfReservationsResponse(Long id, String startAt) {
    public static CreateTimeOfReservationsResponse of(final FindTimeOfReservationsResponse findTimeOfReservationsResponse) {
        return new CreateTimeOfReservationsResponse(
                findTimeOfReservationsResponse.id(),
                findTimeOfReservationsResponse.startAt()
        );
    }
}
