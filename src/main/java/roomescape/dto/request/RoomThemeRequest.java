package roomescape.dto.request;

import jakarta.validation.constraints.NotBlank;
import roomescape.domain.RoomTheme;

public record RoomThemeRequest(
        @NotBlank(message = "테마 이름 입력이 존재하지 않습니다.") String name,
        @NotBlank(message = "테마 설명 입력이 존재하지 않습니다.") String description,
        @NotBlank(message = "테마 썸네일 입력이 존재하지 않습니다.") String thumbnail) {
    public RoomTheme toRoomTheme() {
        return new RoomTheme(name, description, thumbnail);
    }
}
