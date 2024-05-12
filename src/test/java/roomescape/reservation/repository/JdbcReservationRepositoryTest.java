package roomescape.reservation.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.fixture.Fixture;
import roomescape.member.repositoy.JdbcMemberRepository;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.theme.repository.JdbcThemeRepository;

@JdbcTest
@Sql("/delete-data.sql")
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private JdbcReservationRepository reservationRepository;
    private JdbcReservationTimeRepository reservationTimeRepository;
    private JdbcThemeRepository themeRepository;
    private JdbcMemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        reservationRepository = new JdbcReservationRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        reservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        themeRepository = new JdbcThemeRepository(jdbcTemplate, jdbcTemplate.getDataSource());
        memberRepository = new JdbcMemberRepository(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void save() {
        memberRepository.save(Fixture.MEMBER_1);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);

        reservationRepository.save(Fixture.RESERVATION_1);

        assertThat(reservationRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.RESERVATION_1));
    }

    @Test
    @DisplayName("Reservation 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        memberRepository.save(Fixture.MEMBER_1);
        memberRepository.save(Fixture.MEMBER_2);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);
        reservationRepository.save(Fixture.RESERVATION_1);
        reservationRepository.save(Fixture.RESERVATION_2);

        assertThat(reservationRepository.findAll())
                .containsExactly(
                        Fixture.RESERVATION_1,
                        Fixture.RESERVATION_2);
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        memberRepository.save(Fixture.MEMBER_1);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        reservationRepository.save(Fixture.RESERVATION_1);

        assertThat(reservationRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.RESERVATION_1));
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(reservationRepository.findById(20L))
                .isNotPresent();
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 time_id와 동일한 데이터를 조회한다.")
    void findAllByTimeId() {
        memberRepository.save(Fixture.MEMBER_1);
        memberRepository.save(Fixture.MEMBER_2);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);
        reservationRepository.save(Fixture.RESERVATION_1);
        reservationRepository.save(Fixture.RESERVATION_2);

        assertThat(reservationRepository.findAllByTimeId(1L))
                .containsExactly(
                        Fixture.RESERVATION_1,
                        Fixture.RESERVATION_2);
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 time_id 가 없는 경우 빈 리스트를 반환한다.")
    void findAllByTimeId_Return_EmptyCollection() {
        assertThat(reservationRepository.findAllByTimeId(99999L)).isEmpty();
    }

    @Test
    @DisplayName("주어진 theme_id와 동일한 예약들을 조회한다.")
    void findAllByThemeId() {
        memberRepository.save(Fixture.MEMBER_1);
        memberRepository.save(Fixture.MEMBER_2);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);
        reservationRepository.save(Fixture.RESERVATION_1); // theme_id: 1
        reservationRepository.save(Fixture.RESERVATION_2); // theme_id: 2


        assertThat(reservationRepository.findAllByThemeId(1L))
                .containsExactly(Fixture.RESERVATION_1);
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 theme_id 가 없는 경우 빈 리스트를 반환한다.")
    void findAllByThemeId_Return_EmptyCollection() {
        assertThat(reservationRepository.findAllByThemeId(99999L))
                .isEmpty();
    }

    @Test
    @DisplayName("주어진 date, theme_id와 동일한 예약들을 조회한다.")
    void findAllByDateAndThemeId() {
        memberRepository.save(Fixture.MEMBER_1);
        memberRepository.save(Fixture.MEMBER_2);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);
        reservationRepository.save(Fixture.RESERVATION_1);
        reservationRepository.save(Fixture.RESERVATION_2);
        Reservation reservation = Fixture.RESERVATION_1;
        LocalDate date = reservation.getDate();
        Long themeId = reservation.getTheme().getId();

        assertThat(reservationRepository.findAllByDateAndThemeId(date, themeId))
                .containsExactly(reservation);
    }

    @Test
    @DisplayName("주어진 date, theme_id 이 일치하는 예약이 없는 경우 빈 리스트를 반환한다.")
    void findAllByDateAndThemeId_Return_EmptyCollection() {
        assertThat(reservationRepository.findAllByDateAndThemeId(LocalDate.of(1, 1, 1), 99999L))
                .isEmpty();
    }

    @Test
    @DisplayName("날짜와 시간 컬럼의 값이 동일할 경우 참을 반환한다.")
    void existsByDateAndTime_whenSameName() {
        memberRepository.save(Fixture.MEMBER_1);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        reservationRepository.save(Fixture.RESERVATION_1);

        assertTrue(reservationRepository.existsByDateAndTimeAndTheme(
                Fixture.RESERVATION_1.getDate(),
                Fixture.RESERVATION_1.getReservationTime().getId(),
                Fixture.RESERVATION_1.getTheme().getId()));
    }

    @Test
    @DisplayName("날짜 또는 시간 중 하나라도 다를 경우 거짓을 반환한다.")
    void existsByDateAndTime_isFalse() {
        assertFalse(reservationRepository.existsByDateAndTimeAndTheme(
                Fixture.RESERVATION_1.getDate(),
                1L,
                3L));
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        reservationRepository.deleteById(2L);
        assertThat(reservationRepository.findById(2L))
                .isNotPresent();
    }
}
