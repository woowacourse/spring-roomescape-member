package roomescape.service.auth;

public interface AuthService {

    String authenticate(AuthenticationRequest request);

    AuthenticatedMemberProfile authorize(String token);
}
