package roomescape.reservation.entity;

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
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;

class ReservationTest {

    @DisplayName("아이디 존재 여부")
    @ParameterizedTest
    @CsvSource(value = {"1,true", "null,false"}, delimiter = ',', nullValues = "null")
    void test1(Long id, boolean expected) {
        // given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());

        Reservation reservation = new Reservation(id, "꾹이", LocalDate.now(), reservationTime);

        // when
        boolean result = reservation.existId();

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("25자 이하의 이름을 사용할 수 있다.")
    @Test
    void test2() {
        String nameLength25 = "aaaaaaaaaabbbbbbbbbbccc25";

        assertThatCode(() ->
                new Reservation(1L, nameLength25, LocalDate.now(), new ReservationTime(1L, LocalTime.now()))
        ).doesNotThrowAnyException();
    }

    @DisplayName("잘못된 이름을 사용하면 예외를 반환한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "aaaaaaaaaabbbbbbbbbbcccc26"})
    @ParameterizedTest
    void test3(String name) {
        // when & then
        assertThatThrownBy(() -> new Reservation(1L, name, LocalDate.now(), new ReservationTime(1L, LocalTime.now())))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
