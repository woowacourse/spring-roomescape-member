package roomescape.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.Role;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeApiTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 예약시간_추가_테스트() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        // when
        ReservationTimeResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("api/times")
                .then().log().all()
                .statusCode(201)
                .extract().as(ReservationTimeResponse.class);

        // then
        List<ReservationTime> allReservationTimes = reservationTimeRepository.findAll();
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0));
        soft.assertThat(allReservationTimes).hasSize(1);
        soft.assertAll();
    }

    @Test
    void 예약시간_조회_테스트() {
        // given
        reservationTimeRepository.save(ReservationTime.createWithoutId(LocalTime.of(10, 0)));
        // when
        List<ReservationTimeResponse> response = RestAssured.given().log().all()
                .when().get("/api/times")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", ReservationTimeResponse.class);
        // then
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(response).hasSize(1);
        soft.assertThat(response.getFirst().startAt()).isEqualTo(LocalTime.of(10, 0));
        soft.assertAll();

    }

    @Test
    void 예약시간_삭제_테스트() {
        // given
        reservationTimeRepository.save(ReservationTime.createWithoutId(LocalTime.of(10, 0)));
        // when
        RestAssured.given().log().all()
                .when().delete("/api/times/{timeId}", 1L)
                .then().log().all()
                .statusCode(204);

        // then
        List<ReservationTime> allReservationTimes = reservationTimeRepository.findAll();
        assertThat(allReservationTimes).hasSize(0);
    }

    @Test
    void 가능한_예약시간_조회_테스트() {
        // when
        ReservationTime reservationTime1 = reservationTimeRepository.save(
                ReservationTime.createWithoutId(LocalTime.of(10, 0))
        );
        ReservationTime reservationTime2 = reservationTimeRepository.save(
                ReservationTime.createWithoutId(LocalTime.of(11, 0))
        );
        Theme theme = themeRepository.save(Theme.createWithoutId("theme1", "desc", "thumb"));
        Member member = memberRepository.save(
                new Member(null, "member1", "email1@domain.com", "password1", Role.MEMBER));
        reservationRepository.save(
                Reservation.createWithoutId(member, LocalDate.of(2025, 1, 1), reservationTime1, theme));
        // when
        List<AvailableTimeResponse> response = RestAssured.given().log().all()
                .when().get("/api/times/theme/1?date=2025-01-01")
                .then().log().all()
                .statusCode(200)
                .extract().jsonPath().getList(".", AvailableTimeResponse.class);
        // then
        SoftAssertions soft = new SoftAssertions();
        soft.assertThat(response).hasSize(2);
        soft.assertThat(response.get(0).alreadyBooked()).isTrue();
        soft.assertThat(response.get(1).alreadyBooked()).isFalse();
        soft.assertAll();
    }
}
