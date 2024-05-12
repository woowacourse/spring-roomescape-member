package roomescape.acceptance;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import roomescape.support.AcceptanceTest;
import roomescape.support.SimpleRestAssured;
import roomescape.ui.controller.dto.LoginRequest;

public class AuthAcceptanceTest extends AcceptanceTest {
    private String token;

    @TestFactory
    List<DynamicTest> 로그인_로그아웃으로_쿠키_생성_삭제() {
        return Arrays.asList(
                DynamicTest.dynamicTest("로그인하면 쿠키에 토큰을 저장한다.", () -> {
                    LoginRequest request = new LoginRequest("prin@gmail.com", "1q2w3e4r!@");
                    token = SimpleRestAssured.post("/login", request)
                            .statusCode(200)
                            .extract()
                            .cookie("token");
                }),
                DynamicTest.dynamicTest("토큰으로 유저를 체크한다.", () -> {
                    SimpleRestAssured.get("/login/check", token)
                            .statusCode(200)
                            .body("name", is("프린"));
                }),
                DynamicTest.dynamicTest("로그아웃하면 쿠키를 삭제한다", () -> {
                    SimpleRestAssured.post("/logout", "", token)
                            .statusCode(200)
                            .header("Set-Cookie", containsString("token=; Path=/; Max-Age=0;"));
                })
        );
    }
}
