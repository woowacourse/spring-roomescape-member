package roomescape.theme.dto;

public class PopularThemeResponse {

    private final String themeName;
    private final int reservationCount;

    private PopularThemeResponse(String themeName, int reservationCount) {
        this.themeName = themeName;
        this.reservationCount = reservationCount;
    }

    public static PopularThemeResponse of(String themeName, int reservationCount) {
        return new PopularThemeResponse(
                themeName,
                reservationCount
        );
    }

    public String getThemeName() {
        return themeName;
    }

    public int getReservationCount() {
        return reservationCount;
    }
}
