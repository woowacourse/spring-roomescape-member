package roomescape.repository;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class JdbcReservationTimeRepositoryImplTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약 시간 정보를 DB에 저장한다.")
    @Test
    void save() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.MIDNIGHT);

        ReservationTime actual = reservationTimeRepository.save(reservationTime);
        ReservationTime expected = reservationTimeRepository.findById(actual.getId());

        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getStartAt(), actual.getStartAt())
        );
    }

    @DisplayName("id값을 통해 예약 시간 정보를 DB에서 조회한다.")
    @Test
    void findById() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.MIDNIGHT);
        ReservationTime save = reservationTimeRepository.save(reservationTime);

        ReservationTime actual = reservationTimeRepository.findById(save.getId());
        ReservationTime expected = new ReservationTime(save.getId(), save.getStartAt());

        assertAll(
                () -> assertEquals(expected.getId(), actual.getId()),
                () -> assertEquals(expected.getStartAt(), actual.getStartAt())
        );
    }

    @DisplayName("id값을 예약 시간 정보를 DB에서 삭제한다.")
    @Test
    void deleteById() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.MIDNIGHT);
        ReservationTime save = reservationTimeRepository.save(reservationTime);

        reservationTimeRepository.deleteById(save.getId());
        List<ReservationTime> actual = reservationTimeRepository.findAll();

        assertTrue(actual.isEmpty());
    }

    @DisplayName("모든 예약 시간 정보를 DB에서 조회한다.")
    @Test
    void findAll() {
        ReservationTime reservationTime1 = new ReservationTime(LocalTime.MIDNIGHT);
        ReservationTime save1 = reservationTimeRepository.save(reservationTime1);

        ReservationTime reservationTime2 = new ReservationTime(LocalTime.MIDNIGHT);
        ReservationTime save2 = reservationTimeRepository.save(reservationTime2);

        List<ReservationTime> actual = reservationTimeRepository.findAll();
        List<ReservationTime> expected = List.of(save1, save2);

        assertAll(
                () -> assertEquals(expected.get(0).getId(), actual.get(0).getId()),
                () -> assertEquals(expected.get(0).getStartAt(), actual.get(0).getStartAt()),
                () -> assertEquals(expected.get(1).getId(), actual.get(1).getId()),
                () -> assertEquals(expected.get(1).getStartAt(), actual.get(1).getStartAt())
        );
    }
}
