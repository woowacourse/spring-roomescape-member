package roomescape.reservation.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberReservationRequestTest {

    @Test
    @DisplayName("날짜가 null인 경우 예외가 발생한다")
    void test1() {
        assertThatThrownBy(() -> new MemberReservationRequest(null, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 날짜를 입력해주세요.");
    }

    @Test
    @DisplayName("지난 날짜로 예약을 시도하면 예외가 발생한다")
    void test2() {
        // given
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // when & then
        assertThatThrownBy(() -> new MemberReservationRequest(yesterday, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 지난 날짜로는 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("시간 id가 null인 경우 예외가 발생한다")
    void test3() {
        assertThatThrownBy(() -> new MemberReservationRequest(LocalDate.now(), null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간 id를 입력해주세요.");
    }

    @Test
    @DisplayName("테마 id가 null인 경우 예외가 발생한다")
    void test4() {
        assertThatThrownBy(() -> new MemberReservationRequest(LocalDate.now(), 1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 테마 id를 입력해주세요.");
    }

    @Test
    @DisplayName("모든 필드가 유효할 경우 객체가 정상적으로 생성된다")
    void test5() {
        // given
        LocalDate validDate = LocalDate.now();
        Long validTimeId = 1L;
        Long validThemeId = 1L;

        // when
        MemberReservationRequest request = new MemberReservationRequest(validDate, validTimeId, validThemeId);

        // then
        assertThat(request).isNotNull();
    }
}
