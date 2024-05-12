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

  public String login(LoginRequest request) {
    Member member = checkEmailAndPassword(request);
    return jwtTokenProvider.createToken(member.getEmail(), member.getName().getValue());
  }

  private Member checkEmailAndPassword(LoginRequest request) {
    Member member = findMemberByEmail(request.email());
    if (!member.getPassword().equals(request.password())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    return member;
  }

  public Member findMember(String token) {
    Map<String, String> payload = jwtTokenProvider.getPayload(token);
    return findMemberByEmail(payload.get("email"));
  }

  public Member findMemberByEmail(String email) {
    return memberRepository.findByEmail(email).orElseThrow(
        () -> new NoSuchElementException("주어진 이메일로 가입한 멤버가 없습니다. (email : " + email + ")"));
  }

  public boolean checkAdminPermission(String token) {
    return false;
  }
}
