package roomescape.business.service.member;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.AccessToken;
import roomescape.business.domain.member.Member;
import roomescape.config.LoginMember;
import roomescape.exception.UnAuthorizedException;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.member.dto.LoginRequestDto;
import roomescape.presentation.member.dto.MemberRequestDto;
import roomescape.presentation.member.dto.MemberResponseDto;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long registerMember(MemberRequestDto memberRequestDto) {
        Member member = new Member(
                memberRequestDto.name(),
                memberRequestDto.email(),
                memberRequestDto.password()
        );
        return memberRepository.save(member);
    }

    public AccessToken login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));
        if (!member.getPassword().equals(loginRequestDto.password())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
        return AccessToken.create(member);
    }

    public LoginMember getMemberFromToken(AccessToken accessToken) {
        Long memberIdFromToken = accessToken.extractMemberId();
        Member member = memberRepository.findById(memberIdFromToken)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return new LoginMember(
                member.getId(),
                member.getName(),
                member.getRole()
        );
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
