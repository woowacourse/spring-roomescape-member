package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({JdbcReservationRepository.class, JdbcReservationTimeRepository.class})
@JdbcTest
class JdbcReservationRepositoryTest {
    private final ReservationRepository repository;
    private final ReservationTimeRepository timeRepository;

    @Autowired
    public JdbcReservationRepositoryTest(DataSource dataSource) {
        this.repository = new JdbcReservationRepository(dataSource);
        this.timeRepository = new JdbcReservationTimeRepository(dataSource);
    }

    @Test
    void 데이터_생성_테스트() {
        // given
        ReservationTime time = timeRepository.createReservationTime(new ReservationTime(null, LocalTime.parse("20:43")));
        Reservation reservation = new Reservation(null, "브라운", "2026-04-28", time);

        // when
        Long id = repository.createReservation(reservation);

        // then
        assertThat(id).isNotNull();

        List<Reservation> all = repository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getName().value()).isEqualTo("브라운");
    }

    @Test
    void 데이터_전체_조회_테스트() {
        // given
        ReservationTime time1 = timeRepository.createReservationTime(new ReservationTime(null, LocalTime.parse("13:43")));
        ReservationTime time2 = timeRepository.createReservationTime(new ReservationTime(null, LocalTime.parse("10:00")));
        repository.createReservation(new Reservation(null, "브라운", "2026-04-28", time1));
        repository.createReservation(new Reservation(null, "제임스", "2026-04-29", time2));

        // when
        List<Reservation> reservations = repository.findAll();

        // then
        assertThat(reservations).hasSize(2);
        assertThat(reservations)
                .extracting(Reservation::getName)
                .anySatisfy(name -> assertThat(name.value()).isEqualTo("브라운"))
                .anySatisfy(name -> assertThat(name.value()).isEqualTo("제임스"));

        assertThat(reservations)
                .extracting(Reservation::getTime)
                .anySatisfy(time -> assertThat(time).isEqualTo(time1))
                .anySatisfy(time -> assertThat(time).isEqualTo(time2));
    }

    @Test
    void 데이터_삭제_테스트() {
        // given
        ReservationTime time = timeRepository.createReservationTime(new ReservationTime(null, LocalTime.parse("20:40")));
        Long id = repository.createReservation(new Reservation(null, "브라운", "2026-04-28", time));
        assertThat(repository.findAll()).hasSize(1);

        // when
        repository.deleteById(id);

        // then
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void 아이디로_특정_데이터_조회_테스트() {
        ReservationTime time = timeRepository.createReservationTime(new ReservationTime(null, LocalTime.parse("16:00")));
        Reservation test = new Reservation(null, "브라운", "2026-04-29", time);
        Long id = repository.createReservation(test);

        // when
        Reservation target = repository.findById(id);

        // then
        assertThat(target.getId()).isEqualTo(id);
        assertThat(target.getName()).isEqualTo(test.getName());
        assertThat(target.getDate()).isEqualTo(test.getDate());
        assertThat(target.getTime()).isEqualTo(test.getTime());
    }
}