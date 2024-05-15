package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.Member;
import roomescape.dto.request.UserReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.SelectableTimeResponse;
import roomescape.infrastructure.auth.Token;
import roomescape.infrastructure.auth.TokenManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.Fixture.DAY_BEFORE_YESTERDAY;
import static roomescape.Fixture.YESTERDAY;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
@Sql("/truncate.sql")
@Sql("/testdata.sql")
class ReservationControllerIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TokenManager tokenManager;

    @BeforeEach
    void setUp() {
        String sql = "insert into reservation(date, time_id, theme_id, member_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, DAY_BEFORE_YESTERDAY, 1, 1, 1);
        jdbcTemplate.update(sql, YESTERDAY, 1, 1, 1);
        jdbcTemplate.update(sql, YESTERDAY, 1, 2, 1);
    }

    @Test
    @DisplayName("새로운 예약을 저장할 수 있다")
    void should_SaveNewUserReservation() {
        //given
        Member member = new Member(1L, "coli1", "a@a.com", "userpassword", "USER");
        Token token = tokenManager.generate(member);

        UserReservationRequest reservationRequest = new UserReservationRequest(LocalDate.now().plusDays(1), 1L, 1L);
        Map<String, Object> request = new HashMap<>();
        request.put("date", reservationRequest.date().toString());
        request.put("themeId", reservationRequest.themeId());
        request.put("timeId", reservationRequest.timeId());


        //when
        ReservationResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token.getToken())
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getObject(".", ReservationResponse.class);

        //then
        assertAll(
                () -> assertThat(response.date()).isEqualTo(reservationRequest.date()),
                () -> assertThat(response.time().id()).isEqualTo(reservationRequest.timeId()),
                () -> assertThat(response.theme().id()).isEqualTo(reservationRequest.themeId())
        );
    }

    @Test
    @DisplayName("예약 전체를 조회할 수 있다")
    void should_GetAllReservations() {
        //when
        List<ReservationResponse> responses = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReservationResponse.class);

        assertThat(responses).hasSize(3);
    }

    @Test
    @DisplayName("특정 날짜, 테마 아이디에 예약가능시간을 반환한다")
    void should_FindSelectableTimes_When_GiveDate_And_ThemeId() {
        //given - 어제 / 1번 테마  -> timeId1 : 사용불가 / timeId2: 사용 가능
        LocalDate yesterday = LocalDate.now().minusDays(1L);
        long findThemeId = 1L;
        String path = "/reservations/times?date=" + yesterday + "&themeId=" + findThemeId;

        //when
        List<SelectableTimeResponse> responses = RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", SelectableTimeResponse.class);

        //then
        assertAll(
                () -> assertThat(responses).hasSize(2),
                () -> assertThat(responses.get(0).alreadyBooked()).isTrue(),
                () -> assertThat(responses.get(1).alreadyBooked()).isFalse()
        );
    }

    @Test
    @DisplayName("특정 id의 예약을 삭제할 수 있다")
    void should_DeleteReservation_When_Give_ReservationId() {
        //given
        String sql = "SELECT count(*) FROM reservation";
        int firstCount = jdbcTemplate.queryForObject(sql, Integer.class);

        //when
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        //then
        int secondCount = jdbcTemplate.queryForObject(sql, Integer.class);
        assertThat(firstCount).isEqualTo(secondCount + 1);
    }
}