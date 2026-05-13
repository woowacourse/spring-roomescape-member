package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EntityIdTest {

    @Nested
    class 문자열_값을_기반으로_생성된다 {

        @Test
        void 값이_없다면_예외를_던진다() {
            assertThatThrownBy(() -> EntityId.fromString(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("EntityId는 빈 값을 지닐 수 없습니다.");
        }

        @Test
        void UUID_형태가_아니라면_예외를_던진다() {
            String illegalFormatValue = "ILLEGAL FORMAT";

            assertThatThrownBy(() -> EntityId.fromString(illegalFormatValue))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid UUID string: " + illegalFormatValue);
        }

        @Test
        void 형태가_올바르다면_정상적으로_생성된다() {
            String legalFormatValue = UUID.randomUUID().toString();

            assertThatNoException().isThrownBy(() -> EntityId.fromString(legalFormatValue));
        }
    }

    @Test
    void 무작위_값을_지니고_생성된다() {
        assertThatNoException().isThrownBy(EntityId::random);
    }
}
