package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

@JdbcTest
class JdbcReservationTimeRepositoryImplTest {

    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    public JdbcReservationTimeRepositoryImplTest(JdbcTemplate jdbcTemplate) {
        this.reservationTimeRepository = new JdbcReservationTimeRepositoryImpl(jdbcTemplate);
    }

    @DisplayName("예약 시간 정보를 DB에 저장한다.")
    @Test
    void save() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.MIDNIGHT.toString());

        ReservationTime actual = reservationTimeRepository.save(reservationTime);
        ReservationTime expected = reservationTimeRepository.findById(actual.getId()).get();

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("id값을 통해 예약 시간 정보를 DB에서 조회한다.")
    @Test
    void findById() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.MIDNIGHT.toString());
        ReservationTime save = reservationTimeRepository.save(reservationTime);

        ReservationTime actual = reservationTimeRepository.findById(save.getId()).get();
        ReservationTime expected = new ReservationTime(save.getId(), save.getStartAt().toString());

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("id값이 db에 존재하지 않으면 optional.empty를 반환한다.")
    @Test
    void findById_NoSuchId() {
        Optional<ReservationTime> actual = reservationTimeRepository.findById(1L);

        assertThat(actual).isEqualTo(Optional.empty());
    }

    @DisplayName("id값을 예약 시간 정보를 DB에서 삭제한다.")
    @Test
    void deleteById() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.MIDNIGHT.toString());
        ReservationTime save = reservationTimeRepository.save(reservationTime);

        reservationTimeRepository.deleteById(save.getId());
        List<ReservationTime> actual = reservationTimeRepository.findAll();

        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("모든 예약 시간 정보를 DB에서 조회한다.")
    @Test
    void findAll() {
        ReservationTime reservationTime1 = new ReservationTime(LocalTime.MIDNIGHT.toString());
        ReservationTime save1 = reservationTimeRepository.save(reservationTime1);

        ReservationTime reservationTime2 = new ReservationTime(LocalTime.MIDNIGHT.toString());
        ReservationTime save2 = reservationTimeRepository.save(reservationTime2);

        List<ReservationTime> actual = reservationTimeRepository.findAll();
        List<ReservationTime> expected = List.of(save1, save2);

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
