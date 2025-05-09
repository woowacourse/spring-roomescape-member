package roomescape.member.domain;

public record Member(Long id, String name, String email, String password) {
    // TODO : 검증 로직 필요
}
