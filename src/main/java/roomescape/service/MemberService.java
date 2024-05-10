package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.LogInRequest;
import roomescape.dto.MemberNameResponse;
import roomescape.infrastructure.JdbcMemberRepository;
import roomescape.service.exception.ResourceNotFoundException;

@Service
public class MemberService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final JdbcMemberRepository jdbcMemberRepository;

    public MemberService(JdbcMemberRepository jdbcMemberRepository) {
        this.jdbcMemberRepository = jdbcMemberRepository;
    }

    public String logIn(LogInRequest logInRequest) {
        String email = logInRequest.email();
        String password = logInRequest.password();

        Member member = jdbcMemberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ResourceNotFoundException("일치하는 이메일과 비밀번호가 없습니다."));

        return Jwts.builder().subject(member.getId().toString())
                .claim("role", member.getRole().name())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public MemberNameResponse getNameIfLogIn(String token) { // todo: 나중에도 안쓰면 삭제
        Long memberId = Long.valueOf(
                Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject());
        Member member = findValidatedSiteUserById(memberId);

        return new MemberNameResponse(member.getName());
    }

    public Member getUserGivenToken(String token) { //todo: 이름 수정 필요 & 위치가 정말로 여기가 맞는가?
        Long memberId = Long.valueOf(
                Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject());
        return findValidatedSiteUserById(memberId);
    } //todo: 중복 코드 제거

    private Member findValidatedSiteUserById(Long memberId) { //todo: findValidate~~ 이것도 인터셉터에서 처리할 수 있지 않을까?
        return jdbcMemberRepository.findById(memberId).orElseThrow(
                () -> new ResourceNotFoundException("아이디에 해당하는 사용자가 없습니다."));
    }
}
