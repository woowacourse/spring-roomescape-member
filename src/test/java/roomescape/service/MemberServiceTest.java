package roomescape.service;

import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.dto.LoginRequest;
import roomescape.dto.SignUpRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @LocalServerPort
    int port;

    @Test
    void 중복되는_이메일로_가입을_하면_예외가_발생한다() {
        SignUpRequest signUpRequest = new SignUpRequest("admin", "admin@admin.com", "password");
        Assertions.assertThatThrownBy(() -> memberService.addMember(signUpRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s는 중복되는 이메일입니다. ", signUpRequest.email()));
    }

    @Test
    void 존재하지_않는_토큰로_조회시_예외가_발생한다() {
        long id = Long.MAX_VALUE;
        Assertions.assertThatThrownBy(() -> memberService.findById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("id가 %d인 유저가 존재하지 않습니다.", id));
    }
}
