package roomescape.theme.domain;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Theme {
    private String name;
    private String thumbnailImageUrl;
    private String description;
    private LocalTime durationTime;
}
