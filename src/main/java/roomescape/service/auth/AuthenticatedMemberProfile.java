package roomescape.service.auth;

public record AuthenticatedMemberProfile(Long id, String name, boolean isAdmin) {
}
