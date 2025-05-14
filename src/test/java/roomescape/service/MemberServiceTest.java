package roomescape.service;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Member;
import roomescape.domain.MemberRoleType;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.jwt.JwtProvider;
import roomescape.jwt.JwtRequest;
import roomescape.repository.MemberRepository;
import roomescape.service.dto.request.MemberLoginCreation;
import roomescape.service.dto.request.MemberSignUpCreation;
import roomescape.service.dto.response.MemberResult;
import roomescape.service.dto.response.MemberSignUpResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    JwtProvider jwtProvider;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    @Test
    @DisplayName("회원 가입을 할 수 있다")
    void register() {
        //given
        MemberSignUpCreation creation = new MemberSignUpCreation("new", "new@email.com", "1234");

        when(memberRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(new Member(1L, "new", "new@email.com", "1234", MemberRoleType.MEMBER)));
        when(memberRepository.insert(any(Member.class))).thenReturn(1L);

        //when
        MemberSignUpResult actual = memberService.register(creation);

        //then
        assertThat(actual.name()).isEqualTo(creation.name());
    }

    @Test
    @DisplayName("이미 사용 중인 이메일은 회원 가입 할 수 없다")
    void cannotRegisterWhenExistedEmail() {
        //given
        MemberSignUpCreation creation = new MemberSignUpCreation("duplicate", "member@email.com", "1234");
        when(memberRepository.existsByEmail(any(String.class))).thenReturn(true);

        //when //then
        assertThatThrownBy(() -> memberService.register(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessage("이미 사용 중인 이메일입니다");
    }

    @Test
    @DisplayName("사용자의 정보를 기반으로 jwt 토큰을 발행한다")
    void publishAccessToken() {
        //given
        MemberLoginCreation creation = new MemberLoginCreation("member@email.com", "1234");

        when(jwtProvider.generateToken(any(JwtRequest.class))).thenReturn("aaa");
        when(memberRepository.findByEmailAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(new Member(1L, "member", "member@email.com", "1234", MemberRoleType.MEMBER)));

        //when
        String actual = memberService.publishAccessToken(creation);

        //then
        assertThat(actual).isNotNull();
    }

    @ParameterizedTest
    @CsvSource(value = {"MEMBER,1", "ADMIN,1"})
    @DisplayName("권한을 기준으로 전체 유저를 조회한다")
    void getAllMemberByRole(MemberRoleType role, int expectedSize) {
        //when
        when(memberRepository.findAllByRole(role)).thenReturn(
                List.of(new Member(1L, "test", "test@email.com", "1234", role)));

        List<MemberResult> actual = memberService.getAllMemberByRole(role);

        //then
        assertThat(actual).hasSize(expectedSize);
    }
}
