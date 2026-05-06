package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.customException.ReservationTimeException;
import roomescape.FakeReservationRepository;
import roomescape.FakeReservationTimeRepository;
import roomescape.entity.ReservationRepository;
import roomescape.entity.ReservationTimeRepository;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationTimeRequest;
import roomescape.presentation.dto.ReservationTimeResponse;

class ReservationTimeServiceTest {

    private static final LocalDate TODAY = LocalDate.now();

    private ReservationRepository reservationRepository;
    private ReservationService reservationService;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository);
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    private ReservationRequest createReservationRequest(ReservationTimeResponse time) {
        return new ReservationRequest(
                "브라운",
                TODAY,
                time.id()
        );
    }

    private ReservationTimeRequest createReservationTimeRequest() {
        return new ReservationTimeRequest(LocalTime.of(10, 0));
    }

    private ReservationTimeResponse saveTime(LocalTime startAt) {
        return reservationTimeService.saveTime(new ReservationTimeRequest(startAt));
    }

    @Test
    @DisplayName("예약 시간을 저장한다")
    void saveTime() {
        // given
        ReservationTimeRequest request = createReservationTimeRequest();

        // when
        ReservationTimeResponse savedTime = reservationTimeService.saveTime(request);

        // then
        assertThat(savedTime.id()).isNotNull();
    }

    @Test
    @DisplayName("예약 시간 저장 요청이 null이면 예외가 발생한다")
    void saveTimeWithNullRequest() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.saveTime(null))
                .isInstanceOf(ReservationTimeException.class);
    }

    @Test
    @DisplayName("예약 시간이 null이면 예외가 발생한다")
    void saveTimeWithNullStartAt() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.saveTime(new ReservationTimeRequest(null)))
                .isInstanceOf(ReservationTimeException.class);
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다")
    void getTimes() {
        // given
        ReservationTimeResponse firstTime = saveTime(LocalTime.of(10, 0));
        ReservationTimeResponse secondTime = saveTime(LocalTime.of(11, 0));

        // when
        List<ReservationTimeResponse> times = reservationTimeService.getTimes();

        // then
        assertThat(times).hasSize(2);
        assertThat(times).containsExactlyInAnyOrder(firstTime, secondTime);
    }

    @Test
    @DisplayName("예약 시간이 없으면 빈 목록을 조회한다")
    void getTimesWhenEmpty() {
        // when
        List<ReservationTimeResponse> times = reservationTimeService.getTimes();

        // then
        assertThat(times).isEmpty();
    }

    @Test
    @DisplayName("예약 시간을 삭제한다")
    void deleteTime() {
        // given
        ReservationTimeResponse savedTime = saveTime(LocalTime.of(10, 0));

        // when
        reservationTimeService.deleteTime(savedTime.id());

        // then
        assertThat(reservationTimeService.getTimes()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제해도 예외가 발생하지 않는다")
    void deleteNotFoundTime() {
        // when
        reservationTimeService.deleteTime(999L);

        // then
        assertThat(reservationTimeService.getTimes()).isEmpty();
    }

    @Test
    @DisplayName("예약 시간 id가 null이면 삭제할 때 예외가 발생한다")
    void deleteTimeWithNullId() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteTime(null))
                .isInstanceOf(ReservationTimeException.class);
    }

    @Test
    @DisplayName("예약 시간 id가 참조되고 있으면 삭제할 때 예외가 발생한다")
    void deleteTimeWithReferencedReservationTime() {
        // given
        ReservationTimeResponse savedTime = saveTime(LocalTime.of(10, 0));
        ReservationRequest request = createReservationRequest(savedTime);
        reservationService.saveReservation(request);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteTime(savedTime.id()))
                .isInstanceOf(ReservationTimeException.class);
    }

    @Test
    @DisplayName("예약 시간 id가 예약에서 참조되고 있는지 확인 기능")
    void existsByReservationTimeId() {
        // given
        ReservationTimeResponse savedTime = saveTime(LocalTime.of(10, 0));
        ReservationRequest request = createReservationRequest(savedTime);
        reservationService.saveReservation(request);

        // when
        boolean exists = reservationRepository.existsByReservationTimeId(savedTime.id());

        // then
        assertThat(exists).isTrue();
    }
}
