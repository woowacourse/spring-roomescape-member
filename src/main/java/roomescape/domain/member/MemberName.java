package roomescape.domain.member;

import roomescape.exceptions.MissingRequiredFieldException;

public record MemberName(String name) {

    public MemberName {
        validate(name);
    }

    private void validate(String name) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw new MissingRequiredFieldException("사용자 이름은 필수 값입니다.");
        }
    }
}
