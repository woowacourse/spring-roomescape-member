package roomescape.reservationtime.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeDao;

class ReservationTimeDaoTest {

    private static ReservationTimeDao reservationTimeDao;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        reservationTimeDao = new ReservationTimeDao(dataSource);
    }

    @Test
    void 예약시간_저장() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(23, 59));
        ReservationTime saved = reservationTimeDao.save(reservationTime);
        List<ReservationTime> all = reservationTimeDao.findAll();

        assertThat(all).hasSize(13);
        assertThat(all.getLast().getId()).isEqualTo(saved.getId());
        assertThat(all.getLast().getStartAt()).isEqualTo(saved.getStartAt());
    }

    @Test
    void 예약시간_삭제() {
        boolean isDeleted = reservationTimeDao.deleteById(4L);

        List<ReservationTime> all = reservationTimeDao.findAll();
        assertThat(isDeleted).isTrue();
        assertThat(all).hasSize(11);
    }

    @Test
    void 삭제할_id가_없는_경우() {
        // given
        Long id = 999L;

        // when
        boolean isDeleted = reservationTimeDao.deleteById(id);

        // then
        List<ReservationTime> all = reservationTimeDao.findAll();
        assertThat(isDeleted).isFalse();
        assertThat(all).hasSize(12);
    }

    @Test
    void id로_예약시간_조회() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(10, 0));
        ReservationTime saved = reservationTimeDao.save(reservationTime);

        Optional<ReservationTime> foundReservationTime = reservationTimeDao.findById(saved.getId());
        assertThat(foundReservationTime).isPresent();
        assertThat(foundReservationTime.get().getId()).isEqualTo(saved.getId());
        assertThat(foundReservationTime.get().getStartAt()).isEqualTo(saved.getStartAt());
    }

    @Test
    void id_로_조회_시_예약_시간이_없는_경우() {
        // given
        Long id = 99L;

        // when
        Optional<ReservationTime> reservationTime = reservationTimeDao.findById(id);

        // then
        assertThat(reservationTime).isEmpty();
    }

    @Test
    void 모든_예약_시간_반환() {
        // when
        List<ReservationTime> all = reservationTimeDao.findAll();

        // then
        assertThat(all).hasSize(12);
    }

    @Test
    void 예약시간_중복_여부_확인() {
        // given
        LocalTime existTime = LocalTime.of(13, 40);
        LocalTime nonExistTime = LocalTime.of(0, 1);

        // when
        boolean exist = reservationTimeDao.isExistTime(existTime);
        boolean nonExist = reservationTimeDao.isExistTime(nonExistTime);

        // then
        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }

    @Test
    void 존재하는_모든_시간을_카운팅() {
        // when
        int count = reservationTimeDao.countTotalReservationTimes();

        // then
        assertThat(count).isEqualTo(12);
    }

    @Test
    void 시작부터_끝을_설정해_예약시간을_반환() {
        // given
        int start = 3;
        int end = 10;

        // when
        List<ReservationTime> reservationTimesWithPage = reservationTimeDao.findReservationTimesWithPage(start, end);

        // then
        assertThat(reservationTimesWithPage).hasSize(8);
        assertThat(reservationTimesWithPage.getFirst().getId()).isEqualTo(3L);
        assertThat(reservationTimesWithPage.getLast().getId()).isEqualTo(10L);
    }
}
