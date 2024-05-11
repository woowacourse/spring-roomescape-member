package roomescape.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.List;
import javax.naming.AuthenticationException;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Email;
import roomescape.domain.member.LoginMember;
import roomescape.domain.member.Member;
import roomescape.domain.member.Password;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.MemberNameResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.exceptions.NotFoundException;
import roomescape.repository.member.MemberRepository;


@Service
public class MemberService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String getLoginToken(LoginRequest loginRequest) throws AuthenticationException {
        return memberRepository.findByEmailAndPassword(
                        new Email(loginRequest.email()),
                        new Password(loginRequest.password()))
                .map(this::createToken)
                .orElseThrow(() -> new AuthenticationException("이메일 또는 비밀번호가 틀립니다."));
    }

    private String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public MemberNameResponse getMemberResponse(String token) throws AuthenticationException {
        Long memberId = parseTokenToMemberId(token);

        return memberRepository.findById(memberId)
                .map(member -> new MemberNameResponse(member.getName().name()))
                .orElseThrow(() -> new NotFoundException("존재하지 않는 로그인 정보입니다. token = " + token));
    }

    private Long parseTokenToMemberId(String token) throws AuthenticationException {
        try {
            return Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject()
            );
        } catch (JwtException e) {
            throw new AuthenticationException("유효하지 않은 토큰입니다.");
        }
    }

    public LoginMemberRequest getLoginMemberRequest(String token) throws AuthenticationException {
        Long memberId = parseTokenToMemberId(token);

        return memberRepository.findById(memberId)
                .map(member -> new LoginMemberRequest(memberId, member.getName().name(), member.getEmail().email()))
                .orElseThrow(() -> new NotFoundException("존재하지 않는 로그인 정보입니다. token = " + token));
    }

    public LoginMember getLoginMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(LoginMember::new)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 회원 id입니다. memberId = " + memberId));
    }

    // 여기까지 구현 완료. 관리자예약 시 MemberId가 필요해서 MemberResponse를 새로 정의함.
    // 테스트코드 짜고 커밋하면 될 듯?
    public List<MemberResponse> findMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::new)
                .toList();
    }
}
