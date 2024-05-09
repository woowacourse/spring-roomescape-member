package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.model.Member;
import roomescape.repository.MemberRepository;
import roomescape.service.dto.AuthDto;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuthService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String createToken(AuthDto authDto) {
        Member member = memberRepository.findMemberByEmail(authDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 해당 이메일을 가진 계정이 없습니다."));
        return Jwts.builder()
                .subject(String.valueOf(member.getId()))
                .claim("name", member.getName())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Member checkToken(String token) {
        long memberId = Long.parseLong(Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getPayload()
                .getSubject());
        Optional<Member> member = memberRepository.findMemberById(memberId);
        return member.orElseThrow(() -> new NoSuchElementException("[ERROR] 토큰에 해당하는 사용자가 없습니다."));
    }
}
