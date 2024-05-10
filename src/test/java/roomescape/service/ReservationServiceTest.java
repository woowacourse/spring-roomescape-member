package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_AFTER_1DAY;
import static roomescape.TestFixture.DATE_AFTER_2DAY;
import static roomescape.TestFixture.MEMBER_BROWN;
import static roomescape.TestFixture.RESERVATION_TIME_10AM;
import static roomescape.TestFixture.ROOM_THEME1;
import static roomescape.TestFixture.VALID_STRING_DATE;
import static roomescape.TestFixture.VALID_STRING_TIME;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.dao.MemberDao;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.exception.BadRequestException;
import roomescape.service.dto.request.ReservationCreateRequest;
import roomescape.service.dto.response.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
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

    @DisplayName("모든 예약 검색")
    @Test
    void findAll() {
        assertThat(reservationService.findAll()).isEmpty();
    }

    @DisplayName("예약 검색 조건에 빈값을 넣으면 예외를 발생시킨다.")
    @Test
    void findByNullException() {
        assertThatThrownBy(() -> reservationService.findBy(null, null, null, null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("검색 조건이 빈값일 수 없습니다.");
    }

    @DisplayName("dateFrom이 dateTo보다 이후 시간이면 예외를 발생시킨다.")
    @Test
    void findByDateException() {
        assertThatThrownBy(() -> reservationService.findBy(1L, 1L, DATE_AFTER_2DAY, DATE_AFTER_1DAY))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시간을 잘못 입력하셨습니다.");
    }

    @DisplayName("예약 저장")
    @Test
    void save() {
        // given
        ReservationCreateRequest reservationCreateRequest = createReservationRequest(VALID_STRING_DATE);
        // when
        ReservationResponse response = reservationService.save(reservationCreateRequest);
        // then
        assertAll(
                () -> assertThat(reservationService.findAll()).hasSize(1),
                () -> assertThat(response.member().name()).isEqualTo("브라운"),
                () -> assertThat(response.date()).isEqualTo(VALID_STRING_DATE),
                () -> assertThat(response.time().startAt()).isEqualTo(VALID_STRING_TIME)
        );
    }

    @DisplayName("지난 예약을 저장하려 하면 예외가 발생한다.")
    @Test
    void pastReservationSaveThrowsException() {
        // given
        ReservationCreateRequest reservationCreateRequest = createReservationRequest("2000-11-09");
        // when & then
        assertThatThrownBy(() -> reservationService.save(reservationCreateRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("지나간 날짜와 시간에 대한 예약을 생성할 수 없습니다.");
    }

    @DisplayName("삭제 테스트")
    @Test
    void deleteById() {
        // given
        createReservationRequest(VALID_STRING_DATE);
        // when
        reservationService.deleteById(1L);
        // then
        assertThat(reservationService.findAll()).isEmpty();
    }

    private ReservationCreateRequest createReservationRequest(String date) {
        Member member = memberDao.save(MEMBER_BROWN);
        ReservationTime savedReservationTime = reservationTimeDao.save(
                RESERVATION_TIME_10AM);
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME1);
        return new ReservationCreateRequest(member.getId(), LocalDate.parse(date),
                savedReservationTime.getId(), savedRoomTheme.getId());
    }
}
