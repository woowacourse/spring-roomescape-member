package roomescape.dto;

import java.time.LocalTime;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

class ReservationRequestDtoTest {
    private static Stream<Arguments> testCasesForDateFormat() {
        return Stream.of(
                Arguments.of("invalidInput"),
                Arguments.of("2023-10-32"),
                Arguments.of("2023-13-23")
        );
    }

    @ParameterizedTest
    @MethodSource("testCasesForDateFormat")
    @DisplayName("날짜 형식이 올바르지 않은 경우 예외 처리한다.")
    void test(String input) {
        //given
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto("name", input, 1L, 1L);
        // when & then
        assertThatThrownBy(
                () -> reservationRequestDto.convertToReservation(new ReservationTime(1L, LocalTime.of(12, 30)),
                        new Theme(1L, "공포", "무서워요", "image")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("날짜");

    }

    @DisplayName("예약자명은 null 일 수 없다.")
    @Test
    void test1() {
        assertThatThrownBy(() -> new ReservationRequestDto(
                null, "2024-12-11", 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("예약 날짜는 null 일 수 없다.")
    @Test
    void test2() {
        assertThatThrownBy(() -> new ReservationRequestDto(
                "aa", null, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("예약 시간은 null 일 수 없다.")
    @Test
    void test3() {
        assertThatThrownBy(() -> new ReservationRequestDto(
                "aa", "2024-12-12", null, 1L))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("예약 시간은 null 일 수 없다.")
    @Test
    void test4() {
        assertThatThrownBy(() -> new ReservationRequestDto(
                "aa", "2024-12-12", 1L, null))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("예약자명은 공백일 수 없다.")
    @Test
    void test5() {
        assertThatThrownBy(() -> new ReservationRequestDto(
                " ", "2024-12-12", 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("예약 날짜는 공백일 수 없다.")
    @Test
    void test6() {
        assertThatThrownBy(() -> new ReservationRequestDto(
                "히로", " ", 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);

    }

}
