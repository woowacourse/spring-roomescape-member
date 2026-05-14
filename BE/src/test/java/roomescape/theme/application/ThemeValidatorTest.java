package roomescape.theme.application;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.customException.BusinessException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.fake.FakeReservationRepository;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

class ThemeValidatorTest {

    private ReservationRepository reservationRepository;
    private ThemeValidator themeValidator;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        themeValidator = new ThemeValidator(reservationRepository);
    }

    @Test
    @DisplayName("테마를 참조하는 예약이 없으면 예외가 발생하지 않는다")
    void validateNotReferencedByReservation_success() {
        // when & then
        assertThatCode(() -> themeValidator.validateNotReferencedByReservation(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("테마를 참조하는 예약이 있으면 예외가 발생한다")
    void validateNotReferencedByReservation_fail_with_referenced_theme() {
        // given
        ReservationTime time = ReservationTime.createRow(1L, LocalTime.of(10, 0));
        Theme theme = Theme.createRow(1L, "공포", "설명", "https://good.com");
        reservationRepository.save(Reservation.create("인직", LocalDate.now().plusDays(1), time, theme));

        // when & then
        assertThatThrownBy(() -> themeValidator.validateNotReferencedByReservation(theme.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("예약이 연결된 테마는 삭제할 수 없습니다.");
    }
}
