package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.repository.JdbcMemberRepository;
import roomescape.service.auth.JwtProvider;
import roomescape.service.dto.CreateMemberRequestDto;
import roomescape.service.dto.LoginMemberRequestDto;
import roomescape.service.dto.MemberResponseDto;
import roomescape.service.exception.MemberNotFoundException;

@Service
public class MemberService {

    private final JdbcMemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(JdbcMemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public void signup(CreateMemberRequestDto requestDto) {
        if (memberRepository.isMemberExistsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입되어 있는 이메일 주소입니다.");
        }
        memberRepository.insertMember(requestDto.toMember());
    }

    public String verifyMember(LoginMemberRequestDto requestDto) {
        Member member = memberRepository.findMemberByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("회원 정보가 존재하지 않습니다."));

        if (member.isPasswordDifferent(requestDto.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        return jwtProvider.createToken(member);
    }

    public MemberResponseDto verifyToken(String token) {
        long memberId = Long.parseLong(jwtProvider.getclaims(token).getSubject());
        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보가 존재하지 않습니다."));
        return new MemberResponseDto(member);
    }
}
