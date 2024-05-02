package roomescape.repository.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialDataFixture.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.InitialDataFixture;
import roomescape.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ReservationTimeH2RepositoryTest {

    @Autowired
    private ReservationTimeH2Repository reservationTimeH2Repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("ReservationTime을 저장한다.")
    void save() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(12, 0));

        ReservationTime saved = reservationTimeH2Repository.save(reservationTime);

        assertThat(saved.id()).isNotNull();
    }

    @Test
    @DisplayName("중복된 시간을 저장하려고 하면 예외가 발생한다.")
    void saveDuplicatedGetTime() {
        assertThatThrownBy(() -> reservationTimeH2Repository.save(RESERVATION_TIME_1))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("id에 맞는 ReservationTime을 제거한다.")
    void delete() {
        reservationTimeH2Repository.delete(RESERVATION_TIME_2.id());

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);

        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("참조되어 있는 시간을 삭제하는 경우 예외가 발생한다.")
    void deleteReferencedGetTime() {
        assertThatThrownBy(() -> reservationTimeH2Repository.delete(RESERVATION_TIME_1.id()))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("모든 ReservationTime을 찾는다.")
    void findAll() {
        List<ReservationTime> found = reservationTimeH2Repository.findAll();

        assertThat(found).hasSize(3);
    }

    @Test
    @DisplayName("id에 맞는 ReservationTime을 찾는다.")
    void findBy() {
        ReservationTime found = reservationTimeH2Repository.findById(RESERVATION_TIME_1.id()).get();

        assertThat(found.startAt()).isEqualTo(RESERVATION_TIME_1.startAt());
    }

    @Test
    @DisplayName("존재하지 않는 id가 들어오면 빈 Optional 객체를 반환한다.")
    void findEmpty() {
        Optional<ReservationTime> reservationTime = reservationTimeH2Repository.findById(-1L);

        assertThat(reservationTime.isEmpty()).isTrue();
    }
}
