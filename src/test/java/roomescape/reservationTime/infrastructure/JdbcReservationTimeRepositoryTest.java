package roomescape.reservationTime.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import roomescape.reservationTime.domain.ReservationTime;
import roomescape.reservationTime.domain.ReservationTimeRepository;

@JdbcTest
class JdbcReservationTimeRepositoryTest {

    @Autowired
    DataSource dataSource;
    ReservationTimeRepository repository;

    @BeforeEach
    void beforeEach() {
        repository = new JdbcReservationTimeRepository(dataSource);
    }

    @Test
    @DisplayName("저장 후 아이디 반환 테스트")
    void save_test() {
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 10));

        Long save = repository.save(reservationTime);

        assertThat(save).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("삭제 성공 관련 테스트")
    @CsvSource({"0,true", "1,false"})
    void delete_test(Long plus, boolean expected) {
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 10));
        Long save = repository.save(reservationTime);

        boolean isDeleted = repository.deleteBy(save + plus);

        assertThat(isDeleted).isEqualTo(expected);
    }

    @Test
    @DisplayName("전체 조회 테스트")
    void find_all_test() {
        ReservationTime reservationTime1 = ReservationTime.createWithoutId(LocalTime.of(10, 10));
        ReservationTime reservationTime2 = ReservationTime.createWithoutId(LocalTime.of(10, 11));
        ReservationTime reservationTime3 = ReservationTime.createWithoutId(LocalTime.of(10, 12));
        repository.save(reservationTime1);
        repository.save(reservationTime2);
        repository.save(reservationTime3);

        List<ReservationTime> reservations = repository.findAll();
        List<LocalTime> names = reservations.stream()
                .map(ReservationTime::getStartAt)
                .toList();

        assertAll(
                () -> assertThat(reservations).hasSize(3),
                () -> assertThat(names).contains(LocalTime.of(10, 10), LocalTime.of(10, 11), LocalTime.of(10, 12))
        );
    }

    @Test
    @DisplayName("아이디로 조회 테스트")
    void find_by_id() {
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 10));
        Long save = repository.save(reservationTime);

        ReservationTime findTime = repository.findBy(save);

        assertThat(reservationTime.getStartAt()).isEqualTo(findTime.getStartAt());
    }
}