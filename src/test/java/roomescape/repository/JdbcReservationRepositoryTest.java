package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;
import roomescape.domain.MemberRoleType;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.repository.criteria.ReservationCriteria;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcReservationRepository.class)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql")
@Sql(scripts = "/reservation-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcReservationRepositoryTest {

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    @DisplayName("예약 데이터를 저장한다")
    void insert() {
        //given
        Reservation reservation = new Reservation(
                new Member(1, "test", "test@email.com", "1234", MemberRoleType.MEMBER),
                LocalDate.of(2100, 1, 1),
                new ReservationTime(1, LocalTime.of(10, 0)),
                new RoomTheme(1, "test", "test2", "test3"));

        //when
        long actual = reservationRepository.insert(reservation);

        //then
        assertThat(actual).isEqualTo(2L);
    }

    @ParameterizedTest
    @CsvSource(value = {"2025-01-01,1,1,true", "2025-01-02,1,1,false"})
    @DisplayName("같은 테마, 시간인 예약이 존재하는지 확인한다")
    void existSameReservation(LocalDate date, long timeId, long themeId, boolean expected) {
        //when
        boolean actual = reservationRepository.existSameReservation(date, timeId, themeId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("저장된 모든 데이터를 조회한다")
    void findAll() {
        //when
        List<Reservation> actual = reservationRepository.findAll();

        //then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("id를 기반으로 데이터를 조회한다")
    void findById() {
        //given
        long targetId = 1L;

        //when
        Optional<Reservation> actual = reservationRepository.findById(targetId);

        //then
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 id를 기반으로 데이터를 조회하면 empty이다")
    void findByIdWhenNotExistedId() {
        //given
        long targetId = 100L;

        //when
        Optional<Reservation> actual = reservationRepository.findById(targetId);

        //then
        assertThat(actual).isEmpty();
    }

    @ParameterizedTest
    @CsvSource(value = {"1,true", "100,false"})
    @DisplayName("timeId를 기반으로 데이터가 존재하는지 확인한다")
    void existsByTimeId(long targetId, boolean expected) {
        //when
        boolean actual = reservationRepository.existsByTimeId(targetId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,true", "100,false"})
    @DisplayName("themeId를 기반으로 데이터가 존재하는지 확인한다")
    void existsByThemeId(long targetId, boolean expected) {
        //when
        boolean actual = reservationRepository.existsByThemeId(targetId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"1,true", "100,false"})
    @DisplayName("대상 id가 존재할 경우 데이터를 삭제한다")
    void deleteById(long targetId, boolean expected) {
        //when
        boolean actual = reservationRepository.deleteById(targetId);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> provideCriteria() {
        return Stream.of(
                Arguments.of(new ReservationCriteria(1L, 1L,
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 2, 1)), 2),

                Arguments.of(new ReservationCriteria(null, 1L,
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 2, 1)), 5),

                Arguments.of(new ReservationCriteria(1L, null,
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 2, 1)), 5),
                Arguments.of(new ReservationCriteria(1L, 1L, null, LocalDate.of(2025, 1, 2)), 2),
                Arguments.of(new ReservationCriteria(1L, 1L, LocalDate.of(2025, 1, 2), null), 1),
                Arguments.of(new ReservationCriteria(null, null, null, null), 10),
                Arguments.of(new ReservationCriteria(null, null,
                        LocalDate.of(2025, 1, 2), LocalDate.of(2025, 1, 3)), 4),
                Arguments.of(new ReservationCriteria(2L, 1L,
                        LocalDate.of(2025, 1, 1), LocalDate.of(2025, 2, 1)), 3)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCriteria")
    @Sql(scripts = {"/schema.sql", "/reservation-criteria-test.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("필터 기준을 기반으로 예약 데이터를 조회한다")
    void findAllByCriteria(ReservationCriteria criteria, int expectedSize) {
        //when
        List<Reservation> actual = reservationRepository.findAllByCriteria(criteria);

        //then
        assertThat(actual).hasSize(expectedSize);
    }
}
