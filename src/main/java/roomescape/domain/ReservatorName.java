package roomescape.domain;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservatorName name1 = (ReservatorName) o;
        return Objects.equals(name, name1.name);
    }
}
