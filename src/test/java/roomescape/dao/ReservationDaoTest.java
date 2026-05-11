package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.Reservation;

class ReservationDaoTest extends DaoTest {

    @Autowired
    private ReservationDao reservationDao;

    @Test
    void findAll_전체_예약_조회() {
        List<Reservation> reservations = reservationDao.findAll();

        // data.sql 기준 19건 (공포의 저택 5 + 탐정 사무소 4 + 마법사의 연구실 3 + 우주 정거장 2 + 미래 예약 5)
        assertThat(reservations).hasSize(19);
    }

    @Test
    void findByName_이름으로_예약_조회() {
        List<Reservation> reservations = reservationDao.findByName("김철수");

        assertThat(reservations).hasSize(4);
        assertThat(reservations).allMatch(r -> r.getName().equals("김철수"));
    }

    @Test
    void findByName_없는_이름이면_빈_목록() {
        List<Reservation> reservations = reservationDao.findByName("없는사람");

        assertThat(reservations).isEmpty();
    }

    @Test
    void save_예약_저장() {
        long id = reservationDao.save("브라운", LocalDate.of(2026, 12, 31), 1L, 1L);

        assertThat(id).isEqualTo(20L);
    }

    @Test
    void delete_예약_삭제() {
        reservationDao.delete(1L);

        assertThat(reservationDao.findAll()).hasSize(18);
    }

    @Test
    void existsByTimeId_사용중이면_true() {
        // time_id=3 (12:00)은 여러 예약에서 사용 중
        assertThat(reservationDao.existsByTimeId(3L)).isTrue();
    }

    @Test
    void existsByTimeId_미사용이면_false() {
        // time_id=12 (21:00)은 data.sql에 예약 없음
        assertThat(reservationDao.existsByTimeId(12L)).isFalse();
    }

    @Test
    void existsByThemeId_사용중이면_true() {
        assertThat(reservationDao.existsByThemeId(1L)).isTrue();
    }

    @Test
    void existsByThemeId_미사용이면_false() {
        // theme_id=99는 존재하지 않으므로 예약도 없음
        assertThat(reservationDao.existsByThemeId(99L)).isFalse();
    }

    @Test
    void existsByDateAndTimeIdAndThemeId_존재하면_true() {
        // data.sql 첫 번째 예약: 김철수, 2026-04-29, time_id=3, theme_id=1
        assertThat(reservationDao.existsByDateAndTimeIdAndThemeId(LocalDate.of(2026, 4, 29), 3L, 1L)).isTrue();
    }

    @Test
    void existsByDateAndTimeIdAndThemeId_없으면_false() {
        assertThat(reservationDao.existsByDateAndTimeIdAndThemeId(LocalDate.of(2026, 12, 31), 3L, 1L)).isFalse();
    }
}
