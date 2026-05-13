package roomescape.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.service.dto.request.ServiceThemeCreateRequest;

public record ControllerThemeCreateRequest(
        @NotBlank(message = "[ERROR] 이름은 비어 있을 수 없습니다.")
        String name,

        @NotBlank(message = "[ERROR] 설명은 비어 있을 수 없습니다.")
        String description,

        @NotBlank(message = "[ERROR] 썸네일은 비어 있을 수 없습니다.")
        String thumbnailUrl
) {
    public ServiceThemeCreateRequest toServiceThemeRequest() {
        return new ServiceThemeCreateRequest(name, description, thumbnailUrl);
    }
}
