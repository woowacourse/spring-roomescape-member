package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {
    private static final LocalDate DATE = LocalDate.parse("2026-05-05");
    private static final LocalTime START_AT = LocalTime.parse("10:00");

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void 이름이_null_또는_blank이면_예외(String name) {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when & then
        assertThatThrownBy(() -> new Reservation(name, DATE, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 이름이_255자를_초과하면_예외() {
        // given
        String name = "a".repeat(256);
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when & then
        assertThatThrownBy(() -> new Reservation(name, DATE, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 255자를 넘을 수 없습니다.");
    }

    @Test
    void 날짜_null로_예약_생성시_예외() {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when & then
        assertThatThrownBy(() -> new Reservation("구구", null, time, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 날짜는 비어 있을 수 없습니다.");
        ;
    }

    @Test
    void 예약_시간이_null이면_예약_생성시_예외() {
        // given
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when & then
        assertThatThrownBy(() -> new Reservation("홍길동", DATE, null, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 예약 시간은 비어있을 수 없습니다.");
    }

    @Test
    void 테마가_null이면_예약_생성시_예외() {
        // given
        ReservationTime time = new ReservationTime(1L, START_AT);

        // when & then
        assertThatThrownBy(() -> new Reservation("홍길동", DATE, time, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 테마는 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 255})
    void 예약_생성_성공_테스트(int count) {
        // given
        String name = "a".repeat(count);
        ReservationTime time = new ReservationTime(1L, START_AT);
        Theme theme = new Theme("테마 이름", "테마 설명", "썸네일");

        // when
        Reservation result = new Reservation(name, DATE, time, theme);

        // then
        assertThat(result.getName()).isEqualTo(name);
    }
}
