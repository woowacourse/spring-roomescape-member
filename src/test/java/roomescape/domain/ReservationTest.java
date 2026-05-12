package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;

class ReservationTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"a", "pobizoninavy", " "})
    @DisplayName("이름은 2글자 이상 10글자 이내여야 한다.")
    void 불가한_이름(String name) {
        //given
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.parse("10:00"));
        Theme theme = new Theme("공포", "무서움", "https://roomescape.com");

        //when & then
        assertThatThrownBy(() -> new Reservation(name, LocalDate.parse("2030-04-10"), reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름 형식");
    }
}
