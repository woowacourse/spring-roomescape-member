package roomescape;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.RoomTheme;

public class TestFixture {
    public static String MEMBER_NAME_FIXTURE = "명오";
    public static final String EMAIL_FIXTURE = "hkim1109@naver.com";
    public static final String PASSWORD_FIXTURE = "qwer1234";
    public static LocalDate DATE_FIXTURE = LocalDate.of(9999, 12, 31);
    public static LocalTime TIME_FIXTURE = LocalTime.of(10, 0);
    public static final String THEME_NAME_FIXTURE = "레벨 2 탈출";
    public static final String THEME_DESCRIPTION_FIXTURE = "우테코 레벨2를 탈출하는 내용입니다.";
    public static final String THEME_THUMBNAIL_FIXTURE = "https://i.pinimg"
            + ".com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

    public static Member MEMBER_FIXTURE = new Member(EMAIL_FIXTURE, PASSWORD_FIXTURE);
    public static ReservationTime RESERVATION_TIME_FIXTURE = new ReservationTime(TIME_FIXTURE);
    public static RoomTheme ROOM_THEME_FIXTURE = new RoomTheme(THEME_NAME_FIXTURE,
            THEME_DESCRIPTION_FIXTURE, THEME_THUMBNAIL_FIXTURE);

    public static SqlParameterSource MEMBER_PARAMETER_SOURCE = new MapSqlParameterSource()
            .addValue("name", MEMBER_NAME_FIXTURE)
            .addValue("role", Role.NORMAL.name())
            .addValue("email", EMAIL_FIXTURE)
            .addValue("password", PASSWORD_FIXTURE);
    public static SqlParameterSource ADMIN_PARAMETER_SOURCE = new MapSqlParameterSource()
            .addValue("name", MEMBER_NAME_FIXTURE)
            .addValue("role", Role.ADMIN.name())
            .addValue("email", EMAIL_FIXTURE)
            .addValue("password", PASSWORD_FIXTURE);
    public static SqlParameterSource RESERVATION_TIME_PARAMETER_SOURCE = new MapSqlParameterSource()
            .addValue("start_at", Time.valueOf(TIME_FIXTURE));
    public static SqlParameterSource ROOM_THEME_PARAMETER_SOURCE = new MapSqlParameterSource()
            .addValue("name", THEME_NAME_FIXTURE)
            .addValue("description", THEME_DESCRIPTION_FIXTURE)
            .addValue("thumbnail", THEME_THUMBNAIL_FIXTURE);


    public static long createMember(JdbcTemplate jdbcTemplate, SqlParameterSource memberSource) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(memberSource)
                .longValue();
    }

    public static long createReservationTime(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation_time")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(RESERVATION_TIME_PARAMETER_SOURCE)
                .longValue();
    }

    public static long createTheme(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(ROOM_THEME_PARAMETER_SOURCE)
                .longValue();
    }
}
