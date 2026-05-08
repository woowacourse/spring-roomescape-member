package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DurationTest {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(2000, 1, 1);
    private static final LocalDate DEFAULT_END_DATE = LocalDate.of(3000, 1, 1);

    @Nested
    class 값이_존재함을_검증한다 {

        @Test
        void 시작일이_null이라면_예외를_던진다() {
            assertThatThrownBy(() -> new Duration(
                    null,
                    DEFAULT_END_DATE
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("시작일이 필요합니다.");
        }

        @Test
        void 종료일이_null이라면_예외를_던진다() {
            assertThatThrownBy(() -> new Duration(
                    DEFAULT_START_DATE,
                    null
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("종료일이 필요합니다.");
        }
    }

    @Nested
    class 시작일이_종료일_이전임을_검증한다 {

        @Test
        void 시작일과_종료일이_같다면_정상_생성된다() {
            LocalDate sameDate = DEFAULT_START_DATE;

            assertThatNoException().isThrownBy(() -> new Duration(
                    sameDate,
                    sameDate
            ));
        }

        @Test
        void 시작일이_종료일보다_빠르다면_정상_생성된다() {
            LocalDate startDate = DEFAULT_START_DATE;
            LocalDate laterEndDate = startDate.plusDays(1);

            assertThatNoException().isThrownBy(() -> new Duration(
                    startDate,
                    laterEndDate
            ));
        }

        @Test
        void 시작일이_종료일_이후라면_예외를_던진다() {
            LocalDate startDate = DEFAULT_START_DATE;
            LocalDate earlierEndDate = startDate.minusDays(1);

            assertThatThrownBy(() -> new Duration(
                    startDate,
                    earlierEndDate
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("시작일은 종료일과 같거나 앞서야 합니다.");
        }
    }
}
