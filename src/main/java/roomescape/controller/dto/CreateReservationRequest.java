package roomescape.controller.dto;

public record CreateReservationRequest(String name, String date, Long timeId, Long themeId) { }
