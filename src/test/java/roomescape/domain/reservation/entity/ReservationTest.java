package roomescape.domain.reservation.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    @DisplayName("아이디 존재 여부")
    @ParameterizedTest
    @CsvSource(value = {"1,true", "null,false"}, delimiter = ',', nullValues = "null")
    void test1(Long id, boolean expected) {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        Theme theme = new Theme(1L, "공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        Reservation reservation = new Reservation(id, "꾹이", LocalDate.now(), reservationTime, theme);

        // when
        boolean result = reservation.existId();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("25자 이하의 이름을 사용할 수 있다.")
    @Test
    void test2() {
        String nameLength25 = "aaaaaaaaaabbbbbbbbbbccc25";
        Theme theme = new Theme(1L, "공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        assertThatCode(() ->
                new Reservation(1L, nameLength25, LocalDate.now(), new ReservationTime(1L, LocalTime.now()), theme)
        ).doesNotThrowAnyException();
    }

    @DisplayName("잘못된 이름을 사용하면 예외를 반환한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "aaaaaaaaaabbbbbbbbbbcccc26"})
    @ParameterizedTest
    void test3(String name) {
        Theme theme = new Theme(1L, "공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when & then
        assertThatThrownBy(() ->
                new Reservation(1L, name, LocalDate.now(), new ReservationTime(1L, LocalTime.now()), theme)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
