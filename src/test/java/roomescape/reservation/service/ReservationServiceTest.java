package roomescape.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.reservation.exception.ReservationDuplicateException;
import roomescape.fake.ReservationFakeRepository;
import roomescape.fake.ReservationTimeFakeRepository;
import roomescape.fake.ThemeFakeRepository;
import roomescape.reservation.dto.ReservationAvailableTimeResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ReservationServiceTest {

    private final ReservationRepository reservationRepository = new ReservationFakeRepository();
    private final ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
    private final ThemeRepository themeRepository = new ThemeFakeRepository();
    private final ReservationService reservationService = new ReservationService(reservationRepository,
            reservationTimeRepository, themeRepository);

    @Test
    @DisplayName("조회된 엔티티를 DTO로 매핑해 반환한다.")
    void test_readReservations() {
        //given & when
        List<ReservationResponse> actual = reservationService.readReservations();
        //then
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.getFirst().id()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 생성 시, 저장한 엔티티를 DTO로 반환한다.")
    void test_createReservation() {
        //given
        List<ReservationResponse> given = reservationService.readReservations();
        assertThat(given.size()).isEqualTo(1);
        LocalDate givenDate = LocalDate.of(2028, 1, 10);

        //when
        long timeId = 1L;
        long themeId = 1L;
        ReservationRequest request = new ReservationRequest("브라운", givenDate, timeId, themeId);
        ReservationResponse actual = reservationService.createReservation(request);
        //then
        assertThat(actual.id()).isEqualTo(2);
    }

    @Test
    @DisplayName("예약 생성 시, 저장할 날짜가 과거일 경우 예외를 발생한다.")
    void error_createReservationIfBeforeDate() {
        //given
        LocalDate givenDate = LocalDate.MIN;
        long timeId = 1L;
        long themeId = 1L;
        ReservationRequest request = new ReservationRequest("브라운", givenDate, timeId, themeId);

        //when&then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약이 불가능한 시간");
    }

    @Test
    @DisplayName("예약 생성 시, 날짜, 시간, 테마가 중복될 경우 예외가 발생한다.")
    void error_createReservationIfDuplication() {
        //given
        LocalDate givenDate = LocalDate.MAX;
        long timeId = 1L;
        long themeId = 1L;
        ReservationRequest request = new ReservationRequest("브라운", givenDate, timeId, themeId);

        //when&then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(ReservationDuplicateException.class)
                .hasMessageContaining("해당 시각의 중복된 예약이 존재합니다");
    }

    @Test
    @DisplayName("저장소에 없는 값을 삭제하려할 경우, 예외가 발생한다.")
    void error_deleteReservationById() {
        assertThatThrownBy(() -> reservationService.deleteReservationById(Long.MAX_VALUE))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("사용자가 날짜와 테마를 선택하면 예약 가능한 시간들을 DTO로 반환한다.")
    void test_readAvailableReservationTimes() {
        // given
        LocalDate givenDate = LocalDate.MAX;
        Long givenTheme = 1L;

        // when
        List<ReservationAvailableTimeResponse> actual = reservationService.readAvailableReservationTimes(givenDate, givenTheme);

        // then
        Optional<ReservationAvailableTimeResponse> bookedResponseOpt = actual.stream()
                .filter(current -> current.startAt().equals(LocalTime.MAX))
                .findFirst();
        assertThat(bookedResponseOpt).isPresent();
        assertThat(bookedResponseOpt.get().isBooked()).isTrue();

        Optional<ReservationAvailableTimeResponse> availableResponseOpt = actual.stream()
                .filter(current -> current.startAt().equals(LocalTime.of(11, 0)))
                .findFirst();
        assertThat(availableResponseOpt).isPresent();
        assertThat(availableResponseOpt.get().isBooked()).isFalse();
    }
}
