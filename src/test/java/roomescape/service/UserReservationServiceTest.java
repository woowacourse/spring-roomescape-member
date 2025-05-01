package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.service.dto.request.CreateReservationServiceRequest;
import roomescape.service.dto.response.ReservationServiceResponse;

class UserReservationServiceTest {

    private FakeReservationTimeRepository fakeReservationTimeRepository = new FakeReservationTimeRepository();
    private FakeReservationRepository fakeReservationRepository = new FakeReservationRepository();
    private UserReservationService userReservationService = new UserReservationService(fakeReservationRepository,
            fakeReservationTimeRepository);

    @BeforeEach
    void setUp() {
        FakeReservationTimeRepository.clear();
        FakeReservationRepository.clear();
    }

    @DisplayName("예약을 저장소에 추가하고 저장된 정보를 가진 객체를 반환한다.")
    @Test
    void create() {
        // given
        fakeReservationTimeRepository.save(new ReservationTime(LocalTime.now().plusHours(1)));
        String name = "이름";
        LocalDate date = LocalDate.now().plusDays(1);
        Long timeId = 1L;
        CreateReservationServiceRequest command = new CreateReservationServiceRequest(name, date, timeId);

        // when
        ReservationServiceResponse reservationServiceResponse = userReservationService.create(command);

        // then
        assertSoftly(softly -> {
            assertThat(reservationServiceResponse.id()).isEqualTo(1);
            assertThat(reservationServiceResponse.name()).isEqualTo(name);
            assertThat(fakeReservationRepository.getAll()).hasSize(1);
        });
    }

    @DisplayName("저장된 모든 예약 객체의 정보를 가진 객체를 반환한다.")
    @Test
    void getAll() {
        // given
        ReservationTime savedTime = fakeReservationTimeRepository.save(
                new ReservationTime(LocalTime.now().plusHours(1)));
        String name = "이름";
        LocalDate date = LocalDate.now().plusDays(1);
        fakeReservationRepository.save(new Reservation(name, date, savedTime));
        String name2 = "이름2";
        LocalDate date2 = LocalDate.now().plusDays(2);
        fakeReservationRepository.save(new Reservation(name2, date2, savedTime));

        // when
        List<ReservationServiceResponse> queries = userReservationService.getAll();

        // then
        assertThat(queries).hasSize(2);
    }

    @DisplayName("예약을 저장소에서 삭제한다")
    @Test
    void delete() {
        // given
        ReservationTime savedTime = fakeReservationTimeRepository.save(
                new ReservationTime(LocalTime.now().plusHours(1)));
        String name = "이름";
        LocalDate date = LocalDate.now().plusDays(1);
        Reservation savedReservation = fakeReservationRepository.save(new Reservation(name, date, savedTime));
        // when
        userReservationService.delete(savedReservation.id());
        // then
        assertThat(fakeReservationRepository.getAll()).hasSize(0);
    }

    @DisplayName("예약이 없는 id로 삭제를 시도하면 예외를 발생시킨다")
    @Test
    void deleteException() {
        // given
        ReservationTime savedTime = fakeReservationTimeRepository.save(
                new ReservationTime(LocalTime.now().plusHours(1)));
        String name = "이름";
        LocalDate date = LocalDate.now().plusDays(1);
        fakeReservationRepository.save(new Reservation(name, date, savedTime));

        // when & then
        assertThatThrownBy(() -> userReservationService.delete(2L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
