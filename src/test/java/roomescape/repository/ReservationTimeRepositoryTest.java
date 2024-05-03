package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ReservationTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import(H2ReservationTimeRepository.class)
@JdbcTest
class ReservationTimeRepositoryTest {

    final long LAST_ID = 4;
    final ReservationTime exampleFirstTime = new ReservationTime(1L, LocalTime.of(10, 15));

    @Autowired
    private ReservationTimeRepository timeRepository;

    @Test
    @DisplayName("모든 예약 시간 목록을 조회한다.")
    void findAll() {
        // given & when
        final List<ReservationTime> actual = timeRepository.findAll();

        // then
        assertThat(actual).hasSize((int) LAST_ID);
        assertThat(actual.get(0)).isEqualTo(exampleFirstTime);
    }

    @Test
    @DisplayName("특정 id를 통해 예약 시간 데이터를 조회한다.")
    void findByIdPresent() {
        // given & when
        final Optional<ReservationTime> actual = timeRepository.findById(exampleFirstTime.getId());

        // then
        assertThat(actual).hasValue(exampleFirstTime);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 데이터를 조회할 경우 빈 값을 반환한다.")
    void findByIdNotPresent() {
        // given & when
        final Optional<ReservationTime> actual = timeRepository.findById(LAST_ID + 1);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("예약 시간 정보를 저장하면 새로운 아이디가 부여된다.")
    void save() {
        // given
        final ReservationTime time = new ReservationTime(null, LocalTime.of(13, 30));

        // when
        final ReservationTime actual = timeRepository.save(time);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    @DisplayName("등록된 예약 시간 번호로 삭제한다.")
    void deletePresent() {
        // given
        final Long id = LAST_ID;

        // when & then
        assertThat(timeRepository.findById(id)).isPresent();
        assertThat(timeRepository.delete(id)).isNotZero();
        assertThat(timeRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 번호로 삭제할 경우 아무런 영향이 없다.")
    void deleteNotPresent() {
        // given
        final Long id = LAST_ID + 1;

        // when & then
        assertThat(timeRepository.findById(id)).isEmpty();
        assertThat(timeRepository.delete(id)).isZero();
    }
}
