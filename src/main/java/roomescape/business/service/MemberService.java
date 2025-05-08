package roomescape.business.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.domain.member.Member;
import roomescape.config.LoginMember;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.dto.LoginRequestDto;
import roomescape.presentation.dto.MemberRequestDto;

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

    public String login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다."));
        if (!member.getPassword().equals(loginRequestDto.password())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }
        return createAccessToken(member);
    }

    private String createAccessToken(Member member) {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
        return Jwts.builder()
                .subject(member.getId().toString())
                .claim("id", member.getId())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public LoginMember getMemberFromToken(String accessToken) {
        Long memberIdFromToken = parseMemberIdAccessToken(accessToken);
        Member member = memberRepository.findById(memberIdFromToken)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
        return new LoginMember(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }

    private Long parseMemberIdAccessToken(String accesToken) {
        return Long.valueOf(Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor("Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes()))
                .build()
                .parseSignedClaims(accesToken)
                .getPayload()
                .getSubject());
    }
}
