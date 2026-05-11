package roomescape.fixture;

import java.util.Map;
import roomescape.theme.application.dto.ThemeCreateCommand;
import roomescape.theme.application.dto.ThemeQueryResult;

public class ThemeFixture {

    private ThemeFixture() {
    }

    public static ThemeCreateCommand horrorThemeCreateCommand() {
        return new ThemeCreateCommand(
                "공포 테마",
                "공포 테마 설명",
                "http://img.url"
        );
    }

    public static Map<String, String> horrorThemeParams() {
        ThemeCreateCommand command = horrorThemeCreateCommand();
        return Map.of(
                "name", command.name(),
                "description", command.description(),
                "thumbnailImgUrl", command.thumbnailImgUrl()
        );
    }

    public static ThemeCreateCommand themeCreateCommand(int index) {
        return new ThemeCreateCommand(
                "테마 " + index,
                "테마 설명 " + index,
                "http://img.url"
        );
    }

    public static ThemeQueryResult horrorThemeQueryResult(Long id) {
        return new ThemeQueryResult(
                id,
                "공포 테마",
                "공포 테마 설명",
                "http://img.url"
        );
    }

    public static ThemeQueryResult themeQueryResult(int index, Long id) {
        ThemeCreateCommand command = themeCreateCommand(index);
        return new ThemeQueryResult(id, command.name(), command.description(), command.thumbnailImgUrl());
    }
}
