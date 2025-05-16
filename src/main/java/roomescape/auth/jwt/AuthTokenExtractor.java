package roomescape.auth.jwt;

public interface AuthTokenExtractor {

    String extractMemberIdFromToken(String token);

    String extractMemberRoleFromToken(String token);
}
