package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.model.Member;
import roomescape.repository.MemberRepository;
import roomescape.service.dto.AuthDto;

import java.util.NoSuchElementException;

@Service
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String createToken(AuthDto authDto) {
        Member member = memberRepository.findMemberByEmail(authDto.getEmail())
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 해당 이메일을 가진 계정이 없습니다."));
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        return Jwts.builder()
                .subject(String.valueOf(member.getId()))
                .claim("name", member.getName())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
