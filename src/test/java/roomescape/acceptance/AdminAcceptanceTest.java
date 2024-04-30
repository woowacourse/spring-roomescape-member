package roomescape.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.AcceptanceTest;
import roomescape.support.SimpleRestAssured;

class AdminAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/admin";

    @DisplayName("[1단계 - 홈 화면] 관리자 메인 페이지를 응답한다")
    @Test
    void step1() {
        SimpleRestAssured.get(PATH)
                .statusCode(200);
    }

    @DisplayName("[2단계 - 예약 조회] 관리자의 예약 관리 페이지를 응답한다")
    @Test
    void step2() {
        SimpleRestAssured.get(PATH + "/reservation")
                .statusCode(200);
    }
}
