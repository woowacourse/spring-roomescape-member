package roomescape.theme.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Theme {
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private String thumbnail;

    public Theme(final String name, final String description, final String thumbnail) {
        this.id = null;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }
}
