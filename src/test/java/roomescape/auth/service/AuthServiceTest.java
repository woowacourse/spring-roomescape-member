package roomescape.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.exception.InvalidPasswordException;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthServiceTest {
    private static final String NAME = "김철수";
    private static final String EMAIL = "chulsoo@example.com";
    private static final String PASSWORD = "123";
    private static final String ROLE = "USER";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthService authService;

    private Member member;
    private String memberToken;

    @BeforeEach
    void setUp() {
        member = new Member(1L, NAME, EMAIL, PASSWORD, ROLE);
        memberToken = jwtTokenProvider.createToken(member);
    }

    @Test
    @DisplayName("토큰을 생성한다.")
    void createTokenTest() {
        String actualToken = authService.createToken(new MemberRequest(EMAIL, PASSWORD));

        assertThat(actualToken).isEqualTo(memberToken);
    }

    @Test
    @DisplayName("없는 이메일을 입력한 경우, 에러가 발생한다.")
    void wrongEmailTest() {
        String wrongEmail = "wrongemail@example.com";
        assertThatThrownBy(() -> authService.createToken(new MemberRequest(wrongEmail, PASSWORD)))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("패스워드가 틀린 경우, 에러가 발생한다.")
    void wrongPasswordTest() {
        String wrongPassword = "1234";
        assertThatThrownBy(() -> authService.createToken(new MemberRequest(EMAIL, wrongPassword)))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    @DisplayName("토큰을 해석해서 멤버 객체를 얻는다.")
    void readByTokenTest() {
        Member actualMember = authService.readByToken(memberToken);

        assertThat(actualMember).isEqualTo(member);
    }
}
