package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.ADMIN_ZEZE;
import static roomescape.TestFixture.DATE_AFTER_1DAY;
import static roomescape.TestFixture.MEMBER_BROWN;
import static roomescape.TestFixture.RESERVATION_TIME_10AM;
import static roomescape.TestFixture.ROOM_THEME1;
import static roomescape.TestFixture.ROOM_THEME2;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationDaoTest {

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private RoomThemeDao roomThemeDao;
    @Autowired
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        List<Reservation> reservations = reservationDao.findAll();
        for (Reservation reservation : reservations) {
            reservationDao.deleteById(reservation.getId());
        }
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        for (ReservationTime reservationTime : reservationTimes) {
            reservationTimeDao.deleteById(reservationTime.getId());
        }
        List<RoomTheme> roomThemes = roomThemeDao.findAll();
        for (RoomTheme roomTheme : roomThemes) {
            roomThemeDao.deleteById(roomTheme.getId());
        }
        List<Member> members = memberDao.findAll();
        for (Member member : members) {
            memberDao.deleteById(member.getId());
        }
    }

    @DisplayName("존재하는 모든 예약을 보여준다.")
    @Test
    void findAll() {
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("이름에 대한 검색 조건 결과를 반환한다.")
    @Test
    void findBy() {
        // given
        Member brown = memberDao.save(MEMBER_BROWN);
        Member zeze = memberDao.save(ADMIN_ZEZE);

        RoomTheme roomTheme1 = roomThemeDao.save(ROOM_THEME1);
        RoomTheme roomTheme2 = roomThemeDao.save(ROOM_THEME2);

        ReservationTime reservationTime10AM = reservationTimeDao.save(RESERVATION_TIME_10AM);

        Reservation brownReservation = reservationDao.save(
                new Reservation(brown, DATE_AFTER_1DAY, reservationTime10AM, roomTheme1));
        Reservation zezeReservation = reservationDao.save(
                new Reservation(zeze, DATE_AFTER_1DAY, reservationTime10AM, roomTheme2));

        // when
        List<Reservation> reservations = reservationDao.findBy(null, brown.getId(), null, null);

        // then
        Reservation reservation = reservations.get(0);
        assertAll(
                () -> assertThat(reservations.size()).isEqualTo(1),
                () -> assertThat(reservation.getId()).isEqualTo(brownReservation.getId())
        );
    }

    @DisplayName("날짜와 시간이 같은 예약이 존재하는지 여부를 반환한다.")
    @Test
    void duplicatedReservationTest() {
        // given
        Member member = memberDao.save(MEMBER_BROWN);
        ReservationTime savedReservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME1);
        boolean existsFalse
                = reservationDao.existsByDateTime(DATE_AFTER_1DAY, savedReservationTime.getId(), savedRoomTheme.getId());
        reservationDao.save(new Reservation(member, DATE_AFTER_1DAY, savedReservationTime, savedRoomTheme));
        // when
        boolean existsTrue
                = reservationDao.existsByDateTime(DATE_AFTER_1DAY, savedReservationTime.getId(), savedRoomTheme.getId());
        // then
        assertAll(
                () -> assertThat(existsFalse).isFalse(),
                () -> assertThat(existsTrue).isTrue()
        );
    }

    @DisplayName("예약을 저장한다.")
    @Test
    void save() {
        // given
        Member member = memberDao.save(MEMBER_BROWN);
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        RoomTheme roomTheme = roomThemeDao.save(ROOM_THEME1);
        // when
        reservationDao.save(new Reservation(member, DATE_AFTER_1DAY, reservationTime, roomTheme));
        // then
        assertThat(reservationDao.findAll()).hasSize(1);
    }

    @DisplayName("해당 id의 예약을 삭제한다.")
    @Test
    void deleteById() {
        // given
        Member member = memberDao.save(MEMBER_BROWN);
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        RoomTheme roomTheme = roomThemeDao.save(ROOM_THEME1);
        Reservation savedReservation = reservationDao.save(
                new Reservation(member, DATE_AFTER_1DAY, reservationTime, roomTheme));
        // when
        reservationDao.deleteById(savedReservation.getId());
        // then
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하면 true를 반환한다.")
    @Test
    void returnTrueWhenDeleted() {
        // given
        Member member = memberDao.save(MEMBER_BROWN);
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        RoomTheme roomTheme = roomThemeDao.save(ROOM_THEME1);
        Reservation savedReservation = reservationDao.save(
                new Reservation(member, DATE_AFTER_1DAY, reservationTime, roomTheme));
        // when
        boolean deleted = reservationDao.deleteById(savedReservation.getId());
        // then
        assertThat(deleted).isTrue();
    }

    @DisplayName("삭제 대상이 존재하지 않으면 false를 반환한다.")
    @Test
    void returnFalseWhenNotDeleted() {
        // given & when
        boolean deleted = reservationDao.deleteById(1L);
        // then
        assertThat(deleted).isFalse();
    }
}
