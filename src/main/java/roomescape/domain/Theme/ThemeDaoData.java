package roomescape.domain.Theme;

public record ThemeDaoData(long id, String name, String description, String imageUrl) {

    public static ThemeDaoData from(long id, ThemeCommand themeCommand) {
        return new ThemeDaoData(id, themeCommand.name(), themeCommand.description(), themeCommand.imageUrl());
    }

}
