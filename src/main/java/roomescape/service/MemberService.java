package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.CheckResponseDto;
import roomescape.dto.MemberRequestDto;
import roomescape.dto.MemberResponseDto;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.repository.MemberRepository;
import roomescape.token.JwtProvider;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        Member member = new Member(memberRequestDto.name(), memberRequestDto.email(),
            memberRequestDto.password());
        memberRepository.save(member);
        return MemberResponseDto.from(member);
    }

    public TokenResponse requestLogin(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmailAndPassword(tokenRequest.email(),
            tokenRequest.password());
        String token = jwtProvider.createToken(member.getEmail());
        return TokenResponse.from(token);
    }

    public CheckResponseDto validate(String value) {
        if (jwtProvider.validateToken(value)) {
            String email = jwtProvider.getTokenSubject(value);
            Member member = memberRepository.findByEmail(email);
            System.out.println("서비스 입니다.");
            System.out.println(member.getName());
            System.out.println(member.getEmail());
            System.out.println(member.getPassword());
            return CheckResponseDto.from(member);
        }
        throw new IllegalArgumentException("토큰 오류!!!!!!!!!!!!!!!!!!");
    }
}
