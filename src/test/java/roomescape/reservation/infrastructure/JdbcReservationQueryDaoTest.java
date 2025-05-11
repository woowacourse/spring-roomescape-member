package roomescape.reservation.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_2;
import static roomescape.testFixture.Fixture.RESERVATION_3;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.DatabaseCleaner;
import roomescape.reservation.infrastructure.dto.ReservationDetailData;
import roomescape.testFixture.JdbcHelper;

@JdbcTest
@Import({JdbcReservationQueryDao.class, DatabaseCleaner.class})
@ActiveProfiles("test")
class JdbcReservationQueryDaoTest {

    @Autowired
    private JdbcReservationQueryDao reservationQueryDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void clean() {
        databaseCleaner.clean();
    }

    @DisplayName("모든 예약을 조회할 수 있다.")
    @Test
    void findAllTest() {
        // given
        JdbcHelper.insertReservations(jdbcTemplate, RESERVATION_1, RESERVATION_2, RESERVATION_3);

        // when
        List<ReservationDetailData> reservationDetails = reservationQueryDao.findAllReservationDetails();

        // then
        assertThat(reservationDetails).hasSize(3);
    }

    @DisplayName("예약 id로 해당 예약을 조회할 수 있다.")
    @Test
    void findById() {
        // given
        JdbcHelper.insertReservations(jdbcTemplate, RESERVATION_1);
        Long id = RESERVATION_1.getId();

        // when
        Optional<ReservationDetailData> result = reservationQueryDao.findReservationDetailById(id);

        // then
        assertThat(result).isPresent();
        ReservationDetailData reservationData = result.get();
        assertThat(reservationData.member().id()).isEqualTo(RESERVATION_1.getMemberId());
    }
}
