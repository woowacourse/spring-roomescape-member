package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.common.KeyHolderManager;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.fixture.ReservationFixture;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.repository.JdbcReservationTimeRepository;
import roomescape.theme.ThemeTestDataConfig;
import roomescape.theme.repository.JdbcThemeRepository;
import roomescape.user.MemberTestDataConfig;
import roomescape.user.repository.JdbcUserRepository;

@JdbcTest
@Import({JdbcReservationRepository.class,
        JdbcReservationTimeRepository.class,
        JdbcThemeRepository.class,
        ThemeTestDataConfig.class,
        MemberTestDataConfig.class,
        KeyHolderManager.class,
        JdbcUserRepository.class
})
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcReservationRepository reservationRepository;
    @Autowired
    private JdbcReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeTestDataConfig themeTestDataConfig;
    @Autowired
    private MemberTestDataConfig memberTestDataConfig;

    private Reservation createReservation(int plusDays, ReservationTime time) {
        LocalDate date = LocalDate.now().plusDays(plusDays);
        return ReservationFixture.create(date, time, themeTestDataConfig.getSavedTheme(),
                memberTestDataConfig.getSavedMember());
    }

    @DisplayName("예약 시간에 해당하는 예약의 존재 여부를 알 수 있다.")
    @Test
    void existsByReservationTime() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.add(new ReservationTime(LocalTime.now()));

        Reservation reservation = createReservation(1, reservationTime);
        reservationRepository.add(reservation);

        // when
        boolean actual = reservationRepository.existsByReservationTime(reservationTime);

        // then
        assertThat(actual).isTrue();
    }
}
