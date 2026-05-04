package roomescape.theme.domain;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Theme {

    private Long id;
    private String name;
    private String thumbnailImageUrl;
    private String description;
    private LocalTime durationTime;

    public Theme withId(Long id) {
        return Theme.builder()
                .id(id)
                .name(this.name)
                .thumbnailImageUrl(this.thumbnailImageUrl)
                .description(this.description)
                .durationTime(this.durationTime)
                .build();
    }
}
