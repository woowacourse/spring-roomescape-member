package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.AuthorizationException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.exception.SameScheduleException;
import roomescape.reservation.dto.ReservationCreateInfo;
import roomescape.reservation.dto.ReservationIdResponse;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.support.DatabaseHelper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.support.TestFixture.SCHEDULE_12시;
import static roomescape.support.TestFixture.THEME_공포;
import static roomescape.support.TestFixture.USER_1;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private DatabaseHelper databaseHelper;

    @BeforeEach
    void setUp() {
        databaseHelper.cleanUp();
        databaseHelper.insertUser(USER_1.getId(), USER_1.getName(), USER_1.getRole().name());
        databaseHelper.insertTheme(THEME_공포.getId(), THEME_공포.getName(), THEME_공포.getDescription(), THEME_공포.getImageUrl(), "02:00:00");
        databaseHelper.insertSchedule(SCHEDULE_12시.getId(), THEME_공포.getId(), "2026-12-10 12:00:00", "2026-12-10 14:00:00");
        databaseHelper.insertSchedule(2L, THEME_공포.getId(), "2026-12-10 15:00:00", "2026-12-10 17:00:00");
        reservationRepository.create(new Reservation(USER_1, SCHEDULE_12시, THEME_공포));
    }

    @Test
    void 데이터베이스에_저장된_모든_예약을_조회한다() {
        ReservationsResponse response = reservationService.findAll();

        assertThat(response.getReservationsResponse()).hasSize(1);
    }

    @Test
    void 새로운_예약을_생성한다() {
        ReservationCreateInfo info = new ReservationCreateInfo(1L,
                LocalDateTime.of(2026, 12, 10, 15, 0), 1L);

        ReservationIdResponse response = reservationService.create(info);

        assertThat(response.getId()).isNotNull();
        assertThat(reservationService.findAll().getReservationsResponse()).hasSize(2);
    }

    @Test
    void 예약을_삭제한다() {
        Long targetId = reservationService.findAll()
                .getReservationsResponse()
                .get(0)
                .getReservationId();

        reservationService.delete(targetId);

        assertThat(reservationService.findAll().getReservationsResponse()).isEmpty();
    }

    @Test
    void 존재하지_않는_테마로_예약을_시도하면_예외가_발생한다() {
        ReservationCreateInfo info = new ReservationCreateInfo(1L,
                LocalDateTime.of(2026, 12, 10, 15, 0), 999L);

        assertThatThrownBy(() -> reservationService.create(info))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    @Test
    void 등록된_스케줄이_없는_시간에_예약을_시도하면_예외가_발생한다() {
        ReservationCreateInfo info = new ReservationCreateInfo(1L,
                LocalDateTime.of(2026, 12, 10, 10, 0), 1L);

        assertThatThrownBy(() -> reservationService.create(info))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록된 스케줄이 없습니다.");
    }

    @Test
    void 이미_예약이_완료된_스케줄에_중복_예약을_시도하면_예외가_발생한다() {
        ReservationCreateInfo info = new ReservationCreateInfo(1L,
                LocalDateTime.of(2026, 12, 10, 12, 0), 1L);

        assertThatThrownBy(() -> reservationService.create(info))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간은 이미 예약이 완료되었습니다.");
    }

    @Test
    void 과거의_날짜_시간에_예약을_시도하면_예외가_발생한다() {
        ReservationCreateInfo info = new ReservationCreateInfo(1L,
                LocalDateTime.of(2020, 1, 1, 1, 1), 1L);

        assertThatThrownBy(() -> reservationService.create(info))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 사용자가_본인의_예약_목록을_정상적으로_조회한다() {
        Long id = USER_1.getId();
        ReservationsResponse response = reservationService.findAllByUserId(id);

        assertThat(response.getReservationsResponse()).hasSize(1);
        assertThat(response.getReservationsResponse().getFirst().getUserId()).isEqualTo(id);
    }

    @Test
    void 사용자가_본인의_예약을_정상적으로_취소한다() {
        Reservation reservation = reservationRepository.findAll().get(0);
        Long reservationId = reservation.getId();
        Long ownerId = reservation.getUser().getId();

        assertDoesNotThrow(() -> reservationService.cancel(reservationId, ownerId));
        assertThat(reservationRepository.findById(reservationId)).isEmpty();
    }

    @Test
    void 존재하지_않는_예약을_취소하려고_하면_예외가_발생한다() {
        Long nonExistentReservationId = 999L;
        Long currentUserId = USER_1.getId();

        assertThatThrownBy(() -> reservationService.cancel(nonExistentReservationId, currentUserId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @Test
    void 다른_사람의_예약을_취소하려고_하면_예외가_발생한다() {
        Reservation reservationOfUser1 = reservationRepository.findAll().get(0);
        Long reservationId = reservationOfUser1.getId();
        databaseHelper.insertUser(2L, "user2", "USER");

        assertThatThrownBy(() -> reservationService.cancel(reservationId, 2L))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("예약을 취소할 권한이 없습니다.");
    }

    @Test
    void 사용자가_본인의_예약_시간을_정상적으로_변경한다() {
        Reservation reservation = reservationRepository.findAll().get(0);
        Long reservationId = reservation.getId();
        Long currentUserId = reservation.getUser().getId();
        Long newScheduleId = 2L;

        assertDoesNotThrow(() -> reservationService.changeSchedule(reservationId, newScheduleId, currentUserId));

        Reservation updatedReservation = reservationRepository.findById(reservationId).get();
        assertThat(updatedReservation.getSchedule().getId()).isEqualTo(newScheduleId);
    }

    @Test
    void 기존과_동일한_스케줄로_변경을_요청하면_아무_작업_없이_정상_종료된다() {
        Reservation reservation = reservationRepository.findAll().get(0);
        Long reservationId = reservation.getId();
        Long currentUserId = reservation.getUser().getId();
        Long currentScheduleId = reservation.getSchedule().getId();

        assertThatThrownBy(() -> reservationService.changeSchedule(reservationId, currentScheduleId, currentUserId))
                .isInstanceOf(SameScheduleException.class)
                .hasMessage("기존과 동일한 스케줄로 변경할 수 없습니다.");
    }

    @Test
    void 존재하지_않는_예약을_변경하려고_하면_예외가_발생한다() {
        Long nonExistentReservationId = 999L;
        Long currentUserId = USER_1.getId();
        Long newScheduleId = 2L;

        assertThatThrownBy(() -> reservationService.changeSchedule(nonExistentReservationId, newScheduleId, currentUserId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");
    }

    @Test
    void 다른_사람의_예약을_변경하려고_하면_예외가_발생한다() {
        Reservation reservationOfUser1 = reservationRepository.findAll().get(0);
        Long reservationId = reservationOfUser1.getId();
        databaseHelper.insertUser(2L, "user2", "USER");
        Long newScheduleId = 2L;

        assertThatThrownBy(() -> reservationService.changeSchedule(reservationId, newScheduleId, 2L))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("예약을 변경할 권한이 없습니다.");
    }

    @Test
    void 존재하지_않는_스케줄로_변경하려고_하면_예외가_발생한다() {
        Reservation reservation = reservationRepository.findAll().get(0);
        Long reservationId = reservation.getId();
        Long currentUserId = reservation.getUser().getId();
        Long nonExistentScheduleId = 999L;

        assertThatThrownBy(() -> reservationService.changeSchedule(reservationId, nonExistentScheduleId, currentUserId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("존재하지 않는 스케줄입니다.");
    }

    @Test
    void 이미_예약이_완료된_스케줄로_변경하려고_하면_예외가_발생한다() {
        Reservation reservation = reservationRepository.findAll().get(0);
        Long reservationId = reservation.getId();
        Long currentUserId = reservation.getUser().getId();

        databaseHelper.insertUser(2L, "user2", "USER");
        databaseHelper.insertReservation(2L, 2L, 2L);

        assertThatThrownBy(() -> reservationService.changeSchedule(reservationId, 2L, currentUserId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간은 이미 예약이 완료되었습니다.");
    }
}
