package roomescape.domain.ReservationTheme;

import java.sql.ResultSet;
import java.sql.SQLException;

public record ReservationTheme(long id, String name, String description, String imageUrl) {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URL = "image_url";

    public static ReservationTheme from(long id, ReservationThemeCommand command) {
        return new ReservationTheme(id, command.name(), command.description(), command.imageUrl());
    }

    public static ReservationTheme from(ResultSet resultSet) throws SQLException {
        return new ReservationTheme(
                resultSet.getLong(COLUMN_ID),
                resultSet.getString(COLUMN_NAME),
                resultSet.getString(COLUMN_DESCRIPTION),
                resultSet.getString(COLUMN_IMAGE_URL)
        );
    }

}
