package roomescape.time.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.DuplicateResourceException;
import roomescape.time.controller.dto.ReservationTimeRequest;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeService = new ReservationTimeService(reservationTimeRepository);
    }

    @Nested
    @DisplayName("save 메서드는")
    class Save {

        @Test
        @DisplayName("새로운 예약 시간을 입력받으면 성공적으로 저장한다.")
        void saveSuccess() {
            // given
            LocalTime startAt = LocalTime.of(10, 0);
            ReservationTimeRequest request = new ReservationTimeRequest(startAt);
            given(reservationTimeRepository.existsByStartAt(startAt)).willReturn(false);

            ReservationTime savedTime = new ReservationTime(1L, startAt);
            given(reservationTimeRepository.save(any(ReservationTime.class))).willReturn(savedTime);

            // when
            ReservationTime result = reservationTimeService.save(request);

            // then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getStartAt()).isEqualTo(startAt);
            verify(reservationTimeRepository, times(1)).save(any(ReservationTime.class));
        }

        @Test
        @DisplayName("이미 존재하는 예약 시간이면 DuplicateResourceException이 발생한다.")
        void saveFailByDuplicateTime() {
            // given
            LocalTime startAt = LocalTime.of(10, 0);
            ReservationTimeRequest request = new ReservationTimeRequest(startAt);
            given(reservationTimeRepository.existsByStartAt(startAt)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> reservationTimeService.save(request))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("해당 시간이 이미 존재합니다.");

            verify(reservationTimeRepository, never()).save(any(ReservationTime.class));
        }
    }

    @Nested
    @DisplayName("findAvailableTimes 메서드는")
    class FindAvailableTimes {

        @Test
        @DisplayName("테마 ID와 날짜를 전달받아 예약 가능한 시간 목록을 조회한다.")
        void findAvailableTimesCallsRepository() {
            // given
            Long themeId = 1L;
            LocalDate date = LocalDate.of(2026, 5, 7);
            ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));

            given(reservationTimeRepository.findAvailableTimes(themeId, date))
                    .willReturn(List.of(time));

            // when
            List<ReservationTime> result = reservationTimeService.findAvailableTimes(themeId, date);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getStartAt()).isEqualTo(LocalTime.of(10, 0));
            verify(reservationTimeRepository).findAvailableTimes(themeId, date);
        }
    }

    @Nested
    @DisplayName("getById 메서드는")
    class GetById {

        @Test
        @DisplayName("존재하는 ID로 조회하면 예약 시간을 반환한다.")
        void getByIdSuccess() {
            // given
            Long id = 1L;
            ReservationTime time = new ReservationTime(id, LocalTime.of(10, 0));
            given(reservationTimeRepository.findById(id)).willReturn(Optional.of(time));

            // when
            ReservationTime result = reservationTimeService.getById(id);

            // then
            assertThat(result.getId()).isEqualTo(id);
            assertThat(result.getStartAt()).isEqualTo(LocalTime.of(10, 0));
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 IllegalArgumentException이 발생한다.")
        void getByIdFail() {
            // given
            Long id = 999L;
            given(reservationTimeRepository.findById(id)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reservationTimeService.getById(id))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("해당 ID의 예약 시간이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("deleteById 메서드는")
    class DeleteById {

        @Test
        @DisplayName("ID를 전달받아 레포지토리에 삭제를 위임한다.")
        void deleteByIdDelegatesToRepository() {
            // when
            reservationTimeService.deleteById(1L);

            // then
            verify(reservationTimeRepository, times(1)).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("findAll 메서드는")
    class FindAll {

        @Test
        @DisplayName("저장된 모든 예약 시간 목록을 반환한다.")
        void findAllReturnsList() {
            // given
            given(reservationTimeRepository.findAll()).willReturn(List.of(
                    new ReservationTime(1L, LocalTime.of(10, 0)),
                    new ReservationTime(2L, LocalTime.of(13, 0))
            ));

            // when
            List<ReservationTime> result = reservationTimeService.findAll();

            // then
            assertThat(result).hasSize(2);
            verify(reservationTimeRepository).findAll();
        }
    }
}
