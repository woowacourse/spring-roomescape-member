package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EntityIdTest {

    @Nested
    class UUID_값을_기반으로_생성된다 {

        @Test
        void 값이_없다면_예외를_던진다() {
            assertThatThrownBy(() -> EntityId.fromUuid(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("EntityId는 빈 값을 지닐 수 없습니다.");
        }
        @Test
        void 값이_있다면_정상적으로_생성된다() {
            assertThatNoException().isThrownBy(() -> EntityId.fromUuid(UUID.randomUUID()));
        }
    }

    @Test
    void 무작위_값을_지니고_생성된다() {
        assertThatNoException().isThrownBy(EntityId::random);
    }
}
