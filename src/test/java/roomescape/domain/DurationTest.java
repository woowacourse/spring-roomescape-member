package roomescape.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DurationTest {

    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(2000, 1, 1);
    private static final LocalDate DEFAULT_END_DATE = LocalDate.of(3000, 1, 1);

    @Nested
    class 시작일이나_종료일_중_하나_이상이_null이라면_정상_생성된다 {

        @Test
        void 시작일이_null이라면_정상_생성된다() {
            assertThatNoException().isThrownBy(() -> new Duration(
                    null,
                    DEFAULT_END_DATE
            ));
        }

        @Test
        void 종료일이_null이라면_정상_생성된다() {
            assertThatNoException().isThrownBy(() -> new Duration(
                    DEFAULT_START_DATE,
                    null
            ));
        }

        @Test
        void 시작일과_종료일이_null이라면_정상_생성된다() {
            assertThatNoException().isThrownBy(() -> new Duration(
                    null,
                    null
            ));
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
