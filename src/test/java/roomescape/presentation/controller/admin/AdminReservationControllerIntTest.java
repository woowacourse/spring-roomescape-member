package roomescape.presentation.controller.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static roomescape.testFixture.Fixture.MEMBER1;
import static roomescape.testFixture.Fixture.MEMBER2;
import static roomescape.testFixture.Fixture.createAdminReservationCreateDto;
import static roomescape.testFixture.Fixture.resetH2TableIds;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.application.dto.MemberDto;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.testFixture.JdbcHelper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminReservationControllerIntTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String tokenForAdmin;
    private String tokenForUser;

    @BeforeEach
    void cleanDatabase() {
        RestAssured.port = port;
        resetH2TableIds(jdbcTemplate);

        tokenForAdmin = jwtTokenProvider.createToken(MemberDto.from(MEMBER1));
        tokenForUser = jwtTokenProvider.createToken(MemberDto.from(MEMBER2));
    }

    @DisplayName("/admin/reservations 요청 시 201 CREATED")
    @Test
    public void request_addReservation() {
        JdbcHelper.insertMember(jdbcTemplate, MEMBER1);
        JdbcHelper.insertMember(jdbcTemplate, MEMBER2);
        JdbcHelper.insertTheme(jdbcTemplate, Theme.withoutId("테마1", "테마 1입니다.", "썸네일입니다."));
        JdbcHelper.insertReservationTime(jdbcTemplate, ReservationTime.of(1L, LocalTime.of(10, 0)));

        int repositorySize = reservationRepository.findAll().size();
        int expectedSize = repositorySize + 1;

        RestAssured.given().log().all()
                .cookie("token", tokenForAdmin)
                .contentType(ContentType.JSON)
                .body(createAdminReservationCreateDto(2L))
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(expectedSize));

        int afterAddSize = reservationRepository.findAll().size();
        assertThat(afterAddSize).isEqualTo(expectedSize);
    }

    @DisplayName("관리자가 아닌 일반 유저가 /admin/reservations 요청 시 401 Unauthorized")
    @Test
    public void request_addReservation_unauthorized() {
        JdbcHelper.insertMember(jdbcTemplate, MEMBER2);
        JdbcHelper.insertTheme(jdbcTemplate, Theme.withoutId("테마1", "테마 1입니다.", "썸네일입니다."));
        JdbcHelper.insertReservationTime(jdbcTemplate, ReservationTime.of(1L, LocalTime.of(10, 0)));

        int repositorySize = reservationRepository.findAll().size();
        int expectedSize = repositorySize + 1;

        RestAssured.given().log().all()
                .cookie("token", tokenForUser)
                .contentType(ContentType.JSON)
                .body(createAdminReservationCreateDto(2L))
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(401)
                .body("id", is(expectedSize));

        int afterAddSize = reservationRepository.findAll().size();
        assertThat(afterAddSize).isEqualTo(expectedSize);
    }
}
