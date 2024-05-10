package roomescape.service;

import io.jsonwebtoken.Claims;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.repository.JdbcMemberRepository;
import roomescape.service.dto.CreateMemberRequestDto;
import roomescape.service.dto.LoginMemberRequestDto;
import roomescape.service.dto.MemberResponseDto;
import roomescape.service.exception.MemberNotFoundException;

@Service
public class MemberService {

    private final JdbcMemberRepository memberRepository;
    private final JwtService jwtService;

    public MemberService(JdbcMemberRepository memberRepository, JwtService jwtService) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
    }

    public void signup(CreateMemberRequestDto requestDto) {
        if (memberRepository.isMemberExistsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 가입되어 있는 이메일 주소입니다.");
        }
        memberRepository.insertMember(requestDto.toMember());
    }

    public String login(LoginMemberRequestDto requestDto) {
        Member member = memberRepository.findMemberByEmail(requestDto.getEmail())
                .orElseThrow(() -> new MemberNotFoundException("회원 정보가 존재하지 않습니다."));

        if (member.isMismatchedPassword(requestDto.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return jwtService.generateToken(member);
    }

    public Member findMember(String token) {
        Claims claims = jwtService.verifyToken(token);
        long memberId = Long.parseLong(claims.getSubject());
        return memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("회원 정보가 존재하지 않습니다."));
    }

    public List<MemberResponseDto> findAllMemberNames() {
        return memberRepository.findAllMemberNames().stream()
                .map(MemberResponseDto::new)
                .toList();
    }
}
