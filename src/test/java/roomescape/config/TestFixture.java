package roomescape.config;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.payload.ReservationTimeRequest;
import roomescape.theme.entity.Theme;
import roomescape.theme.payload.ThemeRequest;

public final class TestFixture {

    private TestFixture() {
    }

    public static ThemeRequest themeRequest(String name) {
        return new ThemeRequest(name, name + " 설명", "https://example.com/theme.png");
    }

    public static Theme theme(String name) {
        return Theme.create(name, name + " 설명", "https://example.com/theme.png", Theme.RUNTIME);
    }

    public static ReservationTimeRequest reservationTimeRequest(LocalTime startAt) {
        return new ReservationTimeRequest(startAt);
    }

    public static ReservationTime reservationTime(LocalTime startAt) {
        return ReservationTime.create(startAt);
    }

    public static ReservationRequest reservationRequest(
            String name,
            LocalDate date,
            Long timeId,
            Long themeId
    ) {
        return new ReservationRequest(name, date, timeId, themeId);
    }

    public static Reservation reservation(
            String name,
            LocalDate date,
            ReservationTime reservationTime,
            Theme theme
    ) {
        return Reservation.create(name, date, reservationTime, theme);
    }

    public static Map<String, Object> themeRequestBody(String name, String description, String thumbnailUrl) {
        return Map.of(
                "name", name,
                "description", description,
                "thumbnailUrl", thumbnailUrl
        );
    }

    public static Map<String, Object> reservationTimeRequestBody(String startAt) {
        return Map.of("startAt", startAt);
    }

    public static Map<String, Object> reservationRequestBody(
            String name,
            LocalDate date,
            Long timeId,
            Long themeId
    ) {
        return Map.of(
                "name", name,
                "date", date.toString(),
                "timeId", timeId,
                "themeId", themeId
        );
    }

}
