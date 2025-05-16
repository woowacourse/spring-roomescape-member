package roomescape.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Member {

    private final Long id;
    private final String name;
    private final String email;
    private final Password password;
    private final MemberRole role;

    @Builder
    private Member(
            final Long id,
            @NonNull final String name,
            @NonNull final String email,
            @NonNull final Password password,
            @NonNull final MemberRole role
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean matchesPassword(final String password) {
        return this.password.matchesPassword(password);
    }

    public boolean isAdmin() {
        return this.role.isAdmin();
    }

    public String getPassword() {
        return this.password.getValue();
    }
}
