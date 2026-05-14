package roomescape.test.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.EntityId;

@TestComponent
@RequiredArgsConstructor
public class RoomEscapeTestFixture {

    private final JdbcTemplate jdbcTemplate;

    // 초기 데이터 상수
    public static final int INITIALIZED_RESERVATION_COUNT = 3;
    public static final int INITIALIZED_TIME_COUNT = 2;
    public static final EntityId MORNING_TIME_ID = EntityId.fromUuid(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa01"));
    public static final EntityId AFTERNOON_TIME_ID = EntityId.fromUuid(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaa02"));
    public static final EntityId WESTERN_THEME_ID = EntityId.fromUuid(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb01"));
    public static final EntityId SECRET_GARDEN_THEME_ID = EntityId.fromUuid(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbb02"));

    // 초기 데이터 예약 ID
    public static final EntityId BROWN_RESERVATION_ID = EntityId.fromUuid(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccc01"));
    public static final EntityId KEMPT_RESERVATION_ID = EntityId.fromUuid(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccc02"));
    public static final EntityId JASON_RESERVATION_ID = EntityId.fromUuid(UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccc03"));

    // 초기 데이터 예약 날짜
    public static final LocalDate BROWN_RESERVATION_DATE = LocalDate.of(2026, 5, 8);
    public static final LocalDate KEMPT_RESERVATION_DATE = LocalDate.of(2026, 5, 9);
    public static final LocalDate JASON_RESERVATION_DATE = LocalDate.of(2026, 5, 10);

    // 테스트용 데이터 상수
    public static final EntityId TEST_TIME_ID = EntityId.fromUuid(UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddd01"));
    public static final EntityId TEST_THEME_ID = EntityId.fromUuid(UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeee01"));

    public static final LocalDate FUTURE_DATE = LocalDate.now().plusDays(1);
    public static final LocalDate PAST_DATE = LocalDate.now().minusDays(1);

    // data.sql의 이미지 URL
    private static final String WESTERN_IMAGE_URL = "https://i.namu.wiki/i/A1AtvH502V57OxN_IuPqwui9jFHsjBZ18IFkFoBvHfBHSfGDYN9yFmARz6AlyM9AYJDhK1aiqnY5BcVIdWHFcA.webp";
    private static final String SECRET_GARDEN_IMAGE_URL = "https://i.namu.wiki/i/OoI83MDV7W2tuNPf3NCSQADLcng4cRTqQ15nP6JEatDQniUxC800zbwzYBqq2TOE3KhFKXy140SpWfl6uL2d5A.webp";

    public void clearTables() {
        TestDatabaseUtils.clearTables(jdbcTemplate);
    }

    public void insertTestTimeAndTheme() {
        insertTime(TEST_TIME_ID, LocalTime.of(10, 0));
        insertTheme(TEST_THEME_ID, "themeName", "themeDescription", "themeUrl");
    }

    public void insertAnotherTime() {
        insertTime(AFTERNOON_TIME_ID, LocalTime.of(14, 0));
    }

    public void insertInitialData() {
        insertTime(MORNING_TIME_ID, LocalTime.of(10, 0));
        insertTime(AFTERNOON_TIME_ID, LocalTime.of(14, 0));

        insertTheme(WESTERN_THEME_ID, "웨스턴", "서부 시대 미국 배경 추리 테마입니다.", WESTERN_IMAGE_URL);
        insertTheme(SECRET_GARDEN_THEME_ID, "비밀의 화원", "잊혀진 정원 속 비밀을 풀어나가는 미스터리 테마입니다.", SECRET_GARDEN_IMAGE_URL);

        insertReservationWithFixedId(BROWN_RESERVATION_ID, "브라운", BROWN_RESERVATION_DATE, MORNING_TIME_ID, WESTERN_THEME_ID);
        insertReservationWithFixedId(KEMPT_RESERVATION_ID, "검프", KEMPT_RESERVATION_DATE, AFTERNOON_TIME_ID, WESTERN_THEME_ID);
        insertReservationWithFixedId(JASON_RESERVATION_ID, "제이슨", JASON_RESERVATION_DATE, MORNING_TIME_ID, SECRET_GARDEN_THEME_ID);
    }

    public String insertReservation(String name, LocalDate date, EntityId timeId, EntityId themeId) {
        return insertReservation(name, date, timeId.getValueAsString(), themeId.getValueAsString());
    }

    public String insertReservation(String name, LocalDate date, String timeId, String themeId) {
        String reservationId = UUID.randomUUID().toString();
        return insertReservationWithFixedId(EntityId.fromUuid(UUID.fromString(reservationId)), name, date, timeId, themeId);
    }

    public String insertReservationWithFixedId(EntityId reservationId, String name, LocalDate date, EntityId timeId, EntityId themeId) {
        return insertReservationWithFixedId(reservationId, name, date, timeId.getValueAsString(), themeId.getValueAsString());
    }

    public String insertReservationWithFixedId(EntityId reservationId, String name, LocalDate date, String timeId, String themeId) {
        String id = reservationId.getValueAsString();
        jdbcTemplate.update(
                "INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (?, ?, ?, ?, ?)",
                UUID.fromString(id),
                name,
                date,
                UUID.fromString(timeId),
                UUID.fromString(themeId)
        );
        return id;
    }

    public void insertTime(EntityId timeId, LocalTime startAt) {
        jdbcTemplate.update(
                "INSERT INTO reservation_time (id, start_at) VALUES (?, ?)",
                UUID.fromString(timeId.getValueAsString()),
                startAt
        );
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
