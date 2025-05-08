package roomescape.controller.api.dto.request;

import roomescape.service.dto.request.CreateReservationThemeServiceRequest;

public record CreateReservationThemeRequest(
        String name,
        String description,
        String thumbnail
) {

    public CreateReservationThemeServiceRequest toServiceRequest() {
        return new CreateReservationThemeServiceRequest(name, description, thumbnail);
    }
}
