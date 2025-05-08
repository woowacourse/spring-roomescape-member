package roomescape.member.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import roomescape.common.utils.Validator;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldNameConstants(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Member {

    private final MemberId id;
    private final MemberName name;
    private final MemberEmail email;
    private final MemberPassword password;
    private final Role role;

    private static Member of(final MemberId id, final MemberName name, final MemberEmail email, final MemberPassword password, final Role role) {
        validate(id, name, email, password, role);
        return new Member(id, name, email, password, role);
    }

    public static Member withId(final MemberId id, final MemberName name, final MemberEmail email, final MemberPassword password, final Role role) {
        return of(id, name, email, password, role);
    }

    public static Member withoutId(final MemberName name, final MemberEmail email, final MemberPassword password, final Role role) {
        return of(MemberId.unassigned(), name, email, password, role);
    }

    public static void validate(final MemberId id,
                                final MemberName name,
                                final MemberEmail email,
                                final MemberPassword password,
                                final Role role) {
        Validator.of(Member.class)
                .notNullField(Member.Fields.id, id)
                .notNullField(Member.Fields.name, name)
                .notNullField(Member.Fields.email, email)
                .notNullField(Member.Fields.password, password)
                .notNullField(Member.Fields.role, role);
    }
}
