package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.Reservation;
import roomescape.business.domain.ReservationTheme;
import roomescape.business.domain.ReservationTime;
import roomescape.exception.ReservationTimeException;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.persistence.fakerepository.FakeReservationRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.presentation.dto.ReservationTimeRequestDto;
import roomescape.presentation.dto.ReservationTimeResponseDto;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationTimeService = new ReservationTimeService(
                reservationRepository,
                reservationTimeRepository
        );
    }

    @DisplayName("아이디로 예약 가능한 시간을 조회한다")
    @Test
    void getTimeById() {
        // given
        LocalTime time = LocalTime.parse("10:00");
        Long timeId = reservationTimeRepository.add(new ReservationTime(time));
        ReservationTimeResponseDto expected = new ReservationTimeResponseDto(timeId, time);

        // when
        ReservationTimeResponseDto actual = reservationTimeService.getTimeById(timeId);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @DisplayName("예약 가능한 시간을 추가한다")
    @Test
    void createTime() {
        // given
        LocalTime time = LocalTime.parse("10:00");
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(time);

        // when
        Long timeId = reservationTimeService.createTime(reservationTimeRequestDto);

        // then
        ReservationTime saved = reservationTimeRepository.findById(timeId).get();
        assertAll(
                () -> assertThat(saved.getId())
                        .isEqualTo(timeId),
                () -> assertThat(saved.getStartAt())
                        .isEqualTo(time)
        );
    }

    @DisplayName("예약 가능한 시간을 추가할 때 중복된 시간이 존재하면 예외를 발생시킨다")
    @Test
    void createTimeWithDuplicate() {
        // given
        LocalTime time = LocalTime.now();
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(time);
        reservationTimeService.createTime(reservationTimeRequestDto);

        // when
        // then
        assertThatCode(() -> reservationTimeService.createTime(reservationTimeRequestDto))
                .isInstanceOf(ReservationTimeException.class)
                .hasMessage("해당 시간은 이미 존재합니다.");
    }

    @DisplayName("예약 시간대 하나를 삭제한다")
    @Test
    void deleteTimeById() {
        // given
        LocalTime time = LocalTime.now();
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(time);
        Long timeId = reservationTimeService.createTime(reservationTimeRequestDto);

        // when
        reservationTimeService.deleteTimeById(timeId);

        // then
        assertThat(reservationTimeRepository.findById(timeId))
                .isNotPresent();
    }

    @DisplayName("예약이 참조하는 시간대 하나를 삭제한다")
    @Test
    void deleteReferencedTime() {
        // given
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDate date = tomorrow.toLocalDate();
        LocalTime time = tomorrow.toLocalTime();
        Long timeId = reservationTimeRepository.add(new ReservationTime(time));
        reservationRepository.add(new Reservation("수양", date, new ReservationTime(timeId, time), new ReservationTheme(1L, "수양", "수양테마", "수양썸네일")));

        // when
        // then
        assertThatCode(() -> reservationTimeService.deleteTimeById(timeId))
                .isInstanceOf(ReservationTimeException.class)
                .hasMessage("해당 시간의 예약이 존재하여 시간을 삭제할 수 없습니다.");
    }
}
