package roomescape.controller.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationRequestTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void 이름이_null_또는_blank이면_예외(String name) {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest(name, "2026-05-02", 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 이름이_255자를_초과하면_예외() {
        // given
        String name = "a".repeat(256);

        // when & then
        assertThatThrownBy(() -> new ReservationRequest(name, "2026-05-02", 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 255자를 넘을 수 없습니다.");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void null_또는_빈_날짜로_생성시_예외(String date) {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest("구구", date, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 날짜는 비어 있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12-20", "2019-99-99"})
    void 올바르지_않은_형식의_날짜로_생성시_예외(String date) {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest("구구", date, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 날짜 형식이 올바르지 않습니다.");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void timeId가_양수가_아니면_예외_발생(Long timeId) {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest("홍길동", "2026-05-02", timeId, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] id는 양수이어야 합니다.");
    }

    @Test
    void 정상_생성_테스트() {
        // given
        String name = "홍길동";
        String date = "2026-05-02";
        Long timeId = 1L;
        Long themeId = 1L;

        // when
        ReservationRequest result = new ReservationRequest(name, date, timeId, themeId);

        // then
        assertAll(
                () -> assertThat(result.name()).isEqualTo(name),
                () -> assertThat(result.date()).isEqualTo(date),
                () -> assertThat(result.timeId()).isEqualTo(timeId),
                () -> assertThat(result.themeId()).isEqualTo(themeId));
    }
}
