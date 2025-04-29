package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationRequestDto;
import roomescape.dto.ReservationResponseDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.entity.ReservationTime;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.ReservationTimeDao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceTest {

    private ReservationTimeDao reservationTimeDao;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationTimeDao = new ReservationTimeInMemoryDao();
        reservationService = new ReservationService(new ReservationInMemoryDao(), reservationTimeDao);
    }

    @Test
    void 예약을_추가한다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(new ReservationTime(LocalTime.of(10, 0)));
        LocalDate date = LocalDate.of(2025, 5, 1);
        ReservationRequestDto request = new ReservationRequestDto(date, "drago", savedReservationTime.getId());

        // when
        ReservationResponseDto savedReservation = reservationService.add(request);

        // then
        ReservationResponseDto expected =
            new ReservationResponseDto(1L, "drago", date,
                new ReservationTimeResponseDto(savedReservationTime.getId(), savedReservationTime.getStartAt()));
        assertThat(savedReservation).isEqualTo(expected);
    }

    @Test
    void 없는_예약시간으로_예약을_추가할_경우_예외가_발생한다() {
        // when & then
        LocalDate date = LocalDate.of(2025, 5, 1);
        assertThatThrownBy(() -> reservationService.add(new ReservationRequestDto(date, "drago", 19L)))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("선택한 예약 시간이 존재하지 않습니다.");
    }

    @Test
    void 예약을_모두_조회한다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationResponseDto drago = reservationService.add(
            new ReservationRequestDto(LocalDate.of(2025, 5, 1), "drago", savedReservationTime.getId()));

        ReservationResponseDto cookie = reservationService.add(
                new ReservationRequestDto(LocalDate.of(2025, 5, 2), "cookie", savedReservationTime.getId()));

        // when
        List<ReservationResponseDto> allReservation = reservationService.findAll();

        // then
        assertThat(allReservation).containsExactly(drago, cookie);
    }

    @Test
    void 예약을_삭제한다() {
        // given
        ReservationTime savedReservationTime = reservationTimeDao.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationResponseDto addedReservation = reservationService.add(
            new ReservationRequestDto(LocalDate.of(2025, 5, 1), "drago", savedReservationTime.getId()));

        // when
        reservationService.deleteById(addedReservation.id());

        // then
        List<ReservationResponseDto> allReservation = reservationService.findAll();
        assertThat(allReservation.isEmpty()).isTrue();
    }

    @Test
    void 없는_예약을_삭제하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> reservationService.deleteById(9L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("삭제할 예약정보가 없습니다.");
    }
}