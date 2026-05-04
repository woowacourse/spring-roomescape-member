package roomescape.domain.Theme;

import java.sql.ResultSet;
import java.sql.SQLException;

public record ThemeDaoData(long id, String name, String description, String imageUrl) {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URL = "image_url";

    public static ThemeDaoData from(long id, ThemeCommand themeCommand) {
        return new ThemeDaoData(id, themeCommand.name(), themeCommand.description(), themeCommand.imageUrl());
    }

    public static ThemeDaoData from(ResultSet resultSet) throws SQLException {
        return new ThemeDaoData(
                resultSet.getLong(COLUMN_ID),
                resultSet.getString(COLUMN_NAME),
                resultSet.getString(COLUMN_DESCRIPTION),
                resultSet.getString(COLUMN_IMAGE_URL)
        );
    }
}
