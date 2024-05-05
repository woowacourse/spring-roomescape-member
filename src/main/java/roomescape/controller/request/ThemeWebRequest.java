package roomescape.controller.request;

import jakarta.validation.constraints.NotNull;

public record ThemeWebRequest(
    @NotNull(message = "잘못된 테마 이름을 입력하셨습니다.") String name,
    @NotNull(message = "잘못된 테마 설명을 입력하셨습니다.") String description,
    @NotNull(message = "잘못된 형식의 썸네일 url입니다.") String thumbnail) {
}
