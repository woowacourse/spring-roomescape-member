package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock private ReservationDao reservationDao;
    @Mock private ReservationTimeDao reservationTimeDao;
    @Mock private ThemeDao themeDao;
    @InjectMocks private ReservationService reservationService;

    private final ReservationTime sampleTime = new ReservationTime(1L, LocalTime.of(10, 0));
    private final Theme sampleTheme = new Theme(1L, "공포의 저택", "버려진 저택에서 탈출하라!", "https://example.com/img.jpg");

    @Test
    void save_정상_예약_저장() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        given(reservationTimeDao.findById(1L)).willReturn(Optional.of(sampleTime));
        given(themeDao.findById(1L)).willReturn(Optional.of(sampleTheme));
        given(reservationDao.existsByDateAndTimeIdAndThemeId(futureDate, 1L, 1L)).willReturn(false);
        given(reservationDao.save(any(Reservation.class)))
                .willReturn(Reservation.restore(10L, "브라운", futureDate, LocalDate.now(), sampleTime, sampleTheme));

        Reservation result = reservationService.save("브라운", futureDate, 1L, 1L);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getName()).isEqualTo("브라운");
        assertThat(result.getDate()).isEqualTo(futureDate);
    }

    @Test
    void save_존재하지_않는_시간이면_예외() {
        given(reservationTimeDao.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.save("브라운", LocalDate.now().plusDays(1), 99L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @Test
    void save_존재하지_않는_테마이면_예외() {
        given(reservationTimeDao.findById(1L)).willReturn(Optional.of(sampleTime));
        given(themeDao.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.save("브라운", LocalDate.now().plusDays(1), 1L, 99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    @Test
    void save_이미_예약된_시간이면_예외() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        given(reservationTimeDao.findById(1L)).willReturn(Optional.of(sampleTime));
        given(themeDao.findById(1L)).willReturn(Optional.of(sampleTheme));
        given(reservationDao.existsByDateAndTimeIdAndThemeId(futureDate, 1L, 1L)).willReturn(true);

        assertThatThrownBy(() -> reservationService.save("브라운", futureDate, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 예약된 시간입니다.");
    }

    @Test
    void delete_정상_삭제() {
        reservationService.delete(1L);

        then(reservationDao).should().delete(1L);
    }

}
