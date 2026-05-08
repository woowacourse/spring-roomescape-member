package roomescape.theme.controller;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ThemeRequest(
        @NotBlank(message = "이름이 입력되어야 합니다.")
        String name,
        @NotBlank(message = "설명이 입력되어야 합니다.")
        String description,
        @NotBlank(message = "썸네일 주소가 입력되어야합니다.")
        @URL(message = "유효한 썸네일 URL 형식이 아닙니다. (예: http://...)")
        String thumbnail) {
}
