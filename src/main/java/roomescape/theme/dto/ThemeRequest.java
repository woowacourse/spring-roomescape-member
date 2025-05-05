package roomescape.theme.dto;

import roomescape.common.exception.InvalidNameException;
import roomescape.common.exception.InvalidStringException;

public record ThemeRequest(String name, String description, String thumbnail) {

    public ThemeRequest {
        if (name == null || name.isBlank()) {
            throw new InvalidNameException("이름을 입력해주세요.");
        }
        if (description == null || description.isBlank()) {
            throw new InvalidStringException("설명을 입력해주세요.");
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new InvalidStringException("썸네일을 입력해주세요.");
        }
    }
}
