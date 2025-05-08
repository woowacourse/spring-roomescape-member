package roomescape.integrate.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.ThemeResponse;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationIntegrateTest {

    private final String EMAIL = "test@test.com";
    private final String PASSWORD = "pwd";

    private String token;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);

        token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }

    @Test
    void 예약_추가_테스트() {
        LocalTime afterTime = LocalTime.now().plusHours(1L);
        Map<String, String> timeParam = Map.of(
                "startAt", afterTime.toString()
        );

        Map<String, String> themeParam = Map.of(
                "name", "테마 명",
                "description", "description",
                "thumbnail", "thumbnail"
        );

        long timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParam)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        long themeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(themeParam)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        Map<String, Object> reservation = Map.of(
                "name", "브라운",
                "date", LocalDate.now().plusDays(1).toString(),
                "timeId", timeId,
                "themeId", themeId
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 예약_삭제_테스트() {
        LocalTime afterTime = LocalTime.now().plusHours(1L);
        Map<String, String> timeParam = Map.of(
                "startAt", afterTime.toString()
        );

        Map<String, String> themeParam = Map.of(
                "name", "테마 명",
                "description", "description",
                "thumbnail", "thumbnail"
        );

        long timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParam)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        long themeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(themeParam)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        Map<String, Object> reservation = Map.of(
                "name", "브라운",
                "date", LocalDate.now().plusDays(1).toString(),
                "timeId", timeId,
                "themeId", themeId
        );

        long reservationId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 테마_랭킹_테스트(@Autowired ReservationRepository reservationRepository) {
        LocalTime afterTime = LocalTime.now().plusHours(1L);
        Map<String, String> timeParam = Map.of(
                "startAt", afterTime.toString()
        );

        Map<String, String> themeParam = Map.of(
                "name", "테마 명1",
                "description", "description",
                "thumbnail", "thumbnail"
        );

        Map<String, String> themeParam2 = Map.of(
                "name", "테마 명2",
                "description", "description",
                "thumbnail", "thumbnail"
        );

        Map<String, String> themeParam3 = Map.of(
                "name", "테마 명3",
                "description", "description",
                "thumbnail", "thumbnail"
        );

        long timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParam)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        long themeId_1 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParam)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        long themeId_2 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParam2)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        long themeId_3 = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParam3)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        // TODO: token을 이용한 member로 변경
        Member member = memberRepository.findByEmailAndPassword(EMAIL, PASSWORD)
                .orElse(null);

        Reservation reservation1 = new Reservation(member, LocalDate.now().minusDays(1),
                new ReservationTime(timeId, afterTime), new Theme(themeId_1, "테마 명1", "description", "thumbnail"));
        Reservation reservation2 = new Reservation(member, LocalDate.now().minusDays(2),
                new ReservationTime(timeId, afterTime), new Theme(themeId_1, "테마 명1", "description", "thumbnail"));
        Reservation reservation3 = new Reservation(member, LocalDate.now().minusDays(3),
                new ReservationTime(timeId, afterTime), new Theme(themeId_1, "테마 명1", "description", "thumbnail"));
        Reservation reservation4 = new Reservation(member, LocalDate.now().minusDays(4),
                new ReservationTime(timeId, afterTime), new Theme(themeId_2, "테마 명2", "description", "thumbnail"));
        Reservation reservation5 = new Reservation(member, LocalDate.now().minusDays(5),
                new ReservationTime(timeId, afterTime), new Theme(themeId_2, "테마 명2", "description", "thumbnail"));
        Reservation reservation6 = new Reservation(member, LocalDate.now().minusDays(6),
                new ReservationTime(timeId, afterTime), new Theme(themeId_3, "테마 명3", "description", "thumbnail"));

        reservationRepository.add(reservation1);
        reservationRepository.add(reservation2);
        reservationRepository.add(reservation3);
        reservationRepository.add(reservation4);
        reservationRepository.add(reservation5);
        reservationRepository.add(reservation6);

        Response response = RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .extract().response();

        List<ThemeResponse> rankingThemes = response.jsonPath().getList("", ThemeResponse.class);
        List<Long> rankingThemeIds = rankingThemes.stream()
                .map(ThemeResponse::id)
                .toList();

        assertThat(rankingThemeIds).containsExactlyElementsOf(List.of(themeId_1, themeId_2, themeId_3));
    }
}
