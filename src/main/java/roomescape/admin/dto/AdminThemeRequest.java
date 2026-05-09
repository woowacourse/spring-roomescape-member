package roomescape.admin.dto;

import jakarta.validation.constraints.Size;

public record AdminThemeRequest(
    @Size(max = 255, message = "[ERROR] 이름은 255자를 초과할 수 없습니다.")
    String name,

    @Size(max = 500, message = "[ERROR] 설명은 500자를 초과할 수 없습니다.")
    String description,

    @Size(max = 255, message = "[ERROR] URL은 255자를 초과할 수 없습니다.")
    String imageUrl
) {

}
