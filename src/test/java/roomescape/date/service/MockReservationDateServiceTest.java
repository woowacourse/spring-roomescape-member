package roomescape.date.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.date.domain.ReservationDate;
import roomescape.date.fixture.ReservationDateFixture;
import roomescape.date.repository.ReservationDateRepository;
import roomescape.reservation.repository.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MockReservationDateServiceTest {

    private static final LocalDate DEFAULT_DATE = LocalDate.of(2099, 1, 1);

    @Mock
    private ReservationDateRepository reservationDateRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationDateService reservationDateService;

    @Test
    @DisplayName("등록된 예약날짜가 여러개이면 조회 시 등록된 개수만큼 반환한다.")
    void read_dates_by_mock() {
        // given
        List<ReservationDate> reservationDates = List.of(
                ReservationDateFixture.oneWeekLater(),
                ReservationDateFixture.twoWeeksLater()
        );

        when(reservationDateRepository.findAll())
                .thenReturn(reservationDates);

        // when
        List<ReservationDate> actual = reservationDateService.readDates();

        // then
        Assertions.assertThat(actual)
                .hasSize(reservationDates.size());

        verify(reservationDateRepository).findAll();
    }

    @Test
    @DisplayName("등록된 예약날짜와 조회된 예약날짜의 모든 필드는 일치한다")
    void readDate() {
        // given
        Long dateId = 1L;
        ReservationDate saved = ReservationDate.load(dateId, DEFAULT_DATE);
        when(reservationDateRepository.findById(dateId))
                .thenReturn(Optional.of(saved));

        // when
        ReservationDate actual = reservationDateService.readDate(saved.id());

        // then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(saved);

        verify(reservationDateRepository).findById(dateId);
    }

    @Test
    @DisplayName("등록되지 않은 예약날짜를 조회하면 예외가 발생한다.")
    void readDate_deregistered() {
        // given
        Long deregisteredId = Long.MIN_VALUE;
        when(reservationDateRepository.findById(deregisteredId))
                .thenThrow(new IllegalArgumentException("등록되지 않은 예약날짜입니다."));

        // when & then
        Assertions.assertThatThrownBy(() -> reservationDateService.readDate(deregisteredId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 예약날짜입니다.");

        verify(reservationDateRepository).findById(deregisteredId);
    }

    @Test
    @DisplayName("예약날짜를 등록하면 저장된 예약날짜를 반환한다.")
    void register_returns_saved_date() {
        // given
        ReservationDate savedDate = ReservationDateFixture.oneWeekLater();
        when(reservationDateRepository.save(any(ReservationDate.class)))
                .thenReturn(savedDate);

        // when
        ReservationDate registered = reservationDateService.register(DEFAULT_DATE);

        // then
        assertThat(registered)
                .usingRecursiveComparison()
                .isEqualTo(savedDate);

        verify(reservationDateRepository).save(any(ReservationDate.class));
    }

    @Test
    @DisplayName("등록된 예약 날짜를 삭제한다.")
    void delete() {
        // given
        Long registeredId = 1L;
        ReservationDate reservationDate = ReservationDate.load(registeredId, DEFAULT_DATE);

        when(reservationDateRepository.findById(registeredId))
                .thenReturn(Optional.of(reservationDate));

        when(reservationRepository.existsByDateId(registeredId))
                .thenReturn(false);

        when(reservationDateRepository.delete(registeredId))
                .thenReturn(true);

        // when
        ReservationDate actual = reservationDateService.deregister(registeredId);

        // then
        assertThat(actual)
                .isEqualTo(reservationDate);

        verify(reservationDateRepository).findById(registeredId);
        verify(reservationRepository).existsByDateId(registeredId);
        verify(reservationDateRepository).delete(registeredId);
    }

    @Test
    @DisplayName("등록되지않은 예약날짜를 삭제하면 예외가 발생한다.")
    void delete_deregistered() {
        // given
        Long deregisteredId = Long.MIN_VALUE;
        when(reservationDateRepository.findById(deregisteredId))
                .thenThrow(new IllegalArgumentException("등록되지 않은 예약날짜입니다."));

        // when  & then
        Assertions.assertThatThrownBy(() -> reservationDateService.deregister(deregisteredId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되지 않은 예약날짜입니다.");

        verify(reservationDateRepository).findById(deregisteredId);
    }

    @Test
    @DisplayName("해당 날짜에 예약이 존재하면 예약날짜를 삭제할 수 없다.")
    void delete_already_reserved() {
        // given
        Long dateId = 1L;
        ReservationDate reservationDate = ReservationDate.load(dateId, DEFAULT_DATE);

        when(reservationDateRepository.findById(dateId))
                .thenReturn(Optional.of(reservationDate));

        when(reservationRepository.existsByDateId(dateId))
                .thenReturn(true);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationDateService.deregister(dateId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 날짜에 예약이 존재하여, 삭제할 수 없습니다.");

        verify(reservationDateRepository).findById(dateId);
        verify(reservationRepository).existsByDateId(dateId);
    }

}
