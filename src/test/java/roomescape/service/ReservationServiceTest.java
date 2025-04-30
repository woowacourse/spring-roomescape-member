package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.fake.FakeThemeDao;
import roomescape.service.reservation.Reservation;
import roomescape.service.reservation.ReservationTime;
import roomescape.service.reservation.Theme;

class ReservationServiceTest {

    FakeReservationTimeDao reservationTimeDao = new FakeReservationTimeDao();
    FakeReservationDao reservationDao = new FakeReservationDao();
    FakeThemeDao themeDao = new FakeThemeDao();
    ReservationService reservationService = new ReservationService(reservationDao, reservationTimeDao, themeDao);
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    @DisplayName("중복 예약일 경우 예외가 발생한다.")
    @Test
    void testValidateDuplication() {
        // given
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(LocalTime.of(11, 0)));
        Theme theme = new Theme(null, "우테코탈출", "탈출탈출탈출, ", "aaaa");
        Theme savedTheme = themeDao.save(theme);
        ReservationRequest request = new ReservationRequest("leo", tomorrow, savedTime.getId(), savedTheme.getId());
        reservationService.createReservation(request);
        // when
        // then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간에 이미 예약이 존재합니다.");
    }

    @DisplayName("예약 시간이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void testValidateTime() {
        // given
        Theme theme = new Theme(null, "우테코탈출", "탈출탈출탈출, ", "aaaa");
        Theme savedTheme = themeDao.save(theme);
        ReservationRequest request = new ReservationRequest("leo", tomorrow, 1L, savedTheme.getId());
        // when
        // then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("과거 시간에 예약할 경우 예외가 발생한다.")
    @Test
    void testValidatePastTime() {
        // given
        reservationTimeDao.save(new ReservationTime(LocalTime.of(11, 0)));
        Theme theme = new Theme(null, "우테코탈출", "탈출탈출탈출, ", "aaaa");
        Theme savedTheme = themeDao.save(theme);
        LocalDate yesterday = LocalDate.now().minusDays(1);
        ReservationRequest request = new ReservationRequest("leo", yesterday, 1L, savedTheme.getId());
        // when
        // then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 날짜와 시간은 예약 불가합니다.");
    }

    @DisplayName("예약을 생성할 수 있다.")
    @Test
    void testCreate() {
        // given
        LocalTime time = LocalTime.of(11, 0);
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(time));
        Theme theme = new Theme(null, "우테코탈출", "탈출탈출탈출, ", "aaaa");
        Theme savedTheme = themeDao.save(theme);
        ReservationRequest request = new ReservationRequest("leo", tomorrow, savedTime.getId(), savedTheme.getId());
        // when
        ReservationResponse result = reservationService.createReservation(request);
        // then
        Reservation savedReservation = reservationDao.findById(1L);
        assertAll(
                () -> assertThat(result.id()).isEqualTo(1L),
                () -> assertThat(result.name()).isEqualTo(request.name()),
                () -> assertThat(result.date()).isEqualTo(request.date()),
                () -> assertThat(result.time().startAt()).isEqualTo(time),
                () -> assertThat(result.time().id()).isEqualTo(request.timeId()),

                () -> assertThat(result.theme().id()).isEqualTo(savedTheme.getId()),
                () -> assertThat(result.theme().name()).isEqualTo(savedTheme.getName()),
                () -> assertThat(result.theme().description()).isEqualTo(savedTheme.getDescription()),
                () -> assertThat(result.theme().thumbnail()).isEqualTo(savedTheme.getThumbnail()),

                () -> assertThat(savedReservation.getName()).isEqualTo(request.name()),
                () -> assertThat(savedReservation.getDate()).isEqualTo(request.date()),
                () -> assertThat(savedReservation.getTime().getStartAt()).isEqualTo(time),
                () -> assertThat(savedReservation.getTimeId()).isEqualTo(request.timeId()),

                () -> assertThat(savedReservation.getTheme().getId()).isEqualTo(savedTheme.getId()),
                () -> assertThat(savedReservation.getTheme().getName()).isEqualTo(savedTheme.getName()),
                () -> assertThat(savedReservation.getTheme().getDescription()).isEqualTo(savedTheme.getDescription()),
                () -> assertThat(savedReservation.getTheme().getThumbnail()).isEqualTo(savedTheme.getThumbnail())
        );
    }

    @DisplayName("예약 목록을 조회할 수 있다.")
    @Test
    void testFindAll() {
        // given
        LocalTime time = LocalTime.of(11, 0);
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(time));
        Theme theme = new Theme(null, "우테코탈출", "탈출탈출탈출, ", "aaaa");
        Theme savedTheme = themeDao.save(theme);
        ReservationRequest request1 = new ReservationRequest("leo", tomorrow, savedTime.getId(), savedTheme.getId());
        ReservationRequest request2 = new ReservationRequest("leo", tomorrow.plusDays(1), savedTime.getId(),
                savedTheme.getId());
        reservationService.createReservation(request1);
        reservationService.createReservation(request2);
        // when
        // then
        assertThat(reservationService.getReservations()).hasSize(2);
    }

    @DisplayName("예약을 삭제할 수 있다.")
    @Test
    void testCancelById() {
        // given
        LocalTime time = LocalTime.of(11, 0);
        ReservationTime savedTime = reservationTimeDao.save(new ReservationTime(time));
        Theme theme = new Theme(null, "우테코탈출", "탈출탈출탈출, ", "aaaa");
        Theme savedTheme = themeDao.save(theme);
        ReservationRequest request = new ReservationRequest("leo", tomorrow, savedTime.getId(), savedTheme.getId());
        reservationService.createReservation(request);
        // when
        reservationService.cancelReservationById(1L);
        // then
        assertThat(reservationService.getReservations()).isEmpty();
    }
}

