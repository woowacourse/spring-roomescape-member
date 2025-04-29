package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.exception.EntityNotFoundException;
import roomescape.domain.reservation.dto.ReservationRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationTimeResponse;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.service.ReservationService;
import roomescape.reservation.repository.fake.FakeReservationRepository;
import roomescape.reservation.repository.fake.FakeReservationTimeRepository;
import roomescape.utils.FixedClock;

class ReservationServiceTest {

    private static final LocalTime time = LocalTime.of(20, 0);

    private final FakeReservationRepository reservationRepository = new FakeReservationRepository();
    private final FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();

    private ReservationService reservationService;
    private ReservationTime reservationTime;

    private Long reservationTimeId;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        reservationTimeRepository.deleteAll();

        Clock clock = FixedClock.from(LocalDateTime.of(2024, 12, 18, 8, 0));

        reservationService = new ReservationService(clock, reservationRepository, reservationTimeRepository);

        ReservationTime saved = reservationTimeRepository.save(ReservationTime.withoutId(time));
        reservationTimeId = saved.getId();
        reservationTime = new ReservationTime(saved.getId(), saved.getStartAt());
    }

    @DisplayName("모든 예약 정보를 가져온다.")
    @Test
    void test1() {
        // given
        List<String> names = List.of("꾹", "헤일러", "라젤");
        LocalDate date = LocalDate.of(2020, 1, 1);

        for (String name : names) {
            Reservation reservation = Reservation.withoutId(name, date, reservationTime);
            reservationRepository.save(reservation);
        }

        // when
        List<ReservationResponse> result = reservationService.getAll();
        List<String> resultNames = result.stream().map(ReservationResponse::name).toList();

        assertThat(resultNames).containsExactlyInAnyOrderElementsOf(names);
    }

    @DisplayName("예약 정보가 없다면 빈 리스트를 반환한다.")
    @Test
    void test2() {
        List<ReservationResponse> result = reservationService.getAll();

        assertThat(result).isEmpty();
    }

    @DisplayName("예약을 추가한다.")
    @Test
    void test3() {
        // given
        String name = "꾹";
        LocalDate date = LocalDate.of(2025, 1, 1);

        ReservationRequest requestDto = new ReservationRequest(name, date, reservationTimeId);

        // when
        ReservationResponse result = reservationService.create(requestDto);

        // then
        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(result.name()).isEqualTo(name);
        softAssertions.assertThat(result.date()).isEqualTo(date);
        softAssertions.assertThat(result.time()).isEqualTo(new ReservationTimeResponse(reservationTimeId, time));

        softAssertions.assertAll();
    }

    @DisplayName("과거 날짜에 예약을 추가하면 예외가 발생한다.")
    @Test
    void test4() {
        // given
        String name = "꾹";
        LocalDate date = LocalDate.of(2024, 12, 18);
        ReservationTime pastTime = new ReservationTime(2L, LocalTime.of(7, 59));
        reservationTimeRepository.add(pastTime);

        ReservationRequest requestDto = new ReservationRequest(name, date, pastTime.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.create(requestDto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 예약 시간 ID로 저장하면 예외를 반환한다.")
    @Test
    void test5() {
        LocalDate date = LocalDate.of(2025, 4, 29);

        Long notExistId = 1000L;
        ReservationRequest requestDto = new ReservationRequest("꾹", date, notExistId);

        assertThatThrownBy(() -> reservationService.create(requestDto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void test6() {
        // given
        Reservation reservation = Reservation.withoutId("꾹", LocalDate.now(), reservationTime);
        Reservation saved = reservationRepository.save(reservation);

        Long id = saved.getId();

        // then
        assertThatCode(() -> reservationService.delete(id))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약 시간이 존재하지 않으면 예외를 반환한다.")
    @Test
    void test7() {
        Long id = 1L;
        assertThatThrownBy(() -> reservationService.delete(id))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
