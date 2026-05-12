package roomescape.theme.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Builder;
import roomescape.theme.domain.Theme;

@Builder
public record ThemeRequest(
        @NotBlank(message = "테마 이름은 필수입니다.")
        String name,
        @NotBlank(message = "썸네일 이미지 URL은 필수입니다.")
        String thumbnailImageUrl,
        @NotBlank(message = "테마 설명은 필수입니다.")
        String description,
        @NotNull(message = "진행 시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime durationTime
) {
    public Theme toEntity() {
        return Theme.builder()
                .name(name)
                .description(description)
                .thumbnailImageUrl(thumbnailImageUrl)
                .durationTime(durationTime)
                .build();
    }
}
