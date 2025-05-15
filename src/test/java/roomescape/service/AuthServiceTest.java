package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.repository.JdbcAuthRepository;
import roomescape.auth.service.AuthService;
import roomescape.entity.Member;
import roomescape.exception.impl.HasDuplicatedEmailException;
import roomescape.exception.impl.InvalidLoginException;
import roomescape.exception.impl.MemberNotFountException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthServiceTest {

    @Autowired
    private JdbcAuthRepository authRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUP() {
        //사용자 생성
        Member member = authRepository.save(Member.beforeMemberSave(
                "레몬",
                "ywcsuwon@naver.com",
                "123")
        );
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입시 예외가 발생한다.")
    void whenRegisterWithDuplicatedEmailThrowExceptionTest() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> authService.register("메론", "ywcsuwon@naver.com", "123"))
                .isInstanceOf(HasDuplicatedEmailException.class);
    }

    @Test
    @DisplayName("로그인시 비밀번호가 틀리다면 예외가 발생한다.")
    void whenLoginWithInvalidPasswordThrowExceptionTest() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> authService.login("ywcsuwon@naver.com", "1234"))
                .isInstanceOf(InvalidLoginException.class);
    }

    @Test
    @DisplayName("로그인시 없는 회원이라면 예외가 발생한다.")
    void shouldThrowExceptionWhenMemberNotFoundOnLogin() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> authService.login("ywcsuwon@naver123.com", "123"))
                .isInstanceOf(MemberNotFountException.class);
    }
}
