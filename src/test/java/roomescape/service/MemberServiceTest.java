package roomescape.service;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.domain.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.exception.member.DuplicatedEmailException;
import roomescape.service.security.JwtProvider;
import roomescape.web.dto.request.member.SignupRequest;
import roomescape.web.dto.response.member.MemberResponse;

@SpringBootTest
class MemberServiceTest {
    private static final Member dummyMember = new Member("name", "email", "password");
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("모든 사용자들을 반환한다")
    void findAllMember_ShouldReturnAllMembers() {
        // given
        memberRepository.save(dummyMember);
        memberRepository.save(dummyMember);
        memberRepository.save(dummyMember);

        // when
        List<MemberResponse> responses = memberService.findAllMember();

        // then
        Assertions.assertThat(responses).hasSize(3);
    }

    @Test
    @DisplayName("회원가입을 요청을 할 수 있다")
    void signup_ShouldRegistrationNewMember() {
        // given
        SignupRequest request = new SignupRequest("name", "email@email.com", "password");

        // when
        memberService.signup(request);

        // then
        Assertions.assertThat(memberService.findAllMember())
                .hasSize(1);
    }

    @Test
    @DisplayName("중복된 이메일은 회원가입에 실패한다")
    void signup_ShouldThrowException_WhenDuplicatedEmail() {
        // given
        SignupRequest signupRequest = new SignupRequest("name2", "email@email.com", "password");
        memberRepository.save(new Member("name", "email@email.com", "password"));

        // when & then
        Assertions.assertThatThrownBy(() -> memberService.signup(signupRequest))
                .isInstanceOf(DuplicatedEmailException.class);
    }

    @Test
    @DisplayName("회원정보를 삭제할 수 있다")
    void withdrawal_ShouldRemovePersistence() {
        // given
        Member savedMember = memberRepository.save(dummyMember);

        // when
        memberService.withdrawal(savedMember.getId());

        // then
        Assertions.assertThat(memberRepository.findAll()).isEmpty();
    }
}
