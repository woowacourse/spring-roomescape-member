package roomescape.time.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.entity.Member;
import roomescape.auth.entity.Role;
import roomescape.global.infrastructure.JwtTokenProvider;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {
    private final String name = "유저";
    private final Role role = Role.USER;
    private final String email = "user@test.com";
    private final String password = "1234";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("이미 예약이 존재하는 시간은 삭제할 수 없다.")
    @Test
    void deleteTimeExistReservation() {
        // given
        // signup
        Map<String, String> signupParams = Map.of(
                "email", email,
                "password", password,
                "name", name
        );
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signupParams)
                .when()
                .post("/members")
                .then().log().all()
                .statusCode(200);

        final Long userId = jdbcTemplate.queryForObject(
                "SELECT id FROM member WHERE email = ? AND password = ? AND name = ?",
                ((rs, rowNum) -> rs.getLong("id")),
                email, password, name
        );
        Member user = new Member(userId, name, role.name(), email, password);

        // create time
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        // create theme
        Map<String, String> themeParams = Map.of(
                "name", "theme",
                "description", "hi",
                "thumbnail", "hello"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        // create reservation
        LocalDate now = LocalDate.now();
        Map<String, Object> reservationParams = Map.of(
                "name", "test",
                "date", now.plusDays(1).toString(),
                "timeId", 1,
                "themeId", 1
        );

        String userToken = jwtTokenProvider.createToken(user);
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // when & then
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }
}
