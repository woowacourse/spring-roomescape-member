package roomescape.controller;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(statements = {
        "INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00');",
        "INSERT INTO theme (id, name, description, thumbnail) VALUES " +
                "(1, 'T1', 'd1', 'u1')," +
                "(2, 'T2', 'd2', 'u2')," +
                "(3, 'T3', 'd3', 'u3');",
        "INSERT INTO member (id, role, name, email, password) VALUES " +
                "(1, 'USER', 'U1', 'u1@example.com', 'pw')," +
                "(2, 'USER', 'U2', 'u2@example.com', 'pw')," +
                "(3, 'USER', 'U3', 'u3@example.com', 'pw');",
        "INSERT INTO reservation (id, date, member_id, time_id, theme_id) VALUES " +
                "(1, FORMATDATETIME(DATEADD('DAY', -1, CURRENT_DATE), 'yyyy-MM-dd'), 1, 1, 1)," +
                "(2, FORMATDATETIME(DATEADD('DAY', -2, CURRENT_DATE), 'yyyy-MM-dd'), 2, 1, 1)," +
                "(3, FORMATDATETIME(DATEADD('DAY', -3, CURRENT_DATE), 'yyyy-MM-dd'), 3, 1, 2);"
})
public class PopularThemeControllerTest {

    @Test
    @DisplayName("인기 테마 조회 요청은 정렬된 인기 테마 리스트를 반환한다")
    void getWeeklyBestThemes() {
        ApiTestHelper.get("/themes?sort=best&period=weekly", "")
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[1].id", is(2));
    }
}
