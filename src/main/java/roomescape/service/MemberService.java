package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.dto.LogInRequest;
import roomescape.dto.MemberPreviewResponse;
import roomescape.infrastructure.JdbcMemberRepository;
import roomescape.service.exception.ResourceNotFoundException;

import java.util.List;

@Service
public class MemberService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final MemberRepository memberRepository;

    public MemberService(JdbcMemberRepository jdbcMemberRepository) {
        this.memberRepository = jdbcMemberRepository;
    }

    public String logIn(LogInRequest logInRequest) {
        String email = logInRequest.email();
        String password = logInRequest.password();

        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ResourceNotFoundException("일치하는 이메일과 비밀번호가 없습니다."));

        return Jwts.builder().subject(member.getId().toString())
                .claim("role", member.getRole().name())
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public MemberPreviewResponse getNameIfLogIn(String token) { // todo: 나중에도 안쓰면 삭제
        Long memberId = Long.valueOf(
                Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject());
        Member member = findValidatedSiteUserById(memberId);

        return new MemberPreviewResponse(member);
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
        return memberRepository.findById(memberId).orElseThrow(
                () -> new ResourceNotFoundException("아이디에 해당하는 사용자가 없습니다."));
    }

    public List<MemberPreviewResponse> getAllMemberPreview() {
        return memberRepository.findAll().stream()
                .map(MemberPreviewResponse::new)
                .toList();
    }
}
