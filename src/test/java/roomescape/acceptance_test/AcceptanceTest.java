package roomescape.acceptance_test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.dto.ThemeCreateRequest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("테마 생성 후 목록에서 조회된다.")
    public void test1() throws JsonProcessingException {
        ThemeCreateRequest request = new ThemeCreateRequest("brown", "설명", "섬네일");
        Integer themeId =
                given().log().all()
                    .contentType(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(request))
                .when()
                    .post("/admin/themes")
                .then().log().all()
                    .statusCode(201)
                    .body("name", equalTo("brown"))
                    .body("description", equalTo("설명"))
                    .body("thumbnail", equalTo("섬네일"))
                    .extract().path("id");


        given().log().all()
        .when()
            .get("/themes")
        .then().log().all()
            .statusCode(200)
            .body("themes", hasSize(1))
            .body("themes[0].id", equalTo(themeId))
            .body("themes[0].name", equalTo("brown"))
            .body("themes[0].description", equalTo("설명"))
            .body("themes[0].thumbnail", equalTo("섬네일"));
    }

    /**
     * TODO: 인수테스트 시나리오 추가 구현
     * ### 인수테스트 시나리오
     * 1. 테마 생성 후 목록에서 조회된다 x
     * POST /admin/themes
     * GET /themes
     * 생성한 테마가 응답에 포함되는지 검증
     *
     * 2. 테마 생성 후 삭제하면 목록에서 사라진다
     * POST /admin/themes
     * DELETE /admin/themes/{id}
     * GET /themes
     * 삭제한 테마가 없는지 검증
     *
     * 3. 예약 시간 생성 후 목록에서 조회된다
     * POST /admin/times
     * GET /times
     * 생성한 시간이 응답에 포함되는지 검증
     *
     * 4. 중복된 예약 시간을 생성하면 400 에러가 발생한다
     * POST /admin/times로 10:00 생성
     * 다시 POST /admin/times로 10:00 생성
     * 400 Bad Request 검증
     *
     * 5. 테마와 예약 시간을 만든 뒤 예약을 생성한다
     * POST /admin/themes
     * POST /admin/times
     * POST /reservations
     * 201 Created
     * 응답에 예약자 이름, 날짜, 시간, 테마 정보가 맞는지 검증
     *
     * 6. 예약 생성 후 관리자 예약 목록에서 조회된다
     * 테마 생성
     * 예약 시간 생성
     * 예약 생성
     * GET /admin/reservations
     * 생성한 예약이 목록에 포함되는지 검증
     *
     * 7. 예약 삭제 후 관리자 예약 목록에서 사라진다
     * 예약 생성
     * DELETE /admin/reservations/{id}
     * GET /admin/reservations
     * 삭제한 예약이 없는지 검증
     *
     * 8. 예약된 시간은 예약 가능 시간 목록에서 unavailable로 내려온다
     * 테마 생성
     * 예약 시간 2개 생성
     * 특정 날짜에 특정 시간으로 예약 생성
     * GET /times/availability?date=2023-08-05&themeId={themeId}
     * 예약된 시간은 isAvailable=false
     * 예약 안 된 시간은 isAvailable=true
     *
     * 9. 인기 테마는 기간 내 예약 수가 많은 순서대로 조회된다
     * 테마 여러 개 생성
     * 예약 시간 여러 개 생성
     * 기간 내 예약 수가 다르게 예약 생성
     * 기간 밖 예약도 함께 생성
     * GET /themes/popularity?days=7&size=3
     * 기간 내 예약 수가 많은 테마 순서대로 조회되는지 검증
     */


}
