package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"a", "pobizoninavy", " "})
    @DisplayName("이름은 2글자 이상 10글자 이내여야 한다.")
    void 불가한_이름(String name){
        //given
        ReservationTime reservationTime = new ReservationTime(null, java.time.LocalTime.parse("10:00"));

        //when & then
        assertThatThrownBy(()-> new Reservation(null, name, java.time.LocalDate.parse("2023-04-10"), reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름 형식");
    }


}
