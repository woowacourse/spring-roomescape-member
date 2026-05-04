package roomescape.theme.domain;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Theme {
    @Setter
    private Long id;
    private String name;
    private String thumbnailImageUrl;
    private String description;
    private LocalTime durationTime;
}
