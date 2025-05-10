package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.domain.ReservationTime;
import roomescape.infrastructure.persistance.JdbcReservationTimeRepository;

class JdbcReservationTimeRepositoryTest extends JdbcSupportTest {

    private final JdbcReservationTimeRepository reservationTimeRepository = new JdbcReservationTimeRepository(
            new NamedParameterJdbcTemplate(TEST_DATASOURCE), TEST_DATASOURCE);

    @Test
    void 예약_시간을_저장할_수_있다() {
        //when
        Long createdId = reservationTimeRepository.create(new ReservationTime(LocalTime.of(12, 1)));

        //then
        assertThat(reservationTimeRepository.findById(createdId))
                .hasValue(new ReservationTime(1L, LocalTime.of(12, 1)));
    }

    @Test
    void id로_예약_시간을_조회할_수_있다() {
        //given
        insertReservationTime(LocalTime.of(12, 0));

        //when
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(1L);

        //then
        assertThat(reservationTime).hasValue(new ReservationTime(1L, LocalTime.of(12, 0)));
    }

    @Test
    void id값이_없다면_빈_Optional_값이_반환된다() {
        //when
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(1L);

        //then
        assertThat(reservationTime).isEmpty();
    }

    @Test
    void 전체_예약_시간을_조회할_수_있다() {
        //given
        insertReservationTime(LocalTime.of(12, 0));
        insertReservationTime(LocalTime.of(12, 1));

        //when
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        //then
        assertThat(reservationTimes).isEqualTo(List.of(
                new ReservationTime(1L, LocalTime.of(12, 0)),
                new ReservationTime(2L, LocalTime.of(12, 1))
        ));
    }

    @Test
    void id값으로_예약_시간을_삭제한다() {
        //given
        insertReservationTime(LocalTime.of(12, 0));

        //when
        reservationTimeRepository.deleteById(1L);

        //then
        assertThat(reservationTimeRepository.findById(1L)).isEmpty();
    }
}