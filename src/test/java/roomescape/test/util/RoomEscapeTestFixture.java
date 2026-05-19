package roomescape.test.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.EntityId;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@TestComponent
@RequiredArgsConstructor
public class RoomEscapeTestFixture {

    private final JdbcTemplate jdbcTemplate;

    public static final int INITIALIZED_RESERVATION_COUNT = 2;
    public static final int INITIALIZED_TIME_COUNT = 2;
    public static final int INITIALIZED_THEME_COUNT = 2;

    public static final ReservationTime TIME_IN_USE = new ReservationTime(
            EntityId.random(),
            LocalTime.of(10, 0)
    );
    public static final ReservationTime TIME_NOT_IN_USE = new ReservationTime(
            EntityId.random(),
            LocalTime.of(14, 0)
    );

    public static final Theme THEME_IN_USE = new Theme(
            EntityId.random(),
            "A theme",
            "A theme description",
            "A image url"
    );
    public static final Theme THEME_NOT_IN_USE = new Theme(
            EntityId.random(),
            "B theme",
            "B theme description",
            "B image url"
    );

    public static final Reservation FASTER_RESERVATION = Reservation.create(
            EntityId.random(),
            "A reservation name",
            LocalDate.now().plusYears(1),
            TIME_IN_USE,
            THEME_IN_USE.id()
    );
    public static final Reservation LATER_RESERVATION = Reservation.create(
            EntityId.random(),
            "B reservation name",
            LocalDate.now().plusYears(2),
            TIME_IN_USE,
            THEME_IN_USE.id()
    );

    public static final ReservationTime INSERTABLE_TIME = new ReservationTime(
            EntityId.random(),
            LocalTime.of(18, 0)
    );
    public static final Theme INSERTABLE_THEME = new Theme(
            EntityId.random(),
            "C theme",
            "C theme description",
            "C image url"
    );

    public static final LocalDate FUTURE_DATE = LocalDate.now().plusDays(1);
    public static final LocalDate PAST_DATE = LocalDate.now().minusDays(1);

    public void clearTables() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
    }

    public void insertInitialData() {
        insertTime(TIME_IN_USE);
        insertTime(TIME_NOT_IN_USE);
        insertTheme(THEME_IN_USE);
        insertTheme(THEME_NOT_IN_USE);
        insertReservationWithFixedId(FASTER_RESERVATION);
        insertReservationWithFixedId(LATER_RESERVATION);
    }

    public String insertReservation(
            String name,
            LocalDate date,
            EntityId timeId,
            EntityId themeId
    ) {
        boolean defaultCanceled = false;

        return insertReservation(name, date, defaultCanceled, timeId, themeId);
    }

    public String insertReservation(
            String name,
            LocalDate date,
            boolean canceled,
            EntityId timeId,
            EntityId themeId
    ) {
        EntityId reservationId = EntityId.random();

        return insertReservationWithFixedId(reservationId, name, date, canceled, timeId, themeId);
    }

    private String insertReservationWithFixedId(Reservation reservation) {
        return insertReservationWithFixedId(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                reservation.isCanceled(),
                reservation.getTimeId(),
                reservation.getThemeId()
        );
    }

    private String insertReservationWithFixedId(
            EntityId reservationId,
            String name,
            LocalDate date,
            boolean canceled,
            EntityId timeId,
            EntityId themeId
    ) {
        String id = reservationId.getValueAsString();
        jdbcTemplate.update(
                "INSERT INTO reservation (id, name, date, canceled, time_id, theme_id) VALUES (?, ?, ?, ?, ?, ?)",
                UUID.fromString(id),
                name,
                date,
                canceled,
                timeId.getValueAsUuid(),
                themeId.getValueAsUuid()
        );
        return id;
    }

    public void insertTime(ReservationTime time) {
        insertTime(time.id(), time.startAt());
    }

    public void insertTime(EntityId timeId, LocalTime startAt) {
        jdbcTemplate.update(
                "INSERT INTO reservation_time (id, start_at) VALUES (?, ?)",
                UUID.fromString(timeId.getValueAsString()),
                startAt
        );
    }

    public void insertTheme(Theme theme) {
        insertTheme(theme.id(), theme.name(), theme.description(), theme.imageUrl());
    }

    public void insertTheme(EntityId themeId, String name, String description, String imageUrl) {
        jdbcTemplate.update(
                "INSERT INTO theme (id, name, description, image_url) VALUES (?, ?, ?, ?)",
                UUID.fromString(themeId.getValueAsString()),
                name,
                description,
                imageUrl
        );
    }
}
