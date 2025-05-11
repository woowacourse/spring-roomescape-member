package roomescape.reservation.acceptance;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.entity.Member;
import roomescape.member.entity.RoleType;
import roomescape.member.unit.repository.MemberRepository;
import roomescape.reservation.dto.request.ReservationRequest.ReservationAdminCreateRequest;
import roomescape.reservation.dto.request.ReservationRequest.ReservationCreateRequest;
import roomescape.reservation.dto.request.ReservationRequest.ReservationReadFilteredRequest;
import roomescape.reservation.entity.ReservationTime;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;

// @formatter:off
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationAcceptanceTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("예약을 생성한다.")
    void createReservation() throws Exception {
        // given
        var member = memberRepository.save(new Member(0L, "미소", "miso@email.com", "password", RoleType.USER));
        var theme = themeRepository.save(new Theme(1L, "테마", "설명", "썸네일"));
        var time = reservationTimeRepository.save(new ReservationTime(1L, LocalTime.of(10, 0)));
        var date = LocalDate.of(2024, 3, 20);
        var request = new ReservationCreateRequest(date, time.getId(), theme.getId());

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/reservations")
        .then()
                .statusCode(200)
                .body("date", equalTo(date.toString()))
                .body("time.id", equalTo(time.getId().intValue()))
                .body("theme.id", equalTo(theme.getId().intValue()));
    }

    @Test
    @DisplayName("관리자가 예약을 생성한다.")
    void createReservationByAdmin() throws Exception {
        // given
        var member = memberRepository.save(new Member(0L, "미소", "miso@email.com", "password", RoleType.USER));
        var theme = themeRepository.save(new Theme(1L, "테마", "설명", "썸네일"));
        var time = reservationTimeRepository.save(new ReservationTime(1L, LocalTime.of(10, 0)));
        var date = LocalDate.of(2024, 3, 20);
        var request = new ReservationAdminCreateRequest(date, theme.getId(), time.getId(), member.getId());

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/reservations/admin")
        .then()
                .statusCode(200)
                .body("date", equalTo(date.toString()))
                .body("time.id", equalTo(time.getId().intValue()))
                .body("theme.id", equalTo(theme.getId().intValue()));
    }

    @Test
    @DisplayName("과거 날짜로 예약을 생성하면 예외가 발생한다.")
    void createReservationWithPastDate() throws Exception {
        // given
        var member = memberRepository.save(new Member(0L, "미소", "miso@email.com", "password", RoleType.USER));
        var theme = themeRepository.save(new Theme(1L, "테마", "설명", "썸네일"));
        var time = reservationTimeRepository.save(new ReservationTime(1L, LocalTime.of(10, 0)));
        var date = LocalDate.now().minusDays(1);
        var request = new ReservationCreateRequest(date, time.getId(), theme.getId());

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/reservations")
        .then()
                .statusCode(400)
                .body("message", equalTo("과거 날짜는 예약할 수 없습니다."));
    }

    @Test
    @DisplayName("중복된 시간에 예약을 생성하면 예외가 발생한다.")
    void createReservationWithDuplicateTime() throws Exception {
        // given
        var member = memberRepository.save(new Member(0L, "미소", "miso@email.com", "password", RoleType.USER));
        var theme = themeRepository.save(new Theme(1L, "테마", "설명", "썸네일"));
        var time = reservationTimeRepository.save(new ReservationTime(1L, LocalTime.of(10, 0)));
        var date = LocalDate.of(2024, 3, 20);
        var request = new ReservationCreateRequest(date, time.getId(), theme.getId());

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/reservations")
        .then()
                .statusCode(200);

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/reservations")
        .then()
                .statusCode(409)
                .body("message", equalTo("해당 날짜와 시간에 이미 예약이 존재합니다."));
    }

    @Test
    @DisplayName("모든 예약을 조회한다.")
    void getAllReservations() throws Exception {
        // given
        var member = memberRepository.save(new Member(0L, "미소", "miso@email.com", "password", RoleType.USER));
        var theme = themeRepository.save(new Theme(1L, "테마", "설명", "썸네일"));
        var time = reservationTimeRepository.save(new ReservationTime(1L, LocalTime.of(10, 0)));
        var date = LocalDate.of(2024, 3, 20);
        var request = new ReservationCreateRequest(date, time.getId(), theme.getId());

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/reservations")
        .then()
                .statusCode(200);

        // when & then
        given()
        .when()
                .get("/reservations")
        .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].date", equalTo(date.toString()))
                .body("[0].time.id", equalTo(time.getId().intValue()))
                .body("[0].theme.id", equalTo(theme.getId().intValue()));
    }

    @Test
    @DisplayName("필터링된 예약을 조회한다.")
    void getFilteredReservations() throws Exception {
        // given
        var member = memberRepository.save(new Member(0L, "미소", "miso@email.com", "password", RoleType.USER));
        var theme = themeRepository.save(new Theme(1L, "테마", "설명", "썸네일"));
        var time = reservationTimeRepository.save(new ReservationTime(1L, LocalTime.of(10, 0)));
        var date = LocalDate.of(2024, 3, 20);
        var request = new ReservationCreateRequest(date, time.getId(), theme.getId());

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/reservations")
        .then()
                .statusCode(200);

        var filterRequest = new ReservationReadFilteredRequest(
                theme.getId(),
                member.getId(),
                date,
                date
        );

        // when & then
        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(filterRequest))
        .when()
                .post("/reservations/filtered")
        .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].date", equalTo(date.toString()))
                .body("[0].time.id", equalTo(time.getId().intValue()))
                .body("[0].theme.id", equalTo(theme.getId().intValue()));
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() throws Exception {
        // given
        var member = memberRepository.save(new Member(0L, "미소", "miso@email.com", "password", RoleType.USER));
        var theme = themeRepository.save(new Theme(1L, "테마", "설명", "썸네일"));
        var time = reservationTimeRepository.save(new ReservationTime(1L, LocalTime.of(10, 0)));
        var date = LocalDate.of(2024, 3, 20);
        var request = new ReservationCreateRequest(date, time.getId(), theme.getId());

        given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
        .when()
                .post("/reservations")
        .then()
                .statusCode(200);

        // when & then
        given()
        .when()
                .delete("/reservations/1")
        .then()
                .statusCode(200);

        given()
        .when()
                .get("/reservations")
        .then()
                .statusCode(200)
                .body("$", hasSize(0));
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하면 예외가 발생한다.")
    void deleteNonExistentReservation() {
        // when & then
        given()
        .when()
                .delete("/reservations/1")
        .then()
                .statusCode(404)
                .body("message", equalTo("존재하지 않는 id 입니다."));
    }
}
