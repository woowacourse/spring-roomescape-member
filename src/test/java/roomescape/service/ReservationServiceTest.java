/*
package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.TestFixtures;
import roomescape.controller.response.ReservationResponse;

class ReservationServiceTest {
    private final FakeReservationRepository fakeReservationRepository = new FakeReservationRepository();
    private final FakeReservationTimeRepository fakeReservationTimeRepository = new FakeReservationTimeRepository();
    private final FakeThemeRepository fakeThemeRepository = new FakeThemeRepository();
    private final ReservationService reservationService = new ReservationService(
            fakeReservationRepository,
            fakeReservationTimeRepository,
            fakeThemeRepository
    );

    @BeforeEach
    void setUp() {
        fakeReservationRepository.deleteAll();
        fakeReservationTimeRepository.deleteAll();
        fakeThemeRepository.deleteAll();
        fakeReservationTimeRepository.save(TestFixtures.TIME_1);
        fakeReservationTimeRepository.save(TestFixtures.TIME_2);
        fakeReservationTimeRepository.save(TestFixtures.TIME_3);
        fakeThemeRepository.save(TestFixtures.THEME_1);
        fakeThemeRepository.save(TestFixtures.THEME_2);
        fakeThemeRepository.save(TestFixtures.THEME_3);
        fakeReservationRepository.save(TestFixtures.RESERVATION_1);
        fakeReservationRepository.save(TestFixtures.RESERVATION_2);
    }

    @DisplayName("전체 예약을 반환한다")
    @Test
    void findAll() {
        List<ReservationResponse> reservationResponses = reservationService.findAll();

        assertThat(reservationResponses).isEqualTo(List.of(TestFixtures.RESERVATION_RESPONSE_1, TestFixtures.RESERVATION_RESPONSE_2));
    }

    @DisplayName("예약을 저장한다")
    @Test
    void save() {
        reservationService.saveByUser(TestFixtures.RESERVATION_REQUEST_3);

        assertThat(reservationService.findAll()).isEqualTo(List.of(TestFixtures.RESERVATION_RESPONSE_1, TestFixtures.RESERVATION_RESPONSE_2, TestFixtures.RESERVATION_RESPONSE_3));
    }

    @DisplayName("과거 시간의 예약을 저장할 경우 예외를 발생시킨다")
    @Test
    void save2() {
        assertThatThrownBy(() -> reservationService.saveByUser(TestFixtures.PAST_RESERVATION_REQUEST))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 지난 시간입니다.");
    }

    @DisplayName("이미 저장된 예약을 저장할 시 예외를 발생시킨다")
    @Test
    void save3() {
        assertThatThrownBy(() -> reservationService.saveByUser(TestFixtures.RESERVATION_REQUEST_2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 예약이 존재합니다.");
    }

    @DisplayName("해당 id의 예약을 삭제한다")
    @Test
    void deleteById() {
        reservationService.deleteById(1L);

        assertThat(reservationService.findAll()).isEqualTo(List.of(TestFixtures.RESERVATION_RESPONSE_2));
    }
}
*/
