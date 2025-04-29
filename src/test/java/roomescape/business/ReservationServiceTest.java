package roomescape.business;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.dto.ReservationRequestDto;
import roomescape.business.dto.ReservationTimeRequestDto;
import roomescape.business.dto.ReservationTimeResponseDto;
import roomescape.business.service.ReservationService;
import roomescape.persistence.GeneralRepository;
import roomescape.business.fakerepository.FakeReservationRepository;
import roomescape.business.fakerepository.FakeReservationTimeRepository;

class ReservationServiceTest {

    private GeneralRepository<ReservationTime> reservationTimeRepository;
    private ReservationService reservationService;
    private Long timeId;

    @BeforeEach
    void setUp() {
        GeneralRepository<Reservation> reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository);

        timeId = reservationTimeRepository.add(new ReservationTime(1L, LocalTime.now()));
    }

    @DisplayName("예약한다")
    @Test
    void createReservation() {
        reservationService.createReservation(new ReservationRequestDto("예약자", LocalDate.now(), timeId));
        Assertions.assertThat(reservationService.readReservationAll()).isNotEmpty();
    }

    @DisplayName("예약을 취소한다")
    @Test
    void deleteReservation() {
        // given
        Long id = reservationService.createReservation(
                new ReservationRequestDto("예약자", LocalDate.now(), timeId));

        // when
        reservationService.deleteReservation(id);

        // then
        Assertions.assertThat(reservationService.readReservationAll()).isEmpty();
    }

    @DisplayName("예약 목록을 불러온다")
    @Test
    void readReservationAll() {
        // given
        Long id = reservationService.createReservation(
                new ReservationRequestDto("예약자", LocalDate.now(), timeId));

        // when
        int firstReadSize = reservationService.readReservationAll().size();
        reservationService.deleteReservation(id);
        int secondReadSize = reservationService.readReservationAll().size();

        // then
        Assertions.assertThat(firstReadSize).isEqualTo(1);
        Assertions.assertThat(secondReadSize).isEqualTo(0);
    }

    @DisplayName("아이디로 예약 가능한 시간을 조회한다")
    @Test
    void readTimeOne() {
        ReservationTimeResponseDto reservationTime = reservationService.readTimeOne(1L);
        Assertions.assertThat(reservationTime).isNotNull();
    }

    @DisplayName("모든 시간을 조회한다")
    @Test
    void readTimeAll() {
        // given
        Long two = reservationTimeRepository.add(new ReservationTime(2L, LocalTime.now()));

        // when
        List<ReservationTimeResponseDto> reservationTimes = reservationService.readTimeAll();

        // then
        Assertions.assertThat(reservationTimes).hasSize(2);
    }

    @DisplayName("예약 가능한 시간을 추가한다")
    @Test
    void createTime() {
        Long timeId = reservationService.createTime(new ReservationTimeRequestDto(LocalTime.now()));
        Assertions.assertThat(timeId).isEqualTo(1L);
    }

    @DisplayName("예약 시간대 하나를 삭제한다")
    @Test
    void deleteTime() {
        reservationService.deleteTime(1L);
        Assertions.assertThat(reservationService.readTimeAll()).isEmpty();
    }
}