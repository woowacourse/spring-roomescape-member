package roomescape.domain.vo;

import roomescape.common.exception.DomainException;

public record Name(String value) {
    public Name {
        if (value.isBlank()) {
            throw new DomainException("이름은 공백일 수 없습니다");
        }
        if (value.length() > 50) {
            throw new DomainException("이름은 50자 이하여야 합니다");
        }
    }
}

