package roomescape.domain_entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IdTest {

    @Test
    @DisplayName("null을 의미하는 Id 객체의 값을 조회할 시 예외가 발생한다.")
    void failIfGetEmptyValue() {
        Id id = Id.empty();

        assertThatThrownBy(() -> {
            id.value();
        }).isInstanceOf(IllegalStateException.class);
    }
}
