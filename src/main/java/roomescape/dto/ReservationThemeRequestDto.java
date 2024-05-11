package roomescape.dto;

import roomescape.exception.InvalidParameterException;

public record ReservationThemeRequestDto(String name, String description, String thumbnail) {

    public ReservationThemeRequestDto {
        if (name == null || name.isBlank()) {
            throw new InvalidParameterException("이름을 입력하여야 합니다.");
        }
        if (description == null || description.isBlank()) {
            throw new InvalidParameterException("설명을 입력하여야 합니다.");
        }
        if (thumbnail == null || thumbnail.isBlank()) {
            throw new InvalidParameterException("썸네일 주소를 입력하여야 합니다.");
        }
    }
}
