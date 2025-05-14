package roomescape.repository.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.test.fixture.DateFixture.NEXT_DAY;
import static roomescape.test.fixture.DateFixture.TODAY;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.other.ReservationSearchCondition;

@JdbcTest
@Import(H2ReservationRepository.class)
class H2ReservationRepositoryTest {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private H2ReservationRepository reservationRepository;

    @BeforeEach
    void setup() {
        template.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("모든 예약을 조회할 수 있다")
    @Test
    void canFindAll() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원", "test@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);

        // when
        List<Reservation> actualReservations = reservationRepository.findAll();

        // then
        assertThat(actualReservations).hasSize(3);
    }

    @DisplayName("필터를 통해 특정 회원의 예약을 검색할 수 있다")
    @Test
    void canFindAllByMemberFilter() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원1", "test1@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원2", "test2@test.com", "fghf123!", MemberRole.GENERAL.toString());

        template.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");

        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                2L, NEXT_DAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                2L, NEXT_DAY.toString(), 1L, 1L);

        // when & then
        assertAll(
                () -> assertThat(reservationRepository.findAllByFilter(
                        new ReservationSearchCondition(1L, 1L, LocalDate.now(), NEXT_DAY)))
                        .hasSize(1),
                () -> assertThat(reservationRepository.findAllByFilter(
                        new ReservationSearchCondition(1L, 2L, LocalDate.now(), NEXT_DAY)))
                        .hasSize(2)
        );
    }

    @DisplayName("필터를 통해 특정 테마의 예약을 검색할 수 있다")
    @Test
    void canFindAllByThemeFilter() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원1", "test1@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.of(10, 0));

        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마2", "설명2", "썸네일2");

        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 2L);
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 2L);

        // when & then
        assertAll(
                () -> assertThat(reservationRepository.findAllByFilter(
                        new ReservationSearchCondition(1L, 1L, LocalDate.now(), NEXT_DAY)))
                        .hasSize(1),
                () -> assertThat(reservationRepository.findAllByFilter(
                        new ReservationSearchCondition(2L, 1L, LocalDate.now(), NEXT_DAY)))
                        .hasSize(2)
        );
    }

    @DisplayName("필터를 통해 특정 날짜 기간의 예약을 검색할 수 있다")
    @Test
    void canFindAllByDateFilter() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원1", "test1@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)",
                LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");

        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, TODAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.plusDays(1).toString(), 1L, 1L);

        // when & then
        assertAll(
                () -> assertThat(reservationRepository.findAllByFilter(
                        new ReservationSearchCondition(1L, 1L, LocalDate.now(), NEXT_DAY)))
                        .hasSize(2),
                () -> assertThat(reservationRepository.findAllByFilter(
                        new ReservationSearchCondition(1L, 1L, LocalDate.now(), NEXT_DAY.plusDays(1))))
                        .hasSize(3)
        );
    }

    @DisplayName("ID를 기반으로 예약을 조회할 수 있다")
    @Test
    void canFindById() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원", "test@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);

        // when
        Optional<Reservation> reservation = reservationRepository.findById(1L);

        // then
        assertAll(
                () -> assertThat(reservation).isPresent(),
                () -> assertThat(reservation.get().getId()).isEqualTo(1L)
        );
    }

    @DisplayName("이미 예약된 예외인지 조회할 수 있다")
    @Test
    void canCheckAlreadyReserved() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원", "test@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);

        // when
        boolean isAlreadyReserved = reservationRepository.checkAlreadyReserved(NEXT_DAY, 1L, 1L);
        boolean isNotAlreadyReserved = reservationRepository.checkAlreadyReserved(TODAY, 1L, 1L);

        // then
        assertAll(
                () -> assertThat(isAlreadyReserved).isTrue(),
                () -> assertThat(isNotAlreadyReserved).isFalse()
        );
    }

    @DisplayName("특정 예약시간 해당하는 예약이 존재하는지 조회할 수 있다")
    @Test
    void canCheckExistenceInTime() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원", "test@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);

        // when
        boolean isInTime = reservationRepository.checkExistenceInTime(1L);
        boolean isNotInTime = reservationRepository.checkExistenceInTime(100L);

        // then
        assertAll(
                () -> assertThat(isInTime).isTrue(),
                () -> assertThat(isNotInTime).isFalse()
        );
    }

    @DisplayName("특정 테마에 해당하는 예약이 존재하는지 조회할 수 있다")
    @Test
    void canCheckExistenceInTheme() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원", "test@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);

        // when
        boolean isInTheme = reservationRepository.checkExistenceInTheme(1L);
        boolean isNotInTheme = reservationRepository.checkExistenceInTheme(100L);

        // then
        assertAll(
                () -> assertThat(isInTheme).isTrue(),
                () -> assertThat(isNotInTheme).isFalse()
        );
    }

    @DisplayName("예약을 추가할 수 있다")
    @Test
    void canAdd() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?,?,?,?)",
                "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL.toString());
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        Member member = new Member(1L, "회원", "test@test.com", "ecxewqe!23", MemberRole.GENERAL);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "이름", "설명", "섬네일");

        // when
        reservationRepository.add(Reservation.createWithoutId(member, NEXT_DAY, time, theme));

        // then
        Optional<Reservation> actualReservation = reservationRepository.findById(1L);
        Reservation expectedReservation = new Reservation(1L, member, NEXT_DAY, time, theme);
        assertAll(
                () -> assertThat(actualReservation).isPresent(),
                () -> assertThat(actualReservation.get()).isEqualTo(expectedReservation)
        );
    }

    @DisplayName("ID를 기반으로 예약을 제거할 수 있다")
    @Test
    void canDeleteById() {
        // given
        template.update("INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "회원", "test@test.com", "zdsa123!", MemberRole.GENERAL.toString());
        template.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        template.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        template.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                1L, NEXT_DAY.toString(), 1L, 1L);

        // when
        reservationRepository.deleteById(1L);

        // then
        assertThat(reservationRepository.findAll()).isEmpty();
    }
}
