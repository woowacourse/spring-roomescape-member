package roomescape.infrastructure.authentication;

public record AuthenticatedMemberProfile(Long id, String name, boolean isAdmin) {
}
