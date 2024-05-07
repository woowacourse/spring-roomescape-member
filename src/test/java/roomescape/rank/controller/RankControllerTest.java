package roomescape.rank.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import roomescape.rank.response.RankTheme;
import roomescape.reservation.request.ReservationRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RankControllerTest {

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ReservationService reservationService;

    @BeforeEach
    void initialize() {
        reservationTimeDao.save(new ReservationTime(0, LocalTime.parse("10:30")));
        reservationTimeDao.save(new ReservationTime(0, LocalTime.parse("11:30")));
        themeDao.save(new Theme(0, "theme1", "description1", "thumbnail1"));
        themeDao.save(new Theme(0, "theme2", "description2", "thumbnail2"));
        themeDao.save(new Theme(0, "theme3", "description3", "thumbnail3"));

        reservationService.save(new ReservationRequest("pond"
                , LocalDate.now().minusDays(1), 1, 3));
        reservationService.save(new ReservationRequest("pond"
                , LocalDate.now().minusDays(2), 1, 3));
        reservationService.save(new ReservationRequest("pond"
                , LocalDate.now().minusDays(3), 1, 3));
        reservationService.save(new ReservationRequest("pond"
                , LocalDate.now().minusDays(1), 1, 1));
    }

    @Test
    @DisplayName("최근 일주일 인기 테마 목록을 확인한다")
    void getRank() {

        List<RankTheme> rankThemes = RestAssured.given().log().all()
                .when().get("/rank")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", RankTheme.class);

        List<RankTheme> target = List.of(new RankTheme("theme3", "description3", "thumbnail3"),
                new RankTheme("theme1", "description1", "thumbnail1"));

        assertThat(rankThemes.size()).isEqualTo(2);
        assertThat(rankThemes).usingRecursiveComparison().isEqualTo(target);
    }
}
