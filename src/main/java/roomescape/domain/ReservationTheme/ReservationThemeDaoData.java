package roomescape.domain.ReservationTheme;

import java.sql.ResultSet;
import java.sql.SQLException;

public record ReservationThemeDaoData(long id, String name, String description, String imageUrl) {
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_IMAGE_URL = "image_url";

    public static ReservationThemeDaoData from(long id, ReservationThemeCommand reservationThemeCommand) {
        return new ReservationThemeDaoData(id, reservationThemeCommand.name(), reservationThemeCommand.description(), reservationThemeCommand.imageUrl());
    }

    public static ReservationThemeDaoData from(ResultSet resultSet) throws SQLException {
        return new ReservationThemeDaoData(
                resultSet.getLong(COLUMN_ID),
                resultSet.getString(COLUMN_NAME),
                resultSet.getString(COLUMN_DESCRIPTION),
                resultSet.getString(COLUMN_IMAGE_URL)
        );
    }
}
