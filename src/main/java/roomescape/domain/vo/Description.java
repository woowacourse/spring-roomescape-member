package roomescape.domain.vo;


import roomescape.common.exception.DomainException;

import java.util.Objects;

public record Description(String value) {
    public Description {
        if (value.isBlank()) {
            throw new DomainException("설명은 공백일 수 없습니다");
        }
        if (value.length() > 500) {
            throw new DomainException("설명은 500자 이하여야 합니다");
        }
    }
}
