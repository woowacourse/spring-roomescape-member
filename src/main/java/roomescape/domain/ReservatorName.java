package roomescape.domain;

import roomescape.exceptions.MissingRequiredFieldException;

public record ReservatorName(String name) {

    public ReservatorName {
        validate(name);
    }

    private void validate(String name) {
        if (name == null || name.isBlank() || name.isEmpty()) {
            throw new MissingRequiredFieldException("예악자 이름은 필수 값입니다.");
        }
    }
}
