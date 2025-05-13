package roomescape.business.service.member;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberCredential;
import roomescape.business.domain.member.SignUpMember;
import roomescape.config.AccessToken;
import roomescape.config.LoginMember;
import roomescape.exception.MemberException;
import roomescape.exception.UnAuthorizedException;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.member.dto.LoginRequestDto;
import roomescape.presentation.member.dto.MemberRequestDto;
import roomescape.presentation.member.dto.MemberResponseDto;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long registerMember(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.email())) {
            throw new MemberException("이미 존재하는 이메일입니다.");
        }
        SignUpMember signUpMember = new SignUpMember(
                memberRequestDto.name(),
                memberRequestDto.email(),
                memberRequestDto.password()
        );
        return memberRepository.save(signUpMember);
    }

    public AccessToken login(LoginRequestDto loginRequestDto) {
        MemberCredential memberCredential = memberRepository.findCredentialByEmail(loginRequestDto.email())
                .orElseThrow(() -> new MemberException("이메일 또는 비밀번호가 잘못되었습니다."));
        if (!memberCredential.matchesPassword(loginRequestDto.password())) {
            throw new MemberException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
        return AccessToken.create(memberCredential);
    }

    public List<MemberResponseDto> getMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::toResponse)
                .toList();
    }

    public String checkLogin(LoginMember loginMember) {
        if (loginMember == null) {
            throw new UnAuthorizedException("로그인이 필요합니다.");
        }
        return loginMember.name();
    }
}
