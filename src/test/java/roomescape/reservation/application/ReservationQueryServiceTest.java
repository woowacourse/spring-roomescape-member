package roomescape.reservation.application;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.infrastructure.JdbcReservationQueryDao;
import roomescape.reservation.infrastructure.dto.ReservationDetailData;
import roomescape.reservation.presentation.controller.ReservationSearchCondition;

@ExtendWith(MockitoExtension.class)
class ReservationQueryServiceTest {

    @Mock
    private JdbcReservationQueryDao reservationQueryDao;

    @InjectMocks
    private ReservationQueryService reservationQueryService;

    @DisplayName("검색 조건이 없으면 모든 예약 상세 정보를 조회한다.")
    @Test
    void getReservationDetails_withoutCondition() {
        // given
        ReservationSearchCondition emptyCondition = new ReservationSearchCondition(null, null, null, null);
        List<ReservationDetailData> expected = List.of();
        given(reservationQueryDao.findAllReservationDetails()).willReturn(expected);

        // when
        List<ReservationDetailData> reservationDetails = reservationQueryService.getReservationDetails(emptyCondition);

        // then
        verify(reservationQueryDao).findAllReservationDetails();
    }

    @DisplayName("검색 조건이 있으면 해당 조건으로 예약 상세 정보를 조회한다.")
    @Test
    void getReservationDetails_withCondition() {
        // given
        ReservationSearchCondition condition = new ReservationSearchCondition(1L, null, null, null);
        given(reservationQueryDao.findByCondition(condition))
                .willReturn(List.of(mock(ReservationDetailData.class)));

        // when
        List<ReservationDetailData> reservationDetails = reservationQueryService.getReservationDetails(condition);

        // then
        verify(reservationQueryDao).findByCondition(condition);
    }

    @DisplayName("ID로 예약 상세 정보를 조회할 수 있다.")
    @Test
    void getReservationDetailById() {
        // given
        long id = 1L;
        ReservationDetailData mockData = mock(ReservationDetailData.class);
        given(reservationQueryDao.findReservationDetailById(id)).willReturn(Optional.of(mockData));

        // when
        ReservationDetailData reservationDetailById = reservationQueryService.getReservationDetailById(id);

        // then
        verify(reservationQueryDao).findReservationDetailById(id);
    }
}
