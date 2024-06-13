package roomescape.domain.member;

import java.util.Arrays;

public enum MemberRole {
    ADMIN,
    USER;

    public static MemberRole convert(String value) {
        return Arrays.stream(MemberRole.values())
                .filter(memberRole -> memberRole.name().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 이름의 role이 존재하지 않습니다."));

    }
}
