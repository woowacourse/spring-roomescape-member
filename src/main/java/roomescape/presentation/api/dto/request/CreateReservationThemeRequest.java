package roomescape.presentation.api.dto.request;

import roomescape.application.dto.request.CreateReservationThemeServiceRequest;

public record CreateReservationThemeRequest(
        String name,
        String description,
        String thumbnail
) {

    public CreateReservationThemeRequest {
        if (name == null) {
            throw new IllegalArgumentException("테마 이름은 필수로 입력해야 합니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("테마 이름은 비어 있을 수 없습니다.");
        }
        if (description == null) {
            throw new IllegalArgumentException("테마 설명은 필수로 입력해야 합니다.");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("테마 설명은 비어 있을 수 없습니다.");
        }
        if (thumbnail == null) {
            throw new IllegalArgumentException("썸네일은 필수로 입력해야 합니다.");
        }
        if (thumbnail.isBlank()) {
            throw new IllegalArgumentException("썸네일은 비어 있을 수 없습니다.");
        }
    }

    public CreateReservationThemeServiceRequest toServiceRequest() {
        return new CreateReservationThemeServiceRequest(name, description, thumbnail);
    }
}
