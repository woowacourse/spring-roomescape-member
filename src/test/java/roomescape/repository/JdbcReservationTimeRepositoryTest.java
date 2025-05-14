package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ReservationTime;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcReservationTimeRepository.class)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql")
@Sql(scripts = "/reservation-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcReservationTimeRepositoryTest {

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("예약 가능 시간 데이터를 저장한다")
    void insert() {
        //given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(20, 0));

        //when
        long actual = reservationTimeRepository.insert(reservationTime);

        //then
        assertThat(actual).isEqualTo(3L);
    }

    @ParameterizedTest
    @CsvSource(value = {"10:00,true", "20:00,false"})
    @DisplayName("startAt 을 기반으로 예약 가능 시간 데이터 존재 여부를 확인한다")
    void existsByStartAt(LocalTime startAt, boolean expected) {
        //when
        boolean actual = reservationTimeRepository.existsByStartAt(startAt);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하는 모든 예약 가능 시간을 조회한다")
    void findAll() {
        //when
        List<ReservationTime> actual = reservationTimeRepository.findAll();

        //then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("id를 기반으로 데이터를 조회한다")
    void findById() {
        //given
        long targetId = 1L;

        //when
        Optional<ReservationTime> actual = reservationTimeRepository.findById(targetId);

        //then
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 id를 기반으로 데이터를 조회하면 empty이다")
    void findByIdWhenNotExistedId() {
        //given
        long targetId = 100L;

        //when
        Optional<ReservationTime> actual = reservationTimeRepository.findById(targetId);

        //then
        assertThat(actual).isEmpty();
    }

    @ParameterizedTest
    @CsvSource(value = {"2025-01-01,1,1", "2025-01-02,1,0", "2025-01-01,2,0", "2025-01-02,2,0"})
    @DisplayName("특정 테마, 날짜에 예약이 존재하는 예약 가능 시간 데이터를 조회한다")
    void findAllBookedTime(LocalDate date, long themeId, int expected) {
        //when
        List<ReservationTime> actual = reservationTimeRepository.findAllBookedTime(date, themeId);

        //then
        assertThat(actual).hasSize(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"2,true", "100,false"})
    @DisplayName("대상 id가 존재할 경우 데이터를 삭제한다")
    void deleteById(long targetId, boolean expected) {
        //when
        boolean actual = reservationTimeRepository.deleteById(targetId);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
