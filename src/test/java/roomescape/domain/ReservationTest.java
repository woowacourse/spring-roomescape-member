package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    private static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID TIME_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    private static final UUID THEME_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private static final String DEFAULT_NAME = "name";
    private static final LocalDate DEFAULT_DATE = LocalDate.of(2025, 1, 1);

    @Test
    void 식별자가_없다면_예외를_던진다() {
        assertThatThrownBy(() -> new Reservation(
                null,
                DEFAULT_NAME,
                DEFAULT_DATE,
                TIME_ID,
                THEME_ID
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약엔 식별자가 존재해야 합니다.");
    }
    @Nested
    class 이름을_검증한다 {

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\t", "\n"})
        void 이름이_비어_있으면_예외를_던진다(String emptyName) {
            assertThatThrownBy(() -> new Reservation(
                    DEFAULT_ID,
                    emptyName,
                    DEFAULT_DATE,
                    TIME_ID,
                    THEME_ID
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약엔 이름이 존재해야 합니다.");
        }

        @Test
        void 이름이_없다면_예외를_던진다() {
            assertThatThrownBy(() -> new Reservation(
                    DEFAULT_ID,
                    null,
                    DEFAULT_DATE,
                    TIME_ID,
                    THEME_ID
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약엔 이름이 존재해야 합니다.");
        }
    }

    @Test
    void 날짜가_없다면_예외를_던진다() {
        assertThatThrownBy(() -> new Reservation(
                DEFAULT_ID,
                DEFAULT_NAME,
                null,
                TIME_ID,
                THEME_ID
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약엔 날짜가 존재해야 합니다.");
    }

    @Test
    void 시간이_없다면_예외를_던진다() {
        assertThatThrownBy(() -> new Reservation(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DATE,
                null,
                THEME_ID
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약엔 시간이 존재해야 합니다.");
    }

    @Test
    void 테마가_없다면_예외를_던진다() {
        assertThatThrownBy(() -> new Reservation(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_DATE,
                TIME_ID,
                null
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약엔 테마가 존재해야 합니다.");
    }
}
