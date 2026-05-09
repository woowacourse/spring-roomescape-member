package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTest {

    @ParameterizedTest
    @ValueSource(strings = {"a", "pobizoninavy"})
    @DisplayName("이름은 2글자 이상 10글자 이하여야 한다")
    void 불가한_이름(String name) {
        //given
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.parse("10:00"));
        Theme theme = new Theme("공포", "무서움", "https://roomescape.com");

        //when & then
        assertThatThrownBy(() -> new Reservation(name, LocalDate.parse("2030-04-10"), reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("2글자 이상 10글자 이하");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("이름이 빈값일 때 검증")
    void 이름이_빈값일_때(String name) {
        //given
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.parse("10:00"));
        Theme theme = new Theme("공포", "무서움", "https://roomescape.com");

        //when & then
        assertThatThrownBy(() -> new Reservation(name, LocalDate.parse("2030-04-10"), reservationTime, theme))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("입력되지 않았습니다");
    }
}
