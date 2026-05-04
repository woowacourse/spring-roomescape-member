package roomescape.theme.repository;

import roomescape.theme.entity.Theme;

public interface ThemeRepository {

    Theme save(String name, String description, String thumbnailUrl);

}
