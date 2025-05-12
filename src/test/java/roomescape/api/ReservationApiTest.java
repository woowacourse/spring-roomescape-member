package roomescape.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
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
import roomescape.infrastructure.JwtTokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationApiTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationTimeRepository timeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Test
    void 사용자가_예약을_추가한다() {
        Member savedMember = memberRepository.save(
                new Member(null, "name1", "email1@domain.com", "password1", Role.MEMBER)
        );
        String token = tokenProvider.createToken(savedMember.getId().toString(), savedMember.getRole());
        ReservationTime time = timeRepository.save(ReservationTime.createWithoutId(LocalTime.of(9, 0)));
        Theme theme = themeRepository.save(Theme.createWithoutId("theme1", "desc", "thumb1"));
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", "2026-08-05");
        reservation.put("timeId", time.getId());
        reservation.put("themeId", theme.getId());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .cookie("token", token)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(201)
                .body("date", equalTo("2026-08-05"))
                .body("memberName", equalTo("name1"));

    }

    @Test
    void 예약을_삭제한다() {
        // given
        Member member = memberRepository.save(
                new Member(null, "name1", "email1@domain.com", "password1", Role.MEMBER)
        );
        ReservationTime time = timeRepository.save(ReservationTime.createWithoutId(LocalTime.of(9, 0)));
        Theme theme = themeRepository.save(Theme.createWithoutId("theme1", "desc", "thumb1"));
        Reservation reservation = reservationRepository.save(
                Reservation.createWithoutId(member, LocalDate.of(2025, 1, 1), time, theme));
        // when & then
        RestAssured.given().log().all()
                .when().delete("/api/reservations/{reservationId}", reservation.getId())
                .then().log().all()
                .statusCode(204);

        assertThat(reservationRepository.findById(reservation.getId())).isEmpty();
    }
}
