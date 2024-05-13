package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.ReservationTime;
import roomescape.dto.reservationtime.ReservationTimeRequest;
import roomescape.fixture.ThemeFixture;
import roomescape.support.extension.TableTruncateExtension;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(TableTruncateExtension.class)
public class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void 예약_시간을_성공적으로_등록한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);

        ReservationTime reservationTime = reservationTimeService.register(request);

        assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 중복된_예약_시간이_있으면_등록을_실패한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);
        reservationTimeService.register(request);

        assertThatThrownBy(() -> reservationTimeService.register(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 예약 시간입니다.");
    }

    @Test
    void 예약_시간을_삭제한다() {
        LocalTime startAt = LocalTime.of(13, 0);
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);
        ReservationTime reservationTime = reservationTimeService.register(request);

        reservationTimeService.delete(reservationTime.getId());

        assertThat(reservationTimeService.getReservationTimes()).isEmpty();
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_예외가_발생한다() {
        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 예약 시간입니다.");
    }

    @Test
    void 특정_시간의_예약과_같은_시간을_삭제했을_때_예외가_발생한다() {
        LocalTime startAt = LocalTime.now();
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);

        ReservationTime reservationTime = reservationTimeService.register(request);
        Theme theme = themeRepository.save(ThemeFixture.theme());
        reservationRepository.save(
                new Reservation(
                        "prin", LocalDate.now().plusDays(2), reservationTime, theme
                )
        );

        Long id = reservationTime.getId();
        assertThatThrownBy(() -> reservationTimeService.delete(id))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 시간을 사용하는 예약이 존재합니다");
    }
}
