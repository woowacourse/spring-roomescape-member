package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.time.ReservationTime;
import roomescape.domain.time.ReservationTimeRepository;
import roomescape.fixture.ReservationTimeFixture;

@JdbcTest
@Sql("/reservation-time.sql")
class JdbcReservationTimeRepositoryTest {
    private final ReservationTimeRepository reservationTimeRepository;

    @Autowired
    JdbcReservationTimeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    @Test
    void 예약_시간을_저장한다() {
        ReservationTime reservationTime = ReservationTimeFixture.reservationTime("23:00");

        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        assertThat(savedReservationTime.getStartAt()).isEqualTo(reservationTime.getStartAt());
    }

    @Test
    void 모든_예약_시간을_조회한다() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        assertThat(reservationTimes).hasSize(2);
    }

    @Test
    void 예약_시간을_삭제한다() {
        boolean isDeleted = reservationTimeRepository.deleteById(2L);

        assertThat(isDeleted).isTrue();
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_0을_반환한다() {
        boolean isDeleted = reservationTimeRepository.deleteById(0L);

        assertThat(isDeleted).isFalse();
    }

    @Test
    @Sql("/reservation.sql")
    void 테마의_예약된_시간_아이디를_조회한다() {
        Set<Long> reservedTimeIds = reservationTimeRepository.findReservedTimeIds(3L, LocalDate.parse("2024-05-01"));

        assertAll(
                () -> assertThat(reservedTimeIds).hasSize(2),
                () -> assertThat(reservedTimeIds).containsExactly(2L, 3L)
        );
    }
}
