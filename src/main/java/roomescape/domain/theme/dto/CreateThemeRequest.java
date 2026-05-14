package roomescape.domain.theme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import roomescape.domain.theme.Theme;

public record CreateThemeRequest(
    @Size(max = 10, message = "이름은 10자 이하여야 합니다. 다시 입력해주세요.")
    @NotBlank(message = "이름은 비어있을 수 없습니다. 10자 이하의 이름을 입력해주세요.")
    String name,

    @NotNull(message = "테마 내용은 필수 입력값 입니다. 테마에 대한 설명을 입력해주세요.")
    String content,

    @NotNull(message = "url은 비어있을 수 없습니다. url을 입력해주세요.")
    String url
) {

    public Theme toEntity() {
        return Theme.createWithoutId(
            name,
            content,
            url
        );
    }
}
