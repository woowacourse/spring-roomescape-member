package roomescape.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.application.dto.ReservationRequest;
import roomescape.application.dto.ReservationResponse;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private ReservationTimeDao reservationTimeDao;

    @Mock
    private ThemeDao themeDao;

    @Test
    @DisplayName("예약 목록 전체를 반환한다")
    void findAllReservations() {
        // given
        List<Reservation> reservations = List.of(
                new Reservation(1L, "name1", LocalDate.now(), new ReservationTime(1L, LocalTime.of(10, 0)),
                        new Theme(1L, "테마1", "설명1", "썸네일1")),
                new Reservation(2L, "name2", LocalDate.now().plusDays(1), new ReservationTime(2L, LocalTime.of(12, 0)),
                        new Theme(2L, "테마2", "설명2", "썸네일2")),
                new Reservation(3L, "name3", LocalDate.now().plusDays(2), new ReservationTime(3L, LocalTime.of(14, 0)),
                        new Theme(3L, "테마3", "설명3", "썸네일3"))
        );

        doReturn(reservations).when(reservationDao)
                .findAll();

        // when
        List<ReservationResponse> responses = reservationService.findAllReservations();

        // then
        assertThat(responses)
                .hasSize(reservations.size())
                .extracting("id", "name", "date")
                .containsExactly(
                        tuple(1L, "name1", LocalDate.now()),
                        tuple(2L, "name2", LocalDate.now().plusDays(1)),
                        tuple(3L, "name3", LocalDate.now().plusDays(2))
                );

        // verify
        verify(reservationDao, times(1)).findAll();
    }

    @Test
    @DisplayName("예약을 추가한다")
    void createReservation() {
        // given
        Member member = new Member("moda", "test@email.com", "password");
        ReservationRequest request = new ReservationRequest(LocalDate.now(), 1L, 1L);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.now());
        Theme theme = new Theme(1L, "테마", "설명", "썸네일");

        doReturn(reservationTime).when(reservationTimeDao)
                .findById(1L);

        doReturn(theme).when(themeDao)
                .findById(1L);

        doReturn(1L).when(reservationDao)
                .save(any());

        // when
        ReservationResponse response = reservationService.createReservation(member, request);

        // then
        assertThat(response)
                .extracting("id", "name", "date")
                .containsExactly(1L, "name", LocalDate.now());

        // verify
        verify(reservationTimeDao, times(1)).findById(1L);
        verify(themeDao, times(1)).findById(1L);
        verify(reservationDao, times(1)).save(any());
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void deleteReservation() {
        // given
        long deleteId = 1L;

        doNothing().when(reservationDao)
                .deleteById(deleteId);

        // when
        reservationService.deleteReservation(deleteId);

        // verify
        verify(reservationDao, times(1)).deleteById(deleteId);
    }
}
