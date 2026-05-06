package roomescape.controller;

import static org.hamcrest.Matchers.is;

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
    void 인기_테마를_조회한다() {
        LocalDate endDate = LocalDate.of(2026, 5, 5);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/themes?endDate=" + endDate)
                .then().log().all()
                .statusCode(200)
                .body("[0].name", is("공포의 저택"));
    }
}
