package roomescape.auth.service;

import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.Member;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.repository.MemberRepository;
import roomescape.global.infra.JwtTokenProvider;

@Service
public class AuthService {

  public static final String ADMIN_ROLE_NAME = "ADMIN";
  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthService(final MemberRepository memberRepository,
      final JwtTokenProvider jwtTokenProvider) {
    this.memberRepository = memberRepository;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public String login(final LoginRequest request) {
    final Member member = checkEmailAndPassword(request);
    return jwtTokenProvider.createToken(member.getEmail(), member.getName().getValue());
  }

  private Member checkEmailAndPassword(final LoginRequest request) {
    final Member member = findMemberByEmail(request.email());
    if (!member.getPassword().equals(request.password())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    return member;
  }

  public Member findMember(final String token) {
    final Map<String, String> payload = jwtTokenProvider.getPayload(token);
    return findMemberByEmail(payload.get("email"));
  }

  private Member findMemberByEmail(final String email) {
    return memberRepository.findByEmail(email).orElseThrow(
        () -> new NoSuchElementException("주어진 이메일로 가입한 멤버가 없습니다. (email : " + email + ")"));
  }
}
