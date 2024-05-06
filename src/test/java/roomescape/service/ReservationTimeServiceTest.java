package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE;
import static roomescape.TestFixture.RESERVATION_TIME_10AM;
import static roomescape.TestFixture.RESERVATION_TIME_11AM;
import static roomescape.TestFixture.ROOM_THEME1;
import static roomescape.TestFixture.ROOM_THEME2;
import static roomescape.TestFixture.TIME;
import static roomescape.TestFixture.VALID_STRING_TIME;

import io.restassured.RestAssured;
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
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.RoomTheme;
import roomescape.exception.BadRequestException;
import roomescape.service.dto.request.ReservationAvailabilityTimeRequest;
import roomescape.service.dto.request.ReservationTimeRequest;
import roomescape.service.dto.response.ReservationAvailabilityTimeResponse;
import roomescape.service.dto.response.ReservationTimeResponse;

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

    @DisplayName("선택한 테마에 대한 예약시간이 존재하는 경우 예약 가능한 시간을 반환한다.")
    @Test
    void findReservationAvailabilityTimesWhenReservationTimeExist() {
        // given
        // 시간 저장
        ReservationTime savedReservationTime10AM = reservationTimeDao.save(RESERVATION_TIME_10AM);
        ReservationTime savedReservationTime11AM = reservationTimeDao.save(RESERVATION_TIME_11AM);

        // 테마 저장
        RoomTheme savedRoomTheme = roomThemeDao.save(ROOM_THEME1);

        // 예약 저장
        reservationDao.save(new Reservation("브라운", DATE, savedReservationTime10AM, savedRoomTheme));

        ReservationAvailabilityTimeRequest timeRequest = new ReservationAvailabilityTimeRequest(
                DATE, savedRoomTheme.getId());

        // when
        List<ReservationAvailabilityTimeResponse> timeResponses =
                reservationTimeService.findReservationAvailabilityTimes(timeRequest);

        // then
        ReservationAvailabilityTimeResponse response1 = timeResponses.get(0);
        ReservationAvailabilityTimeResponse response2 = timeResponses.get(1);

        assertAll(
                () -> assertThat(timeResponses).hasSize(2),
                () -> assertThat(response1.id()).isEqualTo(savedReservationTime10AM.getId()),
                () -> assertThat(response1.booked()).isTrue(),
                () -> assertThat(response2.id()).isEqualTo(savedReservationTime11AM.getId()),
                () -> assertThat(response2.booked()).isFalse()
        );
    }

    @DisplayName("선택한 테마에 대한 예약시간이 존재하지 않는 경우 예약 가능한 시간을 반환한다.")
    @Test
    void findReservationTimesWithBookStatus() {
        // given
        // 시간 저장
        ReservationTime savedReservationTime10AM = reservationTimeDao.save(RESERVATION_TIME_10AM);
        ReservationTime savedReservationTime11AM = reservationTimeDao.save(RESERVATION_TIME_11AM);

        // 테마 저장
        RoomTheme savedRoomTheme1 = roomThemeDao.save(ROOM_THEME1);
        RoomTheme savedRoomTheme2 = roomThemeDao.save(ROOM_THEME2);

        // 예약 저장
        reservationDao.save(new Reservation("브라운", DATE, savedReservationTime10AM, savedRoomTheme1));

        ReservationAvailabilityTimeRequest timeRequest = new ReservationAvailabilityTimeRequest(
                DATE, savedRoomTheme2.getId());

        // when
        List<ReservationAvailabilityTimeResponse> timeResponses =
                reservationTimeService.findReservationAvailabilityTimes(timeRequest);

        // then
        ReservationAvailabilityTimeResponse response1 = timeResponses.get(0);
        ReservationAvailabilityTimeResponse response2 = timeResponses.get(1);

        assertAll(
                () -> assertThat(timeResponses).hasSize(2),
                () -> assertThat(response1.id()).isEqualTo(savedReservationTime10AM.getId()),
                () -> assertThat(response1.booked()).isFalse(),
                () -> assertThat(response2.id()).isEqualTo(savedReservationTime11AM.getId()),
                () -> assertThat(response2.booked()).isFalse()
        );
    }

    @DisplayName("예약 시간을 저장한다.")
    @Test
    void save() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(TIME);
        // when
        ReservationTimeResponse response = reservationTimeService.save(reservationTimeRequest);
        // then
        assertAll(
                () -> assertThat(reservationTimeService.findAll()).hasSize(1),
                () -> assertThat(response.startAt()).isEqualTo(VALID_STRING_TIME)
        );
    }

    @DisplayName("중복된 예약 시간을 저장하려 하면 예외가 발생한다.")
    @Test
    void duplicatedTimeSaveThrowsException() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(TIME);
        reservationTimeService.save(reservationTimeRequest);
        // when & then
        assertThatThrownBy(() -> reservationTimeService.save(reservationTimeRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("중복된 시간을 생성할 수 없습니다.");
    }

    @DisplayName("예약 시간을 삭제한다.")
    @Test
    void deleteById() {
        // given
        ReservationTimeResponse response = reservationTimeService
                .save(new ReservationTimeRequest(TIME));
        // when
        reservationTimeService.deleteById(response.id());
        // then
        assertThat(reservationTimeService.findAll()).isEmpty();
    }
}
