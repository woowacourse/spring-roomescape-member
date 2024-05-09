package roomescape.service;

import static roomescape.exception.ExceptionType.LOGIN_FAIL;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Sha256Encryptor;
import roomescape.dto.LoginRequest;
import roomescape.exception.RoomescapeException;
import roomescape.repository.CollectionMemberRepository;
import roomescape.repository.MemberRepository;

class MemberServiceTest {

    @Test
    @DisplayName("잘못된 이메일이나 비밀번호로 로그인 시도할 경우 예외 발생하는지 확인")
    void loginWithInvalidRequest() {
        MemberRepository memberRepository = new CollectionMemberRepository();
        Sha256Encryptor encryptor = new Sha256Encryptor();
        MemberService memberService = new MemberService(memberRepository, encryptor);
        Assertions.assertThatThrownBy(() -> memberService.login(new LoginRequest("email@email.com", "123456")))
                .isInstanceOf(RoomescapeException.class)
                .hasMessage(LOGIN_FAIL.getMessage());
    }
}
