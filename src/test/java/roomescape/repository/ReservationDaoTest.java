package roomescape.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import roomescape.RepositoryTest;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;
import roomescape.repository.rowmapper.MemberRowMapper;
import roomescape.repository.rowmapper.ReservationRowMapper;
import roomescape.repository.rowmapper.ReservationTimeRowMapper;
import roomescape.repository.rowmapper.ThemeRowMapper;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationDaoTest extends RepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private RowMapper<Reservation> rowMapper;
    private ReservationDao reservationDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into reservation_time(start_at) values ('09:00')");
        jdbcTemplate.update("insert into member(member_name, email, password, role) " +
                "values ('coli1', 'kkwoo001021@naver.com', 'rlarjsdn1021!', 'USER'), " +
                "('coli2', 'kkwoo1021@hanmail.net', 'rlarjsdn1021!', 'ADMIN')");
        jdbcTemplate.update("insert into theme(theme_name, description, thumbnail) " +
                "values ('공포', '공포입니다.', '123'), " +
                "('해피', '해피입니다.', '456')");

        LocalDate tomorrow = LocalDate.now().plusDays(1L);
        LocalDate yesterday = LocalDate.now().minusDays(1L);

        rowMapper = new ReservationRowMapper(new ReservationTimeRowMapper(), new ThemeRowMapper(), new MemberRowMapper());
        reservationDao = new ReservationDao(jdbcTemplate, dataSource, rowMapper);

        String sql = "insert into reservation(date, time_id, theme_id, member_id) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, tomorrow, 1, 1, 1);
        jdbcTemplate.update(sql, yesterday, 1, 1, 1);
        jdbcTemplate.update(sql, yesterday, 1, 2, 1);
        jdbcTemplate.update(sql, yesterday, 1, 2, 2);
    }

    @AfterEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM member");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    @DisplayName("새로운 예약을 저장할 수 있다")
    void should_SaveNewReservation() {
        //given
        int expectedSize = 5;
        Reservation reservation = new Reservation(
                null,
                LocalDate.now().plusDays(2),
                new ReservationTime(Long.valueOf(1), LocalTime.of(9, 0, 0)),
                new Theme(1L, "공포", "공포입니다.", "123"),
                new Member(1L, "coli1", "kkwoo001021@naver.com", "rlarjsdn1021!", "USER")
        );

        //when
        Reservation savedReservation = reservationDao.save(reservation);

        //then
        assertThat(savedReservation.getId()).isEqualTo(expectedSize);
    }

    @Test
    @DisplayName("아이디를 기반으로 예약을 찾을 수 있다")
    void should_FindById() {
        //given
        int findReservationId = 1;
        int expectedReservationId = 1;

        //when
        Optional<Reservation> foundReservation = reservationDao.findById(findReservationId);

        //then
        assertThat(foundReservation.isPresent()).isTrue();
        assertThat(foundReservation.get().getId()).isEqualTo(expectedReservationId);
    }

    @Test
    @DisplayName("예약 전체를 조회할 수 있다")
    void should_GetAllReservations() {
        //given
        int expectedSize = 4;

        //when-then
        assertThat(reservationDao.getAll()).hasSize(expectedSize);
    }

    @Test
    @DisplayName("예약 시간 id를 통해 해당 시간 예약 이력을 모두 가져올 수 있다.")
    void should_FindReservations_When_GiveTimeId() {
        //given
        int expectedSize = 4;
        int findTimeId = 1;

        //when
        List<Reservation> timeReservations = reservationDao.findByTimeId(findTimeId);

        //then
        assertThat(timeReservations).hasSize(expectedSize);
    }

    @Test
    @DisplayName("테마 id를 통해 해당 테마 예약 이력을 모두 가져올 수 있다")
    void should_FindReservations_When_GiveThemeId() {
        //given
        int expectedSize = 2;
        int findThemeId = 1;

        //when
        List<Reservation> themeReservations = reservationDao.findByThemeId(findThemeId);

        //then
        assertThat(themeReservations).hasSize(expectedSize);
    }

    @Test
    @DisplayName("날짜 + 예약 시간 id + 테마 id를 기반으로 예약 이력을 가져올 수 있다.")
    void should_FindReservations_When_Give_Date_And_TimeId_And_ThemeId() {
        //given
        LocalDate yesterday = LocalDate.now().minusDays(1);
        int findTimeId = 1;
        int findThemeId = 2;
        int expectedSize = 2;

        //when
        List<Reservation> themeReservations = reservationDao.findByDateAndTimeIdAndThemeId(yesterday, findTimeId, findThemeId);

        //then
        assertThat(themeReservations).hasSize(expectedSize);
    }

    @Test
    @DisplayName("날짜 + 테마 id를 기반으로 예약된 이력들을 가져올 수 있다.")
    void should_FindTimeIds_When_Give_Date_And_ThemeId() {
        //given
        LocalDate yesterday = LocalDate.now().minusDays(1);
        int findThemeId = 2;
        int expectedSize = 2;

        //when
        List<Reservation> findReservations = reservationDao.findByDateAndThemeId(yesterday, findThemeId);

        //then
        assertThat(findReservations).hasSize(expectedSize);

    }

    @Test
    @DisplayName("특정 기간 내 예약 횟수가 많은 테마 id를 내림차순으로 조회할 수 있다")
    void should_FindThemeIdRanking_When_Give_Duration_And_LimitNumber() {
        //given
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();
        int expectedFirstRankingId = 2;
        int expectedSecondRankingId = 1;

        //when
        List<Long> themeIdRanking = reservationDao.findRanking(yesterday, today, 2);


        //then
        assertThat(themeIdRanking.get(0)).isEqualTo(expectedFirstRankingId);
        assertThat(themeIdRanking.get(1)).isEqualTo(expectedSecondRankingId);
    }

    @Test
    void should_DeleteReservation_When_Give_ReservationId() {
        //given
        long targetId = 1L;
        int expectedSize = 3;

        //when
        reservationDao.delete(targetId);

        //then
        String sql = "SELECT count(*) FROM reservation";
        int size = jdbcTemplate.queryForObject(sql, Integer.class);
        assertThat(size).isEqualTo(expectedSize);
    }

    @Test
    @DisplayName("테마 id와 회원 id가 일치하는 특정 기간 내 예약이력을 조회할 수 있다")
    void should_FindReservations_When_Give_ThemeId_And_MemberId_In_Duration() {
        //given
        long findThemeId = 1L;
        long findMemberId = 1L;
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate today = LocalDate.now();
        int expectedSize1 = 2;
        int expectedSize2 = 1;

        //when
        List<Reservation> findReservations1 = reservationDao.findByThemeIdAndMemberIdInDuration(findThemeId, findMemberId, yesterday, tomorrow);
        List<Reservation> findReservations2 = reservationDao.findByThemeIdAndMemberIdInDuration(findThemeId, findMemberId, yesterday, today);

        //then
        assertThat(findReservations1).hasSize(expectedSize1);
        assertThat(findReservations2).hasSize(expectedSize2);
    }
}