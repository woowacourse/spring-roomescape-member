package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationThemeRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.service.dto.request.CreateReservationServiceRequest;
import roomescape.service.dto.response.ReservationServiceResponse;

class ReservationServiceTest {

    private FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
    private FakeReservationRepository reservationRepository = new FakeReservationRepository();
    private FakeReservationThemeRepository reservationThemeRepository = new FakeReservationThemeRepository();
    private ReservationService reservationService = new ReservationService(reservationRepository,
            reservationTimeRepository, reservationThemeRepository);

    @DisplayName("예약을 저장소에 추가하고 저장된 정보를 가진 객체를 반환한다.")
    @Test
    void create() {
        // given
        reservationTimeRepository.save(new ReservationTime(LocalTime.now().plusHours(1)));
        reservationThemeRepository.save(new ReservationTheme("themeName", "description", "image.png"));
        String name = "이름";
        LocalDate date = LocalDate.now().plusDays(1);
        Long timeId = 1L;
        Long themeId = 1L;
        CreateReservationServiceRequest request = new CreateReservationServiceRequest(name, date, timeId, themeId);

        // when
        ReservationServiceResponse reservationServiceResponse = reservationService.create(request);

        // then
        assertSoftly(softly -> {
            assertThat(reservationServiceResponse.id()).isEqualTo(1);
            assertThat(reservationServiceResponse.name()).isEqualTo(name);
            assertThat(reservationRepository.getAll()).hasSize(1);
        });
    }

    @DisplayName("저장된 모든 예약 객체의 정보를 가진 객체를 반환한다.")
    @Test
    void getAll() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(
                new ReservationTime(LocalTime.now().plusHours(1)));
        ReservationTheme savedTheme = reservationThemeRepository.save(
                new ReservationTheme("themeName", "description", "image.png"));
        String name = "이름";
        LocalDate date = LocalDate.now().plusDays(1);
        reservationRepository.save(new Reservation(name, date, savedTime, savedTheme));
        String name2 = "이름2";
        LocalDate date2 = LocalDate.now().plusDays(2);
        reservationRepository.save(new Reservation(name2, date2, savedTime, savedTheme));

        // when
        List<ReservationServiceResponse> queries = reservationService.getAll();

        // then
        assertThat(queries).hasSize(2);
    }

    @DisplayName("예약을 저장소에서 삭제한다")
    @Test
    void delete() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(
                new ReservationTime(LocalTime.now().plusHours(1)));
        ReservationTheme savedTheme = reservationThemeRepository.save(
                new ReservationTheme("themeName", "description", "image.png"));
        String name = "이름";
        LocalDate date = LocalDate.now().plusDays(1);
        Reservation savedReservation = reservationRepository.save(new Reservation(name, date, savedTime, savedTheme));
        // when
        reservationService.delete(savedReservation.id());
        // then
        assertThat(reservationRepository.getAll()).hasSize(0);
    }

    @DisplayName("예약이 없는 id로 삭제를 시도하면 예외를 발생시킨다")
    @Test
    void deleteException() {
        // given
        ReservationTime savedTime = reservationTimeRepository.save(
                new ReservationTime(LocalTime.now().plusHours(1)));
        ReservationTheme savedTheme = reservationThemeRepository.save(
                new ReservationTheme("themeName", "description", "image.png"));
        String name = "이름";
        LocalDate date = LocalDate.now().plusDays(1);
        reservationRepository.save(new Reservation(name, date, savedTime, savedTheme));

        // when & then
        assertThatThrownBy(() -> reservationService.delete(2L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
