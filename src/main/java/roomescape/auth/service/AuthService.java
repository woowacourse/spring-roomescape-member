package roomescape.auth.service;

import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.Member;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.repository.MemberRepository;
import roomescape.globar.infra.JwtTokenProvider;

@Service
public class AuthService {

  private final MemberRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;

  public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
    this.memberRepository = memberRepository;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public String createUser(LoginRequest request) {
    return jwtTokenProvider.createToken(request.email(), "어드민");
  }

  public Member findMember(String token) {
    Map<String, String> payload = jwtTokenProvider.getPayload(token);
    return findMemberByEmail(payload.get("email"));
  }

  public Member findMemberByEmail(String email) {
    return memberRepository.findByEmail(email).orElseThrow(
        () -> new NoSuchElementException("주어진 이메일로 가입한 멤버가 없습니다. (email : " + email + ")"));
  }
}
