package roomescape.service.auth;

//TODO: 패키지 재고민 필요
public interface AuthService {

    String authenticate(AuthenticationRequest request);

    AuthenticatedProfile authorize(String token);
}
