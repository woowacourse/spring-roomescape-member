package roomescape.domain.member;

import roomescape.exception.ParameterException;

public class MemberName {
    private static final int MEMBER_NAME_MAXIMUM_LENGTH = 50;

    private final String name;

    public MemberName(String name) {
        validateNameExist(name);
        validateNameLength(name);
        this.name = name;
    }

    private void validateNameExist(String name) {
        if (name == null || name.isBlank()) {
            throw new ParameterException("회원 이름이 비어 있습니다.");
        }
    }

    private void validateNameLength(String name) {
        if (name.length() > MEMBER_NAME_MAXIMUM_LENGTH) {
            throw new ParameterException(
                    String.format("회원 이름은 %d자 이내여야 합니다.", MEMBER_NAME_MAXIMUM_LENGTH)
            );
        }
    }

    public String getName() {
        return name;
    }
}
