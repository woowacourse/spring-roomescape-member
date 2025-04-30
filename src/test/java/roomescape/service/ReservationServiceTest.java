package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.exceptions.ReservationDuplicateException;
import roomescape.fake.ReservationFakeRepository;
import roomescape.fake.ReservationTimeFakeRepository;
import roomescape.fake.ThemeFakeRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

public class ReservationServiceTest {

    private final ReservationRepository reservationRepository = new ReservationFakeRepository();
    private final ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
    private final ThemeRepository themeRepository = new ThemeFakeRepository();
    private final ReservationService reservationService = new ReservationService(reservationRepository,
            reservationTimeRepository, themeRepository);

    @Test
    @DisplayName("조회된 엔티티를 DTO로 매핑해 반환한다.")
    void test_readReservation() {
        //given & when
        List<ReservationResponse> actual = reservationService.readReservation();
        //then
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.getFirst().id()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 생성 시, 저장한 엔티티를 DTO로 반환한다.")
    void test_postReservation() {
        //given
        List<ReservationResponse> given = reservationService.readReservation();
        assertThat(given.size()).isEqualTo(1);
        LocalDate givenDate = LocalDate.of(2028, 1, 10);

        //when
        long timeId = 1L;
        long themeId = 1L;
        ReservationRequest request = new ReservationRequest("브라운", givenDate, timeId, themeId);
        ReservationResponse actual = reservationService.postReservation(request);
        //then
        assertThat(actual.id()).isEqualTo(2);
    }

    @Test
    @DisplayName("예약 생성 시, 저장할 날짜가 과거일 경우 예외를 발생한다.")
    void error_postReservationIfBeforeDate() {
        //given
        LocalDate givenDate = LocalDate.MIN;
        long timeId = 1L;
        long themeId = 1L;
        ReservationRequest request = new ReservationRequest("브라운", givenDate, timeId, themeId);

        //when&then
        assertThatThrownBy(() -> reservationService.postReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약이 불가능한 시간");
    }

    @Test
    @DisplayName("예약 생성 시, 중복된 날짜,시간일 경우 예외가 발생한다.")
    void error_postReservationIfDuplicationDatetime() {
        //given
        LocalDate givenDate = LocalDate.MAX;
        long timeId = 1L;
        long themeId = 1L;
        ReservationRequest request = new ReservationRequest("브라운", givenDate, timeId, themeId);

        //when&then
        assertThatThrownBy(() -> reservationService.postReservation(request))
                .isInstanceOf(ReservationDuplicateException.class)
                .hasMessageContaining("해당 시각의 중복된 예약이 존재합니다");
    }

    @Test
    @DisplayName("저장소에 없는 값을 삭제하려할 경우, 예외가 발생한다.")
    void test_deleteReservation() {
        assertThatThrownBy(() -> reservationService.deleteReservation(Long.MAX_VALUE))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
