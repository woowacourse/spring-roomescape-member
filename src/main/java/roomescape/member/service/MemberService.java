package roomescape.member.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.member.auth.controller.dto.LoginRequest;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.controller.dto.SignupRequest;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

import java.util.List;

@Service
public class MemberService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 정보입니다."));
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public List<MemberResponse> getAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    public MemberResponse search(String token) {
        Long id = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return MemberResponse.from(findMember);
    }

    public Member searchLoginMember(String token) {
        Long id = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

    public MemberResponse add(SignupRequest request) {
        Member member = request.toMemberWithoutId();
        Long id = memberRepository.saveAndReturnId(member);
        return MemberResponse.from(member.createMemberWithId(id));
    }

}
