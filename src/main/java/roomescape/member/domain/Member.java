package roomescape.member.domain;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class Member {

    private final Long id;

    @NonNull
    private final String name;

    @NonNull
    private final String email;

    @NonNull
    private final String password;

    @NonNull
    private final MemberRole role;

    public Member(final Long id, @NonNull final String name, @NonNull final String email,
                  @NonNull final String password,
                  @NonNull final String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = MemberRole.valueOf(role);
    }

    public Member(@NonNull final String name, @NonNull final String email, @NonNull final String password) {
        this.id = null;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = MemberRole.MEMBER;
    }

    public boolean matchesPassword(final String password) {
        return this.password.equals(password);
    }
}
