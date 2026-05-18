package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationDate;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.exception.BusinessException;
import roomescape.exception.ErrorCode;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;

class ReservationTest {
    private final Theme theme = new Theme(1L, new ThemeName("테스트 테마"), "", ThemeImageUrl.defaultImageUrl());

    @Test
    void 오늘_이전의_날짜로_예약시_예외가_발생한다() {
        // given
        ReservationDate yesterday = new ReservationDate(LocalDate.now().minusDays(1));

        // when & then
        assertThatCode(() -> Reservation.create(new MemberName("브라운"), yesterday, new ReservationTime(1L, LocalTime.parse("12:00")), theme))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.PAST_DATE_RESERVATION.getMessage());
    }

    @Test
    void 이전_시간으로_예약시_예외가_발생한다() {
        // given
        ReservationTime oneHourEarlier = new ReservationTime(1L, LocalTime.now().minusHours(1L));

        // when & then
        assertThatCode(() -> Reservation.create(new MemberName("브라운"), new ReservationDate(LocalDate.now()), oneHourEarlier, theme))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.PAST_DATE_RESERVATION.getMessage());
    }
}