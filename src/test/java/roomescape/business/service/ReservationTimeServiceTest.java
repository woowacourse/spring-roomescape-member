package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.Reservation;
import roomescape.business.ReservationTime;
import roomescape.business.dto.ReservationTimeRequestDto;
import roomescape.business.dto.ReservationTimeResponseDto;
import roomescape.business.fakerepository.FakeReservationRepository;
import roomescape.business.fakerepository.FakeReservationTimeRepository;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationTimeRepository;

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
    void readTimeOne() {
        // given
        LocalTime time = LocalTime.now();
        Long timeId = reservationTimeRepository.add(new ReservationTime(time));
        ReservationTimeResponseDto expected = new ReservationTimeResponseDto(timeId, time);

        // when
        ReservationTimeResponseDto actual = reservationTimeService.readTimeOne(timeId);

        // then
        assertThat(actual)
                .isEqualTo(expected);
    }

    @DisplayName("예약 가능한 시간을 추가한다")
    @Test
    void createTime() {
        // given
        LocalTime time = LocalTime.now();
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(time);

        // when
        Long timeId = reservationTimeService.createTime(reservationTimeRequestDto);

        // then
        ReservationTime saved = reservationTimeRepository.findById(timeId);
        assertAll(
                () -> assertThat(saved.getId())
                        .isEqualTo(timeId),
                () -> assertThat(saved.getStartAt())
                        .isEqualTo(time)
        );
    }

    @DisplayName("예약 시간대 하나를 삭제한다")
    @Test
    void deleteTime() {
        // given
        LocalTime time = LocalTime.now();
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(time);
        Long timeId = reservationTimeService.createTime(reservationTimeRequestDto);

        // when
        reservationTimeService.deleteTime(timeId);

        // then
        assertThatCode(() -> reservationTimeRepository.findById(timeId))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("예약이 참조하는 시간대 하나를 삭제한다")
    @Test
    void deleteReferencedTime() {
        // given
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDate date = tomorrow.toLocalDate();
        LocalTime time = tomorrow.toLocalTime();
        Long timeId = reservationTimeRepository.add(new ReservationTime(time));
        reservationRepository.add(new Reservation("수양", date, new ReservationTime(timeId, time), null));

        // when
        // then
        assertThatCode(() -> reservationTimeService.deleteTime(timeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간의 예약이 존재하여 시간을 삭제할 수 없습니다.");
    }
}
