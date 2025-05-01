package roomescape.dto.request;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationCreateRequestTest {

    @DisplayName("예약자 이름은 null이면 예외를 발생한다.")
    @Test
    void validateNameNullThrowExceptionTest() {

        // given
        final String date = LocalDate.now().toString();

        // when & then
        assertThatThrownBy(() -> new ReservationCreateRequest(null, date, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }

    @DisplayName("예약자 이름이 비어있으면 예외를 발생한다.")
    @Test
    void validateNameBlankThrowExceptionTest() {

        // given
        final String name = "";
        final String date = LocalDate.now().toString();

        // when & then
        assertThatThrownBy(() -> new ReservationCreateRequest(name, date, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }

    @DisplayName("예약자 이름이 띄어쓰기면 예외를 발생한다.")
    @Test
    void validateNameSpacingThrowExceptionTest() {

        // given
        final String name = " ";
        final String date = LocalDate.now().toString();

        // when & then
        assertThatThrownBy(() -> new ReservationCreateRequest(name, date, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }

    @DisplayName("날짜가 비어있으면 예외가 발생한다.")
    @Test
    void validateDateNullThrowExceptionTest() {

        // given
        final String name = "체체";

        // when & then
        assertThatThrownBy(() -> new ReservationCreateRequest(name, null, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }

    @DisplayName("시간이 비어있으면 예외가 발생한다.")
    @Test
    void validateTimeNullThrowExceptionTest() {

        // given
        final String name = "체체";
        final String date = LocalDate.now().toString();

        // when & then
        assertThatThrownBy(() -> new ReservationCreateRequest(name, date, null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }

    @DisplayName("날짜 형식에 맞지 않으면 예외가 발생한다.")
    @Test
    void validateNonDateFormatThrowException() {

        // given
        final String name = "체체";
        final String date = "20254-21";

        // when & then
        assertThatThrownBy(() -> new ReservationCreateRequest(name, date, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력해주세요.");
    }

    @DisplayName("테마 id가 null이면 예외가 발생한다.")
    @Test
    void validateThemeNullThrowExceptionTest() {

        // given
        final String name = "체체";
        final String date = LocalDate.now().toString();

        // when & then
        assertThatThrownBy(() -> new ReservationCreateRequest(name, date, 1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }
}
