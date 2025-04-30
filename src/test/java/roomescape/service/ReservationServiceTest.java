package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.test.fixture.ReservationTimeFixture.addReservationTimeInRepository;
import static roomescape.test.fixture.ThemeFixture.addThemeInRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationCreationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.test.fake.FakeReservationRepository;
import roomescape.test.fake.FakeReservationTimeRepository;
import roomescape.test.fake.FakeThemeRepository;

class ReservationServiceTest {

    private static final LocalDate NEXT_DATE = LocalDate.now().plusDays(1);
    private static final LocalTime PAST_TIME = LocalTime.now().minusSeconds(1);

    private final ReservationRepository reservationRepository = new FakeReservationRepository();
    private final ReservationTimeRepository timeRepository = new FakeReservationTimeRepository();
    private final ThemeRepository themeRepository = new FakeThemeRepository();
    private final ReservationService reservationService = new ReservationService(reservationRepository, timeRepository, themeRepository);

    @DisplayName("저장된 예약들을 조회할 수 있다")
    @Test
    void getReservations() {
        ReservationTime time = addReservationTimeInRepository(timeRepository, LocalTime.now());
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");
        reservationRepository.add(Reservation.createWithoutId("reservation1", NEXT_DATE, time, theme));
        reservationRepository.add(Reservation.createWithoutId("reservation2", NEXT_DATE, time, theme));
        reservationRepository.add(Reservation.createWithoutId("reservation3", NEXT_DATE, time, theme));

        List<Reservation> allReservations = reservationService.getAllReservations();

        assertThat(allReservations).hasSize(3);
    }

    @DisplayName("예약을 추가할 수 있다.")
    @Test
    void createReservation() {
        ReservationTime time = addReservationTimeInRepository(timeRepository, LocalTime.now());
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");
        ReservationCreationRequest request =
                new ReservationCreationRequest("reservation1", NEXT_DATE, time.getId(), theme.getId());

        long savedId = reservationService.saveReservation(request);

        Reservation savedReservation = reservationRepository.findAll().getFirst();
        Reservation expectedReservation = new Reservation(1L, request.getName(), request.getDate(), time, theme);
        assertAll(
                () -> assertThat(savedId).isEqualTo(1L),
                () -> assertThat(savedReservation).isEqualTo(expectedReservation)
        );
    }

    @DisplayName("과거 날짜와 시간으로는 예약을 추가할 수 없다.")
    @Test
    void canNotCreateReservationWithPastDateTime() {
        ReservationTime pastTime = addReservationTimeInRepository(timeRepository, PAST_TIME);
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");
        ReservationCreationRequest request =
                new ReservationCreationRequest("reservation1", LocalDate.now(), pastTime.getId(), theme.getId());

        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 과거의 날짜와 시간입니다.");
    }

    @DisplayName("이미 예약한 날짜와 시간으로는 예약이 불가능하다")
    @Test
    void canNotCreateReservationWithSameDateTime() {
        LocalDate sameDate = NEXT_DATE;
        ReservationTime sameTime = addReservationTimeInRepository(timeRepository, LocalTime.of(10, 0));
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");

        reservationRepository.add(Reservation.createWithoutId("reservation1", sameDate, sameTime, theme));

        ReservationCreationRequest request =
                new ReservationCreationRequest("reservation2", sameDate, sameTime.getId(), theme.getId());

        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 예약이 완료된 날짜와 시간입니다.");
    }

    @DisplayName("특정 ID의 예약을 삭제할 수 있다.")
    @Test
    void deleteReservation() {
        ReservationTime time = addReservationTimeInRepository(timeRepository, LocalTime.now());
        Theme theme = addThemeInRepository(themeRepository, "이름", "설명", "썸네일");

        reservationRepository.add(Reservation.createWithoutId("reservation1", NEXT_DATE, time, theme));
        reservationRepository.add(Reservation.createWithoutId("reservation2", NEXT_DATE, time, theme));
        reservationRepository.add(Reservation.createWithoutId("reservation3", NEXT_DATE, time, theme));
        long deletedId = reservationRepository.findAll().getFirst().getId();

        reservationService.deleteReservation(deletedId);

        assertThat(reservationRepository.findAll())
                .extracting(Reservation::getId)
                .doesNotContain(deletedId);
    }

    @DisplayName("존재하지 않는 예약을 삭제하려고 할 경우 예외를 발생시킨다")
    @Test
    void deleteNoneExistentReservation() {
        long noneExistentId = 1L;
        assertThatThrownBy(() -> reservationService.deleteReservation(noneExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] ID에 해당하는 예약이 존재하지 않습니다.");
    }
}
