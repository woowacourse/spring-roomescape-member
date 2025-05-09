package roomescape.reservation.application;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.AlreadyExistException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.ReservationTimeRepository;
import roomescape.reservation.infrastructure.JdbcReservationRepository;
import roomescape.reservation.infrastructure.JdbcReservationTimeRepository;
import roomescape.reservation.ui.dto.CreateReservationTimeRequest;

@JdbcTest
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @ParameterizedTest
    @CsvSource(value = {
            "10:00", "22:00"
    })
    void 예약시간을_추가한다(final LocalTime startAt) {
        // given
        final CreateReservationTimeRequest request = new CreateReservationTimeRequest(startAt);

        // when & then
        Assertions.assertThatCode(() -> reservationTimeService.create(request))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약시간을_삭제한다() {
        // given
        final LocalTime startAt = LocalTime.of(20, 28);
        final Long id = reservationTimeRepository.save(new ReservationTime(startAt));

        // when & then
        Assertions.assertThatCode(() -> reservationTimeService.delete(id))
                .doesNotThrowAnyException();
    }

    @Test
    void 이미_존재하는_예약시간을_추가하면_예외가_발생한다() {
        // given
        final LocalTime startAt = LocalTime.of(19, 55);
        reservationTimeRepository.save(new ReservationTime(startAt));

        final CreateReservationTimeRequest request = new CreateReservationTimeRequest(startAt);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationTimeService.create(request))
                .isInstanceOf(AlreadyExistException.class);
    }

    // TODO: 테스트 추가: 해당_예약_시간으로_등록된_예약이_있으면_삭제할_수_없다

    @Test
    void 삭제할_예약시간이_없으면_예외가_발생한다() {
        // given
        final Long id = Long.MAX_VALUE;

        // when & then
        Assertions.assertThatThrownBy(() -> reservationTimeService.delete(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public ReservationTimeRepository reservationTimeRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new JdbcReservationTimeRepository(jdbcTemplate);
        }

        @Bean
        public ReservationRepository reservationRepository(
                final JdbcTemplate jdbcTemplate
        ) {
            return new JdbcReservationRepository(jdbcTemplate);
        }

        @Bean
        public ReservationTimeService reservationTimeService(
                final ReservationTimeRepository reservationTimeRepository
        ) {
            return new ReservationTimeService(reservationTimeRepository);
        }
    }
}
