package roomescape.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class Member {

    private Long id;

    @NonNull
    private final String name;

    @NonNull
    private final String email;

    @NonNull
    private final String password;

    public Member(@NonNull final String name, @NonNull final String email, @NonNull final String password) {
        this.id = null;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
