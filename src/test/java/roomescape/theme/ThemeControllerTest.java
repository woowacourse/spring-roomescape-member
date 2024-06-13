package roomescape.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.theme.response.RankTheme;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("저장된 모든 테마를 조회하고 상태코드 200을 응답한다.")
    void findAll() {
        int count = count();

        List<Theme> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Theme.class);

        assertThat(themes.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("테마를 추가하고 상태코드 201을 응답한다.")
    void create() {
        int count = count();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new Theme(0L, "구구", "테마 설명2", "thumbnail2.jpg"))
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        assertThat(count()).isEqualTo(count + 1);

    }

    @Test
    @DisplayName("저장된 테마를 삭제하고 상태코드 204를 응답한다.")
    void delete() {
        int count = count();

        RestAssured.given().log().all()
                .when().delete("/themes/" + 3)
                .then().log().all()
                .statusCode(204);

        assertThat(count()).isEqualTo(count - 1);

    }

    @Test
    @DisplayName("최근 일주일 인기 테마 목록을 확인한다")
    void getRank() {
        initializeForRank();

        List<RankTheme> rankThemes = RestAssured.given().log().all()
                .when().get("/themes/rank")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", RankTheme.class);

        List<RankTheme> target = List.of(new RankTheme("Theme1", "Description for Theme1", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new RankTheme("Theme3", "Description for Theme3", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        assertThat(rankThemes).usingRecursiveComparison().isEqualTo(target);
    }

    void insertReservation(long memberId, LocalDate date, long timeId, long themeId) {
        Member member = memberDao.findMemberById(memberId).get();
        ReservationTime reservationTime = reservationTimeDao.findById(timeId).get();
        Theme theme = themeDao.findById(themeId).get();

        reservationDao.save(new Reservation(member, date, reservationTime, theme));
    }

    void initializeForRank() {
        insertReservation(1,
                LocalDate.now().minusDays(1),
                1,
                1);
        insertReservation(1,
                LocalDate.now().minusDays(6),
                1,
                3);
    }

    int count() {
        return themeDao.findAll().size();
    }
}
