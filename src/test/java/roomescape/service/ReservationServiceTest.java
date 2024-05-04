package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static roomescape.TestFixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservedThemeResponse;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationDao;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("예약을 생성한다.")
    void create() {
        // given
        Reservation reservation = MIA_RESERVATION();

        given(reservationDao.save(reservation))
                .willReturn(new Reservation(1L, reservation));

        // when
        ReservationResponse response = reservationService.create(reservation);

        // then
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("동일한 테마, 날짜, 시간에 한 팀만 예약할 수 있다.")
    void createSameReservation() {
        // given
        ReservationTime miaReservationTime = new ReservationTime(1L, MIA_RESERVATION_TIME);
        Reservation miaReservation = MIA_RESERVATION(miaReservationTime, WOOTECO_THEME(1L));

        given(reservationDao.findAllByDateAndTimeAndThemeId(any(), any(), anyLong()))
                .willReturn(List.of(miaReservation));

        // when & then
        assertThatThrownBy(() -> reservationService.create(miaReservation))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다.")
    void getAll() {
        // given
        Reservation miaReservation = MIA_RESERVATION();
        Reservation tommyReservation = TOMMY_RESERVATION();

        given(reservationDao.findAll())
                .willReturn(List.of(miaReservation, tommyReservation));

        // when
        List<ReservationResponse> reservations = reservationService.findAll();

        // then
        assertAll(() -> {
            assertThat(reservations).hasSize(2)
                    .extracting(ReservationResponse::name)
                    .containsExactly(USER_MIA, USER_TOMMY);
            assertThat(reservations).extracting(ReservationResponse::time)
                    .extracting(ReservationTimeResponse::startAt)
                    .containsExactly(MIA_RESERVATION_TIME, TOMMY_RESERVATION_TIME);
            assertThat(reservations).extracting(ReservationResponse::theme)
                    .extracting(ReservedThemeResponse::name)
                    .containsExactly(WOOTECO_THEME_NAME, WOOTECO_THEME_NAME);
        });
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        // given
        Long existingId = 1L;

        given(reservationDao.existById(existingId))
                .willReturn(true);

        // when & then
        assertThatCode(() -> reservationService.delete(existingId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 예약 Id로 삭제할 수 없다.")
    void deleteNotExistingId() {
        // given
        Long notExistingId = 1L;

        given(reservationDao.existById(notExistingId))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> reservationService.delete(notExistingId))
                .isInstanceOf(NotFoundException.class);
    }
}
