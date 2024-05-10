package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_AFTER_1DAY;
import static roomescape.TestFixture.MEMBER_BROWN;
import static roomescape.TestFixture.RESERVATION_TIME_10AM;
import static roomescape.TestFixture.ROOM_THEME1;
import static roomescape.TestFixture.TIME;

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
import roomescape.exception.NotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeDaoTest {

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

    @DisplayName("모든 예약 시간을 보여준다")
    @Test
    void findAll() {
        assertThat(reservationTimeDao.findAll()).isEmpty();
    }

    @DisplayName("예약 시간을 저장한다.")
    @Test
    void save() {
        // given & when
        reservationTimeDao.save(RESERVATION_TIME_10AM);
        // then
        assertThat(reservationTimeDao.findAll()).hasSize(1);
    }

    @DisplayName("해당 id의 예약 시간을 보여준다.")
    @Test
    void findById() {
        // given & when
        ReservationTime savedReservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        Long id = savedReservationTime.getId();
        // then
        ReservationTime reservationTime = reservationTimeDao.findById(id)
                .orElseThrow(() -> new NotFoundException("예약시간을 찾을 수 없습니다."));
        assertThat(reservationTime.getStartAt()).isEqualTo(TIME);
    }

    @DisplayName("중복된 예약 시간이 존재하는 지 여부를 반환한다.")
    @Test
    void existsByStartAt() {
        // given
        boolean existsFalse = reservationTimeDao.existByStartAt(TIME);
        reservationTimeDao.save(RESERVATION_TIME_10AM);
        // when
        boolean existsTrue = reservationTimeDao.existByStartAt(TIME);
        // then
        assertAll(
                () -> assertThat(existsFalse).isFalse(),
                () -> assertThat(existsTrue).isTrue()
        );
    }

    @DisplayName("해당 id의 예약 시간을 삭제한다.")
    @Test
    void deleteById() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        // when
        reservationTimeDao.deleteById(reservationTime.getId());
        // then
        assertThat(reservationTimeDao.findAll()).isEmpty();
    }

    @DisplayName("해당 id의 예약 시간을 삭제하는 경우, 그 id를 참조하는 예약도 삭제한다.")
    @Test
    void deleteByIdDeletesReservationAlso() {
        // given
        Member member = memberDao.save(MEMBER_BROWN);
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        RoomTheme roomTheme = roomThemeDao.save(ROOM_THEME1);
        reservationDao.save(new Reservation(member, DATE_AFTER_1DAY, reservationTime, roomTheme));
        // when
        reservationTimeDao.deleteById(reservationTime.getId());
        // then
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하면 true를 반환한다.")
    @Test
    void returnTrueWhenDeleted() {
        // given
        ReservationTime reservationTime = reservationTimeDao.save(RESERVATION_TIME_10AM);
        // when
        boolean deleted = reservationTimeDao.deleteById(reservationTime.getId());
        // then
        assertThat(deleted).isTrue();
    }

    @DisplayName("삭제 대상이 존재하지 않으면 false를 반환한다.")
    @Test
    void returnFalseWhenNotDeleted() {
        // given & when
        boolean deleted = reservationTimeDao.deleteById(1L);
        // then
        assertThat(deleted).isFalse();
    }
}
