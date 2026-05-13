package roomescape.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReservationLocalDateTest {

    @Test
    void 오늘_이후_날짜로_생성한다() {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // when
        ReservationLocalDate date = ReservationLocalDate.createForSave(tomorrow);

        // then
        assertThat(date.value()).isEqualTo(tomorrow);
    }

    @Test
    void 오늘_날짜로_생성하면_예외가_발생한다() {
        // given
        LocalDate today = LocalDate.now();

        // when & then
        assertThatThrownBy(() -> ReservationLocalDate.createForSave(today))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("오늘 이후의 날짜만 선택할 수 있습니다.");
    }

    @Test
    void 과거_날짜로_생성하면_예외가_발생한다() {
        // given
        LocalDate yesterday = LocalDate.now().minusDays(1);

        // when & then
        assertThatThrownBy(() -> ReservationLocalDate.createForSave(yesterday))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("오늘 이후의 날짜만 선택할 수 있습니다.");
    }


    @Nested
    @DisplayName("오늘 포함 오늘 이전의 날짜인 지 조회한다")
    class IsBeforeNow {

        @Test
        void 오늘_날짜인_경우_TRUE를_반환한다() {
            //given
            LocalDate today = LocalDate.now();
            ReservationLocalDate reservationDate = new ReservationLocalDate(today);

            //when
            boolean beforeNow = reservationDate.isOnBeforeNow();

            //then
            assertThat(beforeNow).isTrue();
        }

        @Test
        void 하루_전_날짜인_경우_TRUE를_반환한다() {
            //given
            LocalDate yesterday = LocalDate.now().minusDays(1);
            ReservationLocalDate reservationDate = new ReservationLocalDate(yesterday);

            //when
            boolean beforeNow = reservationDate.isOnBeforeNow();

            //then
            assertThat(beforeNow).isTrue();
        }

        @Test
        void 하루_지난_날짜인_경우_TRUE를_반환한다() {
            //given
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            ReservationLocalDate reservationDate = new ReservationLocalDate(tomorrow);

            //when
            boolean beforeNow = reservationDate.isOnBeforeNow();

            //then
            assertThat(beforeNow).isFalse();
        }
    }
}
