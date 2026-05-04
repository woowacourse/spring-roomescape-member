package roomescape.theme.entity;

import java.time.Duration;

public class Theme {

    private final Long id;
    private final String name;
    private final String description;
    private final String thumbnailUrl;
    private final Duration runtime = Duration.ofHours(1);

    public Theme(Long id, String name, String description, String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
    }
}
