package roomescape.controller.auth;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TokenLoginControllerTest {
    @DisplayName("로그인 요청 시 토큰을 쿠키에 담아 반환한다.")
    @Test
    void login() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "anna@gmail.com");
        params.put("password", "password");
    }
}
