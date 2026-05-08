package roomescape.domain.Theme;

import java.sql.ResultSet;
import java.sql.SQLException;

public record ThemeWithCount(long id, String name, String description, String imageUrl, long count) {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URL = "image_url";
    private static final String COLUMN_COUNT = "count";

    public static ThemeWithCount from(ResultSet resultSet) throws SQLException {
        return new ThemeWithCount(
                resultSet.getLong(COLUMN_ID),
                resultSet.getString(COLUMN_NAME),
                resultSet.getString(COLUMN_DESCRIPTION),
                resultSet.getString(COLUMN_IMAGE_URL),
                resultSet.getLong(COLUMN_COUNT)
        );
    }
}
