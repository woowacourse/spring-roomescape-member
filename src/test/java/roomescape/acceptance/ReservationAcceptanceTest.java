package roomescape.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.support.SimpleRestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationAcceptanceTest {
    private static final String PATH = "/reservations";
    private static final ReservationRequest REQUEST = new ReservationRequest(
            LocalDate.now().plusDays(2),
            1L,
            1L
    );
//    private static final Map<String, Object> BODY = Map.of(
//            "date", LocalDate.now().plusDays(2).toString(),
//            "memberId", 1L,
//            "timeId", 1L,
//            "themeId", 1L
//    );

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("[2단계 - 예약 조회]")
    @TestFactory
    @Sql(scripts = {"/clear.sql", "/init.sql"})
    DynamicNode step2() {
        return dynamicTest("예약을 조회한다.", () -> {
            RestAssured.given().log().all()
                    .cookie(makeToken().toString())
                    .when().get(PATH)
                    .then().statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("size()", is(0));
        });
    }

    @DisplayName("[3단계 - 예약 추가 / 취소]")
    @TestFactory
    List<DynamicTest> step3() {
        return Arrays.asList(
                dynamicTest("예약을 등록한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .cookie(makeToken())
                            .body(REQUEST)
                            .when().post(PATH)
                            .then()
                            .contentType(ContentType.JSON)
                            .body("id", is(1))
                            .statusCode(201);
                }),
                dynamicTest("등록 후 예약을 모두 조회한다.", () -> {
                    RestAssured.given()
                            .cookie(makeToken())
                            .when().get(PATH)
                            .then().statusCode(200)
                            .contentType(ContentType.JSON)
                            .body("size()", is(1));
                }),
                dynamicTest("등록된 예약을 삭제한다.", () -> {
                    RestAssured.given().log().all()
                            .cookie(makeToken().toString())
                            .contentType(ContentType.JSON)
                            .when().delete(PATH + "/1")
                            .then().statusCode(204);
                }),
                dynamicTest("삭제 후 예약을 모두 조회한다.", () -> {
                    RestAssured.given().log().all()
                            .cookie(makeToken().toString())
                            .contentType(ContentType.JSON)
                            .when().get(PATH)
                            .then().statusCode(200)
                            .body("size()", is(0));
                })
        );
    }

    @DisplayName("[5단계 - 데이터 조회하기] 데이터 삽입 후 조회 API와 쿼리 결과를 비교한다")
    @Test
    void step5() {
        LocalDate date = LocalDate.now().plusDays(2);
        jdbcTemplate.update(
                "INSERT INTO reservation (reservation_date, member_id, time_id, theme_id) VALUES (?, ?, ?, ?)",
                date, 1L, 1L, 1L);

        List<ReservationResponse> reservations = SimpleRestAssured.get(PATH)
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponse.class);

        Integer count = executeCountQuery();
        assertThat(reservations).hasSize(count);
    }

    @DisplayName("[6단계 - 데이터 추가 / 삭제하기]")
    @TestFactory
    List<DynamicTest> step6() {
        return Arrays.asList(
                dynamicTest("예약 등록 후 쿼리로 개수를 조회한다", () -> {
                    SimpleRestAssured.post(PATH, REQUEST)
                            .statusCode(201)
                            .header("Location", "/reservations/1");
                    Integer count = executeCountQuery();
                    assertThat(count).isEqualTo(1);
                }),
                dynamicTest("예약 삭제 후 쿼리로 개수를 조회한다", () -> {
                    SimpleRestAssured.delete(PATH + "/1")
                            .statusCode(204);
                    Integer countAfterDelete = executeCountQuery();
                    assertThat(countAfterDelete).isZero();
                })
        );
    }

    @DisplayName("[8단계 - 예약과 시간 관리]")
    @TestFactory
    List<DynamicTest> step8() {
        return Arrays.asList(
                dynamicTest("예약을 등록한다.", () -> {
                    SimpleRestAssured.post(PATH, REQUEST)
                            .statusCode(201);
                }),
                dynamicTest("등록 후 모든 예약을 조회한다", () -> {
                    SimpleRestAssured.get(PATH)
                            .statusCode(200)
                            .body("size()", is(1));
                })
        );
    }

    private Integer executeCountQuery() {
        return jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
    }

    private String makeToken() {
        Member member = new Member(1L, new MemberName("레모네"), "lemone@gmail.com", "lemon12", MemberRole.ADMIN);
        String accessToken = jwtTokenProvider.createToken(member);
        return "token=" + accessToken;
    }
}
