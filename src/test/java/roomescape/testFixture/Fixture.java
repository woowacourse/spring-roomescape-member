package roomescape.testFixture;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public class Fixture {
    public static final Theme THEME_1 = Theme.of(1L, "테마1", "테마 1입니다.", "썸네일1");
    public static final ReservationTime RESERVATION_TIME_1 = ReservationTime.of(1L, LocalTime.of(10, 0));
    public static final ReservationTime RESERVATION_TIME_2 = ReservationTime.of(2L, LocalTime.of(11, 0));
    public static final ReservationTime RESERVATION_TIME_3 = ReservationTime.of(3L, LocalTime.of(12, 0));
    public static final Member MEMBER1 = Member.of(1L, "어드민", "admin@email.com", "password");
    public static final Member MEMBER2 = Member.of(2L, "브라운", "brown@email.com", "brown");
    public static final Member MEMBER3 = Member.of(3L, "브리", "brie@email.com", "brie");
    public static final Member MEMBER4 = Member.of(4L, "솔라", "solar@email.com", "solar");
    public static final Reservation RESERVATION_1 =
            Reservation.of(1L, MEMBER1, THEME_1, LocalDate.now().plusDays(1), RESERVATION_TIME_1);

    public static final Map<String, Object> RESERVATION_BODY = createReservationBody();

    private static Map<String, Object> createReservationBody() {
        String date = LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Map<String, Object> params = new HashMap<>();
        params.put("memberId", 1);
        params.put("date", date);
        params.put("timeId", 1);
        params.put("themeId", 1);

        return params;
    }

    public static void resetH2TableIds(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute((Connection connection) -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET REFERENTIAL_INTEGRITY FALSE");
                statement.execute("TRUNCATE TABLE reservation");
                statement.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
                statement.execute("TRUNCATE TABLE reservation_time");
                statement.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
                statement.execute("TRUNCATE TABLE theme");
                statement.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
                statement.execute("TRUNCATE TABLE member");
                statement.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
                statement.execute("SET REFERENTIAL_INTEGRITY TRUE");
            }
            return null;
        });
    }
}
