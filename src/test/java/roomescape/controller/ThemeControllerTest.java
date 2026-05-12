package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Test
    void 전체_테마를_조회한다() {
        // given
        createTheme("방탈출1", "다함께 탈출해요 방탈출1", "https://example.com/theme1.jpg");
        createTheme("방탈출2", "다함께 탈출해요 방탈출2", "https://example.com/theme2.jpg");

        // when & then
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    void 인기_테마를_조회한다() {
        // given
        setupPopularThemesViaApi();
        LocalDate endDate = LocalDate.of(2026, 5, 5);

        // when & then
        RestAssured.given().log().all()
                .when().get("/themes?endDate=" + endDate)
                .then().log().all()
                .statusCode(200)
                .body("[0].name", is("공포의 저택"));
    }

    private void setupPopularThemesViaApi() {
        int time1 = createTime("10:00");
        int time2 = createTime("11:00");
        int time3 = createTime("12:00");
        int time4 = createTime("13:00");
        int time5 = createTime("14:00");
        int time6 = createTime("15:00");
        int time7 = createTime("16:00");
        int time8 = createTime("17:00");
        int time9 = createTime("18:00");

        int theme1 = createTheme("공포의 저택", "오래된 저택에서 탈출하세요", "https://example.com/theme1.jpg");
        int theme2 = createTheme("사라진 연구소", "비밀 연구소의 진실을 밝혀내세요", "https://example.com/theme2.jpg");
        int theme3 = createTheme("시간 여행자", "시간의 틈에서 탈출하세요", "https://example.com/theme3.jpg");
        int theme4 = createTheme("감옥 탈출", "제한 시간 안에 감옥을 탈출하세요", "https://example.com/theme4.jpg");
        int theme5 = createTheme("마법사의 방", "마법사의 숨겨진 방을 탐험하세요", "https://example.com/theme5.jpg");
        int theme6 = createTheme("좀비 바이러스", "바이러스가 퍼진 도시에서 살아남으세요", "https://example.com/theme6.jpg");
        int theme7 = createTheme("해적의 보물", "해적선에 숨겨진 보물을 찾으세요", "https://example.com/theme7.jpg");
        int theme8 = createTheme("스파이 미션", "비밀 요원이 되어 임무를 완수하세요", "https://example.com/theme8.jpg");
        int theme9 = createTheme("우주 정거장", "고장난 우주 정거장에서 탈출하세요", "https://example.com/theme9.jpg");
        int theme10 = createTheme("고대 유적", "고대 유적의 수수께끼를 풀어보세요", "https://example.com/theme10.jpg");

        String date1 = "2026-05-05";
        String date2 = "2026-05-04";
        String date3 = "2026-05-03";
        String date4 = "2026-05-02";
        String date5 = "2026-05-01";
        String date6 = "2026-04-30";
        String date7 = "2026-04-29";

        // theme1: 12건
        createReservation(date1, time1, theme1);
        createReservation(date1, time2, theme1);
        createReservation(date1, time3, theme1);
        createReservation(date1, time4, theme1);
        createReservation(date1, time5, theme1);
        createReservation(date2, time1, theme1);
        createReservation(date2, time2, theme1);
        createReservation(date3, time3, theme1);
        createReservation(date3, time4, theme1);
        createReservation(date4, time5, theme1);
        createReservation(date5, time6, theme1);
        createReservation(date6, time7, theme1);

        // theme2: 10건
        createReservation(date1, time1, theme2);
        createReservation(date1, time2, theme2);
        createReservation(date1, time3, theme2);
        createReservation(date2, time4, theme2);
        createReservation(date2, time5, theme2);
        createReservation(date3, time6, theme2);
        createReservation(date3, time7, theme2);
        createReservation(date4, time8, theme2);
        createReservation(date5, time9, theme2);
        createReservation(date6, time1, theme2);

        // theme3: 9건
        createReservation(date1, time1, theme3);
        createReservation(date1, time2, theme3);
        createReservation(date1, time3, theme3);
        createReservation(date2, time4, theme3);
        createReservation(date3, time5, theme3);
        createReservation(date4, time6, theme3);
        createReservation(date5, time7, theme3);
        createReservation(date6, time8, theme3);
        createReservation(date7, time9, theme3);

        // theme4: 8건
        createReservation(date1, time1, theme4);
        createReservation(date1, time2, theme4);
        createReservation(date2, time3, theme4);
        createReservation(date3, time4, theme4);
        createReservation(date4, time5, theme4);
        createReservation(date5, time6, theme4);
        createReservation(date6, time7, theme4);
        createReservation(date7, time8, theme4);

        // theme5: 7건
        createReservation(date1, time1, theme5);
        createReservation(date2, time2, theme5);
        createReservation(date3, time3, theme5);
        createReservation(date4, time4, theme5);
        createReservation(date5, time5, theme5);
        createReservation(date6, time6, theme5);
        createReservation(date7, time7, theme5);

        // theme6: 6건
        createReservation(date1, time1, theme6);
        createReservation(date2, time2, theme6);
        createReservation(date3, time3, theme6);
        createReservation(date4, time4, theme6);
        createReservation(date5, time5, theme6);
        createReservation(date6, time6, theme6);

        // theme7: 5건
        createReservation(date1, time1, theme7);
        createReservation(date2, time2, theme7);
        createReservation(date3, time3, theme7);
        createReservation(date4, time4, theme7);
        createReservation(date5, time5, theme7);

        // theme8: 4건
        createReservation(date1, time1, theme8);
        createReservation(date2, time2, theme8);
        createReservation(date3, time3, theme8);
        createReservation(date4, time4, theme8);

        // theme9: 3건
        createReservation(date1, time1, theme9);
        createReservation(date2, time2, theme9);
        createReservation(date3, time3, theme9);

        // theme10: 2건
        createReservation(date1, time1, theme10);
        createReservation(date2, time2, theme10);
    }

    private int createTime(String startAt) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", startAt))
                .when().post("/admin/times")
                .then().statusCode(201)
                .extract().jsonPath().getInt("id");
    }

    private int createTheme(String name, String description, String thumbnail) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "description", description, "thumbnail", thumbnail))
                .when().post("/admin/themes")
                .then().statusCode(201)
                .extract().jsonPath().getInt("id");
    }

    private void createReservation(String date, int timeId, int themeId) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "예약자", "date", date, "timeId", timeId, "themeId", themeId))
                .when().post("/reservations")
                .then().statusCode(201);
    }
}
