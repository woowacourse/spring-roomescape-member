package roomescape.dao.row;

import roomescape.domain.Theme;
import roomescape.domain.vo.Description;
import roomescape.domain.vo.Name;
import roomescape.domain.vo.ThumbnailUrl;

public record ThemeRow(Long id,
                       String name,
                       String thumbnailUrl,
                       String description) implements DomainConvertible<Theme> {

    public ThemeRow(String name, String thumbnailUrl, String description) {
        this(null, name, thumbnailUrl, description);
    }

    public static ThemeRow from(Theme theme) {
        return new ThemeRow(
                theme.getId(),
                theme.getName().value(),
                theme.getThumbnailUrl().value(),
                theme.getDescription().value()
        );
    }

    @Override
    public Theme toDomain() {
        return new Theme(id,
                new Name(name),
                new ThumbnailUrl(thumbnailUrl),
                new Description(description));
    }
}
