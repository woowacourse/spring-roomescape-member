package roomescape.controller.api.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.LoginUtils;
import roomescape.domain.Reservation;
import roomescape.repository.member.MemberRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static roomescape.InitialDataFixture.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private ReservationController reservationController;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    private String token;

    @BeforeEach
    void beforeEach() {
        memberRepository.save(USER_1);
        themeRepository.save(THEME_1);
        reservationTimeRepository.save(RESERVATION_TIME_1);
        token = LoginUtils.loginAndGetToken(USER_1);
    }

    @AfterEach
    void afterEach() {
        LoginUtils.logout(USER_1);
    }

    @Test
    @DisplayName("ReservationController는 JdbcTemplate를 필드로 가지지 않는다.")
    void doesNotContainJdbcTemplate() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }

    @Test
    @DisplayName("저장된 reservation을 모두 반환한다.")
    void getReservations() {
        reservationRepository.save(RESERVATION_1);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("Reservation을 추가한다.")
    void addReservation() {
        //given
        LocalDate localDate = LocalDate.now().plusDays(1);
        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("date", localDate.toString());
        reservationParams.put("timeId", "1");
        reservationParams.put("themeId", "1");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .header("Location", "/reservations/1");
    }

    @Test
    @DisplayName("Reservation을 삭제한다.")
    void deleteReservation() {
        Reservation saved = reservationRepository.save(RESERVATION_1);
        int size = reservationRepository.findAll().size();
        System.out.println("reservationRepository.findAll() = " + reservationRepository.findAll());
        System.out.println("size = " + size);

        RestAssured.given().log().all()
                .when().delete("/reservations/" + saved.getId())
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(size - 1));
    }
}
