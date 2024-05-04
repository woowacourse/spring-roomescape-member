package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.model.ReservationTime;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeRepositoryTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약 시간 저장")
    @Test
    void save() {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime( LocalTime.parse("10:30")));
        final ReservationTime savedReservationTime = reservationTimeRepository.findById(reservationTime.getId()).get();
        assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.parse("10:30"));
    }

    @DisplayName("존재하는 예약 시간 조회")
    @Test
    void findExistById() {
        final ReservationTime reservationTime = reservationTimeRepository.findById(1L).orElseThrow();
        assertAll(
                () -> assertThat(reservationTime.getId()).isEqualTo(1L),
                () -> assertThat(reservationTime.getStartAt()).isEqualTo(LocalTime.parse("09:00"))
        );
    }

    @DisplayName("존재하지 않는 예약 시간 조회")
    @Test
    void findEmptyById() {
        final Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(14L);
        assertTrue(reservationTime.isEmpty());
    }

    @DisplayName("예약 시간 목록 조회")
    @Test
    void findAll() {
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        assertThat(reservationTimes.size()).isEqualTo(7);
    }

    @DisplayName("예약 시간 삭제")
    @Test
    void deleteExistById() {
        reservationTimeRepository.deleteById(5L);
        assertThat(reservationTimeRepository.findById(5L)).isEmpty();
    }

    @DisplayName("특정 시간 존재 여부 확인")
    @ParameterizedTest
    @MethodSource("getStartAtWithExist")
    void existByStartAtFalse(final String startAt, final boolean expectedResult) {
        final boolean existByStartAt = reservationTimeRepository.existByStartAt(LocalTime.parse(startAt));

        assertThat(existByStartAt).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> getStartAtWithExist() {
        return Stream.of(
                Arguments.of("10:00", true),
                Arguments.of("14:20", false)
        );
    }
}
