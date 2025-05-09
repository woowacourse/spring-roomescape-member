package roomescape.domain.auth;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.domain.auth.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthApiTest {

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void init() {
        RestAssured.port = port;
    }

    @DisplayName("회원 정보를 등록한다")
    @Test
    void registerTest1() {
        // given

        // when

        // then
    }

    @DisplayName("이미 존재하는 이메일로 등록할 시 409 예외를 반환한다.")
    @Test
    void registerTest2() {

    }

    @DisplayName("로그인을 한다")
    @Test
    void loginTest1() {
        // given

        // when

        // then

    }

    @DisplayName("비밀번호가 다르다면 401 예외를 반환한다.")
    @Test
    void loginTest2() {
        // given

        // when

        // then

    }

    @DisplayName("이메일이 존재하지 않는다면 401 예외를 반환한다.")
    @Test
    void loginTest3() {
        // given

        // when

        // then

    }

    @DisplayName("쿠키를 통해 인증 정보를 조회할 수 있다.")
    @Test
    void checkTest1() {
        // given

        // when

        // then

    }

    @DisplayName("잘못된 쿠키는 401 예외를 반환한다.")
    @Test
    void checkTest2() {
        // given

        // when

        // then

    }
}
