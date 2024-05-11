package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.member.Member;
import roomescape.domain.member.Name;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.member.Role;
import roomescape.domain.theme.Theme;

import java.time.LocalDate;
import java.util.List;


class ReservationJdbcDaoTest extends DaoTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private ReservationDao reservationDao;

    private Member member;
    private ReservationTime reservationTime;
    private Theme theme;

    @BeforeEach
    void setUp() {
        member = memberDao.save(new Member(new Name("냥인"), "nyangin@email.com", "1234", Role.USER));
        reservationTime = reservationTimeDao.save(new ReservationTime("19:00"));
        theme = themeDao.save(new Theme("호러", "매우 무섭습니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void save() {
        // given
        final Reservation reservation = new Reservation(member, "2034-05-08", reservationTime, theme);

        // when
        final Reservation savedReservation = reservationDao.save(reservation);

        // then
        assertThat(savedReservation.getId()).isNotNull();
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    void findAll() {
        // given
        final Reservation reservation = new Reservation(member, "2034-05-08", reservationTime, theme);
        reservationDao.save(reservation);

        // when
        final List<Reservation> reservations = reservationDao.findAll();

        // then
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("테마, 사용자, 예약 날짜에 따른 예약 목록을 조회한다.")
    void findAllByThemeAndMemberAndPeriod() {
        // given
        final Reservation reservation = new Reservation(member, "2034-05-08", reservationTime, theme);
        reservationDao.save(reservation);
        final Reservation reservation2 = new Reservation(member, "2034-05-09", reservationTime, theme);
        reservationDao.save(reservation2);

        // when
        final List<Reservation> reservations = reservationDao.findAllByThemeAndMemberAndPeriod(
                theme.getId(), member.getId(), LocalDate.parse("2034-05-08"), LocalDate.parse("2034-05-09"));

        // then
        assertThat(reservations).hasSize(2);
    }

    @Test
    @DisplayName("동일 시간대의 예약 목록을 조회한다.")
    void findAllByDateAndTime() {
        // given
        final Reservation reservation = new Reservation(member, "2034-05-08", reservationTime, theme);
        reservationDao.save(reservation);

        // when
        final List<Reservation> reservations = reservationDao.findAllByDateAndTimeAndThemeId(
                LocalDate.parse("2034-05-08"), reservationTime, 1L);

        // then
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("Id에 해당하는 예약이 존재하면 true를 반환한다.")
    void returnTrueWhenExistById() {
        // given
        final Reservation reservation = new Reservation(member, "2034-05-08", reservationTime, theme);
        final Reservation savedReservation = reservationDao.save(reservation);

        // when
        final boolean isExist = reservationDao.existById(savedReservation.getId());

        // then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("Id에 해당하는 예약이 존재하지 않으면 false를 반환한다.")
    void returnFalseWhenNotExistById() {
        // given
        final Long id = 1L;

        // when
        final boolean isExist = reservationDao.existById(id);

        // then
        assertThat(isExist).isFalse();
    }

    @Test
    @DisplayName("Id에 해당하는 예약을 삭제한다.")
    void deleteById() {
        // given
        final Reservation reservation = new Reservation(member, "2034-05-08", reservationTime, theme);
        final Reservation savedReservation = reservationDao.save(reservation);

        // when
        reservationDao.deleteById(savedReservation.getId());

        // then
        final List<Reservation> reservations = reservationDao.findAll();
        assertThat(reservations).hasSize(0);
    }

    @Test
    @DisplayName("timeId에 해당하는 예약 건수를 조회한다.")
    void countByTimeId() {
        // given
        final long timeId = 2L;

        // when
        final int count = reservationDao.countByTimeId(timeId);

        // then
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("날짜와 themeId로 예약 목록을 조회한다.")
    void findAllByDateAndThemeId() {
        // given
        final Reservation reservation = new Reservation(member, "2034-05-08", reservationTime, theme);
        reservationDao.save(reservation);

        // when
        final List<Long> reservationsByDateAndThemeId = reservationDao.findAllTimeIdsByDateAndThemeId(
                LocalDate.parse("2034-05-08"), theme.getId());

        // then
        assertThat(reservationsByDateAndThemeId).hasSize(1);
    }
}
