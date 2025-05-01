package roomescape.theme.entity;

import roomescape.exception.BadRequestException;

public class ReservationThemeEntity {

    private final Long id;

    private String name;

    private String description;

    private String thumbnail;

    public ReservationThemeEntity(Long id, String name, String description, String thumbnail) {
        if (id == null
                || name == null || name.isBlank()
                || description == null || description.isBlank()
                || thumbnail == null || thumbnail.isBlank()
        ) {
            throw new BadRequestException("필요한 테마 정보가 모두 입력되지 않았습니다.");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
