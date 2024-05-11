package roomescape.controller.reservation;

public record SearchReservationRequest(Long themeId, Long memberId, String dateFrom, String dateTo) {

    public boolean existNull() {
        return themeId == null || memberId == null || dateFrom == null || dateTo == null;
    }
}
