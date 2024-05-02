package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.RESERVATION_TIME_FIXTURE;
import static roomescape.TestFixture.ROOM_THEME_FIXTURE;
import static roomescape.TestFixture.VALID_STRING_TIME_FIXTURE;

import io.restassured.RestAssured;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.RoomThemeDao;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.request.ReservationTimeWithBookStatusRequest;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ReservationTimeWithBookStatusResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeServiceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private RoomThemeDao roomThemeDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
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
    }

    @DisplayName("존재하는 모든 예약 시간을 반환한다.")
    @Test
    void findAll() {
        assertThat(reservationTimeService.findAll()).isEmpty();
    }

    @DisplayName("날짜와 테마에 따른 예약 가능한 시간을 반환한다.")
    @Test
    void findReservationTimesWithBookStatus() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(RESERVATION_TIME_FIXTURE);
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME_FIXTURE);
        reservationDao.save(new Reservation(new Name("brown"), DATE_FIXTURE,
                savedReservationTime, savedRoomTheme));
        ReservationTimeWithBookStatusRequest timeRequest = new ReservationTimeWithBookStatusRequest(
                DATE_FIXTURE.toString(), savedRoomTheme.getId());
        // when
        List<ReservationTimeWithBookStatusResponse> timeResponses =
                reservationTimeService.findReservationTimesWithBookStatus(timeRequest);
        // then
        ReservationTimeWithBookStatusResponse timeResponse = timeResponses.get(0);
        assertAll(
                () -> assertThat(timeResponse.id()).isEqualTo(savedReservationTime.getId()),
                () -> assertThat(LocalTime.parse(timeResponse.startAt())).isEqualTo(
                        savedReservationTime.getStartAt()),
                () -> assertThat(timeResponse.booked()).isTrue()
        );
    }

    @DisplayName("예약 시간을 저장한다.")
    @Test
    void save() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(
                VALID_STRING_TIME_FIXTURE);
        // when
        ReservationTimeResponse response = reservationTimeService.save(reservationTimeRequest);
        // then
        assertAll(
                () -> assertThat(reservationTimeService.findAll()).hasSize(1),
                () -> assertThat(response.startAt()).isEqualTo(VALID_STRING_TIME_FIXTURE)
        );
    }

    @DisplayName("중복된 예약 시간을 저장하려 하면 예외가 발생한다.")
    @Test
    void duplicatedTimeSaveThrowsException() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(
                VALID_STRING_TIME_FIXTURE);
        reservationTimeService.save(reservationTimeRequest);
        // when & then
        assertThatThrownBy(() -> reservationTimeService.save(reservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 예약 시간입니다.");
    }

    @DisplayName("예약 시간을 삭제한다.")
    @Test
    void deleteById() {
        // given
        ReservationTimeResponse response = reservationTimeService
                .save(new ReservationTimeRequest(VALID_STRING_TIME_FIXTURE));
        // when
        reservationTimeService.deleteById(response.id());
        // then
        assertThat(reservationTimeService.findAll()).isEmpty();
    }
}
