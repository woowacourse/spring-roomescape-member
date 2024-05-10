package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.login.LoginRequest;
import roomescape.dto.login.LoginResponse;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String login(LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();  // TODO: 암호화

        Member member = memberRepository.findByEmailAndPassword(email, password);

        // TODO: 환경변수 분리
        String secretKey = "나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.나는 신이다.";

        // TODO: token에 어떤 정보가 들어가야 할지 고민 일단 token은 복호화하기 쉽기 때문에 password는 담으면 안됨
        // TODO: jwt 토근 생성 책임 분리 JwtProvider?
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("email", member.getEmail())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public LoginResponse findLoggedInMember(Long memberId) {
        Member member = memberRepository.findById(memberId);

        return LoginResponse.from(member);
    }
}
