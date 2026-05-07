package roomescape.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Test
    @Sql(scripts = {"/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void 최근_7일간_인기_테마_상위_10개를_조회한다() {
        LocalDate baseDate = LocalDate.of(2026, 5, 6);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes?baseDate=" + baseDate)
                .then().log().all()
                .statusCode(200)
                .body("name", contains(
                        "공포의 저택",
                        "사라진 연구소",
                        "시간 여행자",
                        "감옥 탈출",
                        "마법사의 방",
                        "좀비 바이러스",
                        "해적의 보물",
                        "스파이 미션",
                        "우주 정거장",
                        "고대 유적"
                ))
                .body("name", not(hasItem("지하 벙커")));
    }
}
