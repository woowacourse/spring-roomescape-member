package roomescape.dto;

import jakarta.validation.constraints.Size;

public record ThemeRequest(
        @Size(min = 1, max = 20, message = "[ERROR] 테마 이름은 1자 이상 20자 이하입니다.")
        String name,
        
        String description,

        String url) {
}
