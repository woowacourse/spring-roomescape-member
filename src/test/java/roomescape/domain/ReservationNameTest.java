package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationNameTest {

    @DisplayName("이름이 공백이면 IllegalArgumentException 반환한다.")
    @Test
    void return_illegalArgumentException_name_empty() {
        assertThatThrownBy(() -> new ReservationName(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 공백을 허용하지 않습니다.");
    }

    @DisplayName("이름이 같으면 같은 hash 값을 갖고 동일한 객체이다.")
    @Test
    void hash_equals_override_test() {
        ReservationName reservationName1 = new ReservationName("재즈");
        ReservationName reservationName2 = new ReservationName("재즈");

        assertAll(
                () -> assertThat(reservationName1.hashCode()).isEqualTo(reservationName2.hashCode()),
                () -> assertThat(reservationName1).isEqualTo(reservationName2));
    }
}
