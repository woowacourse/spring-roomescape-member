package roomescape.controller.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidInputException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationRequestTest {
    private LocalDate date = LocalDate.parse("2026-05-02");

    @Test
    void 이름이_null이면_예외() {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest(null, date, 1L, 1L))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 이름이_255자를_초과하면_예외() {
        // given
        String name = "a".repeat(256);

        // when & then
        assertThatThrownBy(() -> new ReservationRequest(name, date, 1L, 1L))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("이름은 255자를 넘을 수 없습니다.");
    }

    @Test
    void 날짜를_null_로_생성시_예외() {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest("구구", null, 1L, 1L))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("날짜는 비어 있을 수 없습니다.");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void timeId가_양수가_아니면_예외_발생(Long timeId) {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest("홍길동", date, timeId, 1L))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("시간ID는 양수이어야 합니다.");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void themeId가_양수가_아니면_예외_발생(Long themeId) {
        // when & then
        assertThatThrownBy(() -> new ReservationRequest("홍길동", date, 1L, themeId))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("테마ID는 양수이어야 합니다.");
    }

    @Test
    void 정상_생성_테스트() {
        // given
        String name = "홍길동";
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
