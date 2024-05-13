package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationDateTest {

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("예약 날짜에 null이나 공백 문자열이 입력되면 예외가 발생한다.")
    void createReservationDateByNullOrEmptyDate(String given) {
        //when //then
        assertThatThrownBy(() -> ReservationDate.from(given))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜는 비어있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023.04.05", "12.05.04", "12-4-3", "1212-3-4"})
    @DisplayName("예약 날짜 형식이 맞지 않으면 예외가 발생한다.")
    void createReservationDateByInvalidDateFormat(String given) {
        //when //then
        assertThatThrownBy(() -> ReservationDate.from(given))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 날짜 형식은 yyyy-MM-dd 이어야 합니다.");
    }

    @ParameterizedTest
    @CsvSource(value = {"2024-04-04,2024-04-03,true", "2024-04-04,2024-05-05,false", "2024-04-04,2024-04-04,false"})
    @DisplayName("비교 날짜보다 이후인지 판단한다.")
    void isAfter(String from, String to, boolean expected) {
        //given
        ReservationDate fromDate = ReservationDate.from(from);
        ReservationDate toDate = ReservationDate.from(to);

        //when
        boolean result = fromDate.isAfter(toDate);

        //then
        assertThat(result).isEqualTo(expected);
    }
}
