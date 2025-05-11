package roomescape.member.domain;

import roomescape.global.domain.Id;
import roomescape.member.exception.InvalidMemberException;

public class Member {

    private static final int MAX_NAME_LENGTH = 10;

    private final Id id;
    private final String name;
    private final String email;
    private final String password;
    private final MemberRole memberRole;

    private Member(final Id id, final String name, final String email, final String password,
                   final MemberRole memberRole) {
        validate(name);
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.memberRole = memberRole;
    }

    public static Member of(final Long id, final String name, final String email, final String password,
                            final MemberRole memberRole) {
        return new Member(Id.assignDatabaseId(id), name, email, password, memberRole);
    }

    public static Member withUnassignedId(final String name, final String email, final String password,
                                          final MemberRole memberRole) {
        return new Member(Id.unassigned(), name, email, password, memberRole);
    }

    private void validate(final String name) {
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidMemberException("name은 10글자 이하이어야합니다.");
        }
    }

    public Long getId() {
        return id.getDatabaseId();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }
}
