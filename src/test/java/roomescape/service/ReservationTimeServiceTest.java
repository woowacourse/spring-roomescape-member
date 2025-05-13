package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.controller.reservationTime.dto.ReservationTimeRequestDto;
import roomescape.controller.reservationTime.dto.ReservationTimeResponseDto;
import roomescape.model.ReservationTime;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeDao reservationTimeDao;

    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @DisplayName("시간을 저장")
    @Test
    void test() {
        // given
        ReservationTimeRequestDto request = new ReservationTimeRequestDto(LocalTime.of(10, 0));
        when(reservationTimeDao.isDuplicatedStartAtExisted(LocalTime.of(10, 0))).thenReturn(false);
        when(reservationTimeDao.saveTime(any())).thenReturn(1L);

        // when
        ReservationTimeResponseDto response = reservationTimeService.saveTime(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0));

        verify(reservationTimeDao).isDuplicatedStartAtExisted(LocalTime.of(10, 0));
        verify(reservationTimeDao).saveTime(any());
    }

    @DisplayName("모든 시간을 조회한다")
    @Test
    void test1() {
        // given
        List<ReservationTime> fakeList = List.of(
                new ReservationTime(1L, LocalTime.of(10, 0)),
                new ReservationTime(2L, LocalTime.of(12, 0))
        );
        when(reservationTimeDao.findAll()).thenReturn(fakeList);

        // when
        List<ReservationTimeResponseDto> times = reservationTimeService.getAllTimes();

        // then
        assertThat(times).hasSize(2);
        assertThat(times).extracting("startAt")
                .containsExactlyInAnyOrder(LocalTime.of(10, 0), LocalTime.of(12, 0));

        verify(reservationTimeDao).findAll();
    }

    @DisplayName("시간을 삭제한다")
    @Test
    void test2() {
        // given
        Long idToDelete = 1L;
        when(reservationDao.existsByReservationTimeId(idToDelete)).thenReturn(false);

        // when
        reservationTimeService.deleteTime(idToDelete);

        // then
        verify(reservationDao).existsByReservationTimeId(idToDelete);
        verify(reservationTimeDao).deleteTimeById(idToDelete);
    }
}