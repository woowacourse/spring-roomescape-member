package roomescape.persistence.entity;

import org.springframework.jdbc.core.RowMapper;
import roomescape.business.domain.Theme;

public record ThemeEntity(
        Long id,
        String name,
        String description,
        String thumbnail
) {

    private static final RowMapper<ThemeEntity> DEFAULT_ROW_MAPPER =
            (rs, rowNum) -> new ThemeEntity(
                    rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
            );

    public Theme toDomain() {
        return Theme.createWithId(
                id,
                name,
                description,
                thumbnail
        );
    }

    public static ThemeEntity from(final Theme theme) {
        return new ThemeEntity(
                theme.getId(),
                theme.getName(),
                theme.getDescription(),
                theme.getThumbnail()
        );
    }

    public static RowMapper<ThemeEntity> getDefaultRowMapper() {
        return DEFAULT_ROW_MAPPER;
    }
}
