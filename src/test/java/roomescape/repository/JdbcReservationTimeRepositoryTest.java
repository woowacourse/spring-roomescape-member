package roomescape.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@Import(JdbcReservationTimeRepository.class)
@JdbcTest
class JdbcReservationTimeRepositoryTest {
    private final ReservationTimeRepository repository;

    @Autowired
    public JdbcReservationTimeRepositoryTest(DataSource dataSource) {
        this.repository = new JdbcReservationTimeRepository(dataSource);
    }

    @Test
    void 시간_데이터_생성_테스트() {
        // given
        ReservationTime reservationTime = repository.createReservationTime(new ReservationTime(null, "16:20"));
        assertThat(reservationTime).isNotNull();

        // when
        List<ReservationTime> all = repository.findAll();

        // then
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getId()).isEqualTo(reservationTime.getId());
        assertThat(all.get(0).getStartAt()).isEqualTo(reservationTime.getStartAt());
    }

    @Test
    void 시간_데이터_전체_조회_테스트() {
        // given
        ReservationTime time1 = new ReservationTime(null, "20:43");
        ReservationTime time2 = new ReservationTime(null, "10:00");

        repository.createReservationTime(time1);
        repository.createReservationTime(time2);

        // when
        List<ReservationTime> times = repository.findAll();

        //then
        assertThat(times).hasSize(2);
        assertThat(times)
                .extracting(ReservationTime::getStartAt)
                .anySatisfy(getStartAt -> assertThat(getStartAt).isEqualTo(time1.getStartAt()))
                .anySatisfy(getStartAt -> assertThat(getStartAt).isEqualTo(time2.getStartAt()));
    }

    @Test
    void 시간_데이터_삭제_테스트() {
        // given
        ReservationTime reservationTime = repository.createReservationTime(new ReservationTime(null, "20:43"));
        List<ReservationTime> all = repository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.getFirst().getId()).isEqualTo(reservationTime.getId());

        // when, then
        assertThatNoException().isThrownBy(() -> repository.deleteById(reservationTime.getId()));
        assertThat(repository.findAll()).hasSize(0);
    }

    @Test
    void 아이디로_특정_데이터_조회_테스트() {
        // given
        ReservationTime test = repository.createReservationTime(new ReservationTime(null, "16:20"));
        Long id = test.getId();

        // when
        ReservationTime target = repository.findById(id);

        // then
        assertThat(target.getStartAt()).isEqualTo(test.getStartAt());
    }
}