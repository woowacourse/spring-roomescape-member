package roomescape.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    Role(final String value) {
        this.value = value;
    }
}
