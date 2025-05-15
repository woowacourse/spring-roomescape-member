package roomescape.business.service.auth;

import org.springframework.stereotype.Service;
import roomescape.business.domain.member.MemberCredential;
import roomescape.business.domain.member.MemberRole;
import roomescape.business.domain.member.SignUpMember;
import roomescape.config.AccessToken;
import roomescape.config.JwtPayload;
import roomescape.config.LoginMember;
import roomescape.exception.MemberException;
import roomescape.exception.UnAuthorizedException;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.auth.dto.LoginRequestDto;
import roomescape.presentation.auth.dto.MemberRequestDto;

@Service
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
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
        return createAccessToken(memberCredential);
    }

    private AccessToken createAccessToken(MemberCredential memberCredential) {
        MemberRole memberRole = getMemberRole(memberCredential);
        return AccessToken.create(new JwtPayload(memberCredential.getId(), memberRole.value()));
    }

    private MemberRole getMemberRole(MemberCredential memberCredential) {
        return memberRepository.findById(memberCredential.getId())
                .orElseThrow(() -> new MemberException("회원 정보가 존재하지 않습니다."))
                .getRole();
    }

    public String checkLogin(LoginMember loginMember) {
        if (loginMember == null) {
            throw new UnAuthorizedException("로그인이 필요합니다.");
        }
        return loginMember.name();
    }
}
