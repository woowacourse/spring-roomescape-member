package roomescape.business.service.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.persistence.fakerepository.FakeReservationRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.presentation.admin.dto.ReservationTimeRequestDto;

class AdminReservationTimeServiceTest {

    private AdminReservationTimeService adminReservationTimeService;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        adminReservationTimeService = new AdminReservationTimeService(reservationRepository, reservationTimeRepository);
    }

    @DisplayName("예약 가능한 시간을 추가한다")
    @Test
    void createTime() {
        // given
        LocalTime time = LocalTime.parse("10:00");
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(time);

        // when
        Long timeId = adminReservationTimeService.createTime(reservationTimeRequestDto).id();

        // then
        ReservationTime saved = reservationTimeRepository.findById(timeId).get();
        assertAll(
                () -> assertThat(saved.getId()).isEqualTo(timeId),
                () -> assertThat(saved.getStartAt()).isEqualTo(time)
        );
    }

    @DisplayName("예약 가능한 시간을 추가할 때 중복된 시간이 존재하면 예외가 발생한다")
    @Test
    void shouldThrowException_WhenAddingDuplicateTime() {
        // given
        LocalTime time = LocalTime.now();
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(time);
        adminReservationTimeService.createTime(reservationTimeRequestDto);

        // when
        // then
        assertThatCode(() -> adminReservationTimeService.createTime(reservationTimeRequestDto))
                .isInstanceOf(ReservationTimeException.class)
                .hasMessage("해당 시간은 이미 존재합니다.");
    }

    @DisplayName("예약 시간대 하나를 삭제한다")
    @Test
    void deleteTimeById() {
        // given
        LocalTime time = LocalTime.now();
        Long timeId = reservationTimeRepository.add(new ReservationTime(time)).getId();

        // when
        adminReservationTimeService.deleteTimeById(timeId);

        // then
        assertThat(reservationTimeRepository.findById(timeId))
                .isNotPresent();
    }

    @DisplayName("예약이 참조하는 시간대 하나를 삭제하는 경우 예외가 발생한다")
    @Test
    void shouldThrowException_WhenDeletingTimeWithReservation() {
        // given
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDate date = tomorrow.toLocalDate();
        LocalTime time = tomorrow.toLocalTime();
        Long timeId = reservationTimeRepository.add(new ReservationTime(time)).getId();
        reservationRepository.add(
                new Reservation(new Member(1L, "수양", "test@email.com"), date, new ReservationTime(timeId, time),
                        new ReservationTheme(1L, "수양", "수양테마", "수양썸네일")));

        // when
        // then
        assertThatCode(() -> adminReservationTimeService.deleteTimeById(timeId))
                .isInstanceOf(ReservationTimeException.class)
                .hasMessage("해당 시간의 예약이 존재하여 시간을 삭제할 수 없습니다.");
    }
}
