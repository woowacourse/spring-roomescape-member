package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.application.ReservationService;
import roomescape.application.ServiceTest;
import roomescape.application.dto.ReservationRequest;
import roomescape.exception.RoomescapeErrorCode;
import roomescape.exception.RoomescapeException;

@ServiceTest
class ReservationFactoryTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationCommandRepository reservationCommandRepository;

    @Autowired
    private ReservationQueryRepository reservationQueryRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("정상적인 예약 요청을 받아서 저장한다.")
    @Test
    void shouldReturnReservationResponseWhenValidReservationRequestSave() {
        ReservationTime time = reservationTimeRepository.create(ReservationTimeFixture.defaultValue());
        Theme theme = themeRepository.create(ThemeFixture.defaultValue());
        ReservationRequest reservationRequest = ReservationRequestFixture.defaultValue(time, theme);

        reservationService.create(reservationRequest);

        List<Reservation> reservations = reservationQueryRepository.findAll();
        Reservation reservation = reservations.get(0);
        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservation.getId()).isNotNull(),
                () -> assertThat(reservation.getName()).isEqualTo(reservationRequest.name()),
                () -> assertThat(reservation.getTheme().getId()).isEqualTo(reservationRequest.themeId()),
                () -> assertThat(reservation.getDate()).isEqualTo(reservationRequest.date()),
                () -> assertThat(reservation.getTime().getId()).isEqualTo(reservationRequest.timeId())
        );
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약을 생성시 예외를 반환한다.")
    @Test
    void shouldReturnIllegalArgumentExceptionWhenNotFoundReservationTime() {
        Theme savedTheme = themeRepository.create(ThemeFixture.defaultValue());
        ReservationRequest request = ReservationRequestFixture.of(99L, savedTheme.getId());

        assertThatCode(() -> reservationService.create(request))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(RoomescapeErrorCode.NOT_FOUND_TIME);
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약을 생성시 예외를 반환한다.")
    void shouldThrowIllegalArgumentExceptionWhenNotFoundTheme() {
        ReservationTime time = reservationTimeRepository.create(ReservationTimeFixture.defaultValue());
        ReservationRequest request = ReservationRequestFixture.of(time.getId(), 1L);

        assertThatCode(() -> reservationService.create(request))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(RoomescapeErrorCode.NOT_FOUND_THEME);
    }

    @DisplayName("중복된 예약을 하는 경우 예외를 반환한다.")
    @Test
    void shouldReturnIllegalStateExceptionWhenDuplicatedReservationCreate() {
        ReservationTime time = reservationTimeRepository.create(ReservationTimeFixture.defaultValue());
        Theme theme = themeRepository.create(ThemeFixture.defaultValue());
        ReservationRequest request = ReservationRequestFixture.defaultValue(time, theme);

        reservationCommandRepository.create(request.toReservation(time, theme));

        assertThatCode(() -> reservationService.create(request))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(RoomescapeErrorCode.DUPLICATED_RESERVATION);
    }

    @DisplayName("과거 시간을 예약하는 경우 예외를 반환한다.")
    @Test
    void shouldThrowsIllegalArgumentExceptionWhenReservationDateIsBeforeCurrentDate() {
        ReservationTime time = reservationTimeRepository.create(ReservationTimeFixture.defaultValue());
        Theme theme = themeRepository.create(ThemeFixture.defaultValue());
        ReservationRequest reservationRequest = ReservationRequestFixture.of(LocalDate.of(1999, 1, 1), time.getId(), theme.getId());

        assertThatCode(() -> reservationService.create(reservationRequest))
                .isInstanceOf(RoomescapeException.class)
                .extracting("errorCode")
                .isEqualTo(RoomescapeErrorCode.BAD_REQUEST);
    }
}
