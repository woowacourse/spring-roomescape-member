package roomescape.member.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Password {

    private final String value;

    public static Password createForLoginMember() {
        return new Password(null);
    }

    public static Password createForMember(@NonNull final String password) {
        return new Password(password);
    }

    public boolean matchesPassword(final String password) {
        return value.equals(password);
    }
}
