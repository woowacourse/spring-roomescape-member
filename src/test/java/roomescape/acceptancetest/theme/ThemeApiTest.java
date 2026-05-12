package roomescape.acceptancetest.theme;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;
import roomescape.acceptancetest.fixture.AcceptanceTestFixture;

@RoomecapeAcceptanceTest
public class ThemeApiTest {

    @Autowired
    private AcceptanceTestFixture acceptanceTestFixture;

    @Test
    @DisplayName("인기 테마 조회")
    void findPopularThemes() {
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "bb");
        acceptanceTestFixture.createTheme("사라진 열쇠", "aa", "bb");
        acceptanceTestFixture.createTheme("비밀의 방", "aa", "bb");
        acceptanceTestFixture.createReservationTime("10:00", 1L);
        acceptanceTestFixture.createReservationTime("11:00", 1L);
        acceptanceTestFixture.createReservationTime("12:00", 2L);
        acceptanceTestFixture.createReservationTime("13:00", 3L);

        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.today().minusDays(1), 1L);
        acceptanceTestFixture.createReservation("코니", acceptanceTestFixture.today().minusDays(1), 2L);
        acceptanceTestFixture.createReservation("샐리", acceptanceTestFixture.today().minusDays(7), 3L);
        acceptanceTestFixture.createReservation("문", acceptanceTestFixture.today().minusDays(8), 4L);

        RestAssured.given().log().all()
                .queryParam("period", 7)
                .queryParam("limit", 2)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("id", contains(1, 2))
                .body("name", contains("미술관의 밤", "사라진 열쇠"));
    }

}
