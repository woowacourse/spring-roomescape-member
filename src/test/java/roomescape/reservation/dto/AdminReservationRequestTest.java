package roomescape.reservation.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdminReservationRequestTest {

    @Test
    @DisplayName("모든 필드가 유효할 경우 객체가 정상적으로 생성된다")
    void test5() {
        // when
        AdminReservationRequest request = new AdminReservationRequest(LocalDate.now(), 1L, 1L, 1L);

        // then
        assertThat(request).isNotNull();
    }

    @Test
    @DisplayName("날짜가 null인 경우 예외가 발생한다")
    void test1() {
        assertThatThrownBy(() -> new AdminReservationRequest(null, 1L, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 날짜를 입력해주세요.");
    }

    @Test
    @DisplayName("테마 id가 null인 경우 예외가 발생한다")
    void test2() {
        assertThatThrownBy(() -> new AdminReservationRequest(LocalDate.now(), null, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 테마 id를 입력해주세요.");
    }

    @Test
    @DisplayName("시간 id가 null인 경우 예외가 발생한다")
    void test3() {
        assertThatThrownBy(() -> new AdminReservationRequest(LocalDate.now(), 1L, null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간 id를 입력해주세요.");
    }

    @Test
    @DisplayName("사용자 id가 null인 경우 예외가 발생한다")
    void test4() {
        assertThatThrownBy(() -> new AdminReservationRequest(LocalDate.now(), 1L, 1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 사용자 id를 입력해주세요.");
    }
}
