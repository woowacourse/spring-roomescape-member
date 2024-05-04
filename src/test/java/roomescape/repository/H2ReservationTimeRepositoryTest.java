package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import roomescape.BasicAcceptanceTest;
import roomescape.TestFixtures;
import roomescape.domain.ReservationTime;

@Sql(value = "/setReservationTime.sql")
class H2ReservationTimeRepositoryTest extends BasicAcceptanceTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약 시간을 DB에 저장한다.")
    @Test
    void save() {
        reservationTimeRepository.save(TestFixtures.THIRD_TIME);

        List<ReservationTime> expected = List.of(TestFixtures.FIRST_TIME, TestFixtures.SECOND_TIME, TestFixtures.THIRD_TIME);

        assertThat(reservationTimeRepository.findAll()).isEqualTo(expected);
    }

    @DisplayName("DB에 저장된 전체 예약 시간을 반환한다.")
    @Test
    void findAll() {
        List<ReservationTime> reservationTimes = List.of(TestFixtures.FIRST_TIME, TestFixtures.SECOND_TIME);

        assertThat(reservationTimeRepository.findAll()).isEqualTo(reservationTimes);
    }

    @DisplayName("해당 id의 예약 시간을 반환한다.")
    @Test
    void findById() {
        assertThat(reservationTimeRepository.findById(1L).orElseThrow()).isEqualTo(TestFixtures.FIRST_TIME);
    }

    @DisplayName("해당 id의 예약 시간을 삭제한다.")
    @Test
    void deleteById() {
        reservationTimeRepository.deleteById(1L);

        assertThat(reservationTimeRepository.findAll()).isEqualTo(List.of(TestFixtures.SECOND_TIME));
    }
}
