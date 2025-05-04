package roomescape.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import roomescape.TestRepositoryConfig;
import roomescape.business.ReservationTheme;
import roomescape.exception.ErrorResponseDto;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.presentation.dto.ReservationThemeResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@ActiveProfiles("test")
@Import(TestRepositoryConfig.class)
class ReservationThemeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FakeReservationThemeRepository reservationThemeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        reservationThemeRepository.clear();
    }

    @DisplayName("예약 가능한 테마 목록을 조회하면 상태 코드와 함께 테마 목록이 반환된다.")
    @Test
    void getReservationThemes() {
        // given
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        reservationThemeRepository.add(new ReservationTheme("테마2", "설명2", "테마2.jpg"));

        // when
        List<ReservationThemeResponseDto> response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/themes")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReservationThemeResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response.get(0).name()).isEqualTo("테마1"),
                () -> assertThat(response.get(1).name()).isEqualTo("테마2"),
                () -> assertThat(response.get(0).description()).isEqualTo("설명1"),
                () -> assertThat(response.get(1).description()).isEqualTo("설명2"),
                () -> assertThat(response.get(0).thumbnail()).isEqualTo("테마1.jpg"),
                () -> assertThat(response.get(1).thumbnail()).isEqualTo("테마2.jpg")
        );
    }

    @DisplayName("예약 가능한 테마를 생성하면 상태 코드와 함께 생성된 테마가 반환된다.")
    @Test
    void createReservationTheme() {
        // given
        ReservationTheme reservationTheme = new ReservationTheme("테마1", "설명1", "테마1.jpg");

        // when
        ReservationThemeResponseDto response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(reservationTheme)
                .when()
                .post("/themes")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(ReservationThemeResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.id()).isNotNull(),
                () -> assertThat(response.name()).isEqualTo("테마1"),
                () -> assertThat(response.description()).isEqualTo("설명1"),
                () -> assertThat(response.thumbnail()).isEqualTo("테마1.jpg")
        );
    }

    @DisplayName("예약 가능한 테마를 생성할 때 중복된 이름이 있으면 예외가 발생한다.")
    @Test
    void createReservationThemeWithDuplicateName() {
        // given
        ReservationTheme reservationTheme1 = new ReservationTheme("테마1", "설명1", "테마1.jpg");
        reservationThemeRepository.add(reservationTheme1);
        ReservationTheme reservationTheme2 = new ReservationTheme("테마1", "설명2", "테마2.jpg");

        // when
        ErrorResponseDto errorResponseDto = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(reservationTheme2)
                .when()
                .post("/themes")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDto.class);

        // then
        assertThat(errorResponseDto.message())
                .isEqualTo("동일한 이름의 테마를 추가할 수 없습니다.");
    }

    @DisplayName("예약 가능한 테마를 삭제하면 상태 코드 204가 반환된다.")
    @Test
    void deleteReservationTheme() {
        // given
        ReservationTheme reservationTheme = new ReservationTheme("테마1", "설명1", "테마1.jpg");
        Long id = reservationThemeRepository.add(reservationTheme);

        // when
        RestAssured
                .given()
                .log().all()
                .when()
                .delete("/themes/" + id)
                .then()
                .log().all()
                .statusCode(204);

        // then
        assertThat(reservationThemeRepository.findById(id)).isEmpty();
    }
}
