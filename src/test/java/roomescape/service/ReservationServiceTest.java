package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationStatus;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.fixture.ReservationFixture;
import roomescape.domain.fixture.ReservationTimeFixture;
import roomescape.domain.fixture.ThemeFixture;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.global.exception.ForbiddenException;
import roomescape.global.exception.InactiveException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.fake.FakeReservationRepository;
import roomescape.repository.fake.FakeReservationTimeRepository;
import roomescape.repository.fake.FakeThemeRepository;
import roomescape.web.dto.reservation.ReservationCancelRequest;
import roomescape.web.dto.reservation.ReservationRequest;
import roomescape.web.dto.reservation.ReservationResponse;
import roomescape.web.dto.reservationTime.ReservationTimeResponse;
import roomescape.web.dto.theme.ThemeResponse;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        this.reservationRepository = new FakeReservationRepository();
        this.reservationTimeRepository = new FakeReservationTimeRepository();
        this.themeRepository = new FakeThemeRepository();
        this.reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
                themeRepository);
    }

    @Test
    void 새로운_예약을_정상적으로_등록한다() {
        // given
        ReservationTime time = reservationTimeRepository.save(
                ReservationTimeFixture.createReservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        LocalDate reservationDate = LocalDate.now().plusDays(1);
        ReservationRequest request = new ReservationRequest("이프", reservationDate, theme.getId(), time.getId());

        // when
        ReservationResponse response = reservationService.reserve(request);

        // then
        ReservationTimeResponse timeResponse = ReservationTimeResponse.from(time);
        ThemeResponse themeResponse = ThemeResponse.from(theme);
        assertThat(response).extracting(ReservationResponse::id, ReservationResponse::name, ReservationResponse::date,
                        ReservationResponse::time, ReservationResponse::theme, ReservationResponse::status)
                .containsExactly(1L, "이프", reservationDate, timeResponse, themeResponse, ReservationStatus.RESERVED);
    }

    @Test
    void 존재하지_않는_테마_정보로_예약하면_예외가_발생한다() {
        // given
        ReservationRequest request = new ReservationRequest("이프", LocalDate.now().plusDays(1), 1L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(request)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("존재하지 않는 테마 정보입니다.");
    }

    @Test
    void 존재하지_않는_시간_정보로_예약하면_예외가_발생한다() {
        // given
        ReservationRequest request = new ReservationRequest("이프", LocalDate.now().plusDays(1), 1L, 1L);
        themeRepository.save(ThemeFixture.createDefaultTheme());

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(request)).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("존재하지 않는 시간 정보입니다.");
    }

    @Test
    void 같은_날짜와_같은_시간에_특정_테마로_예약을_시도하면_중복_예외가_발생한다() {
        // given
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        ReservationTime time = reservationTimeRepository.save(ReservationTimeFixture.createDefaultReservationTime());

        Reservation existingReservation = ReservationFixture.createDefaultReservationWithName("기존 예약자", theme, time);
        reservationRepository.save(existingReservation);

        LocalDate date = existingReservation.getDate();

        ReservationRequest request = new ReservationRequest("새예약자", date, theme.getId(), time.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(request)).isInstanceOf(DuplicateEntityException.class)
                .hasMessageContaining("이미 예약 된 날짜입니다.");
    }

    @Test
    void 비활성화된_테마로_예약하면_예외가_발생한다() {
        // given
        Theme inactiveTheme = ThemeFixture.createDefaultTheme();
        inactiveTheme.deactivate();
        Theme savedTheme = themeRepository.save(inactiveTheme);
        ReservationTime time = reservationTimeRepository.save(ReservationTimeFixture.createDefaultReservationTime());
        ReservationRequest request = new ReservationRequest("웨지", LocalDate.now().plusDays(1), savedTheme.getId(),
                time.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(request)).isInstanceOf(InactiveException.class)
                .hasMessage("비활성화 된 테마는 예약할 수 없습니다.");
    }

    @Test
    void 페이징_정보에_따른_모든_예약_목록을_조회한다() {
        // given
        reservationRepository.save(ReservationFixture.createDefaultReservationWithName("이프"));
        reservationRepository.save(ReservationFixture.createDefaultReservationWithName("바니"));

        // when
        List<ReservationResponse> responses = reservationService.getAllReservationsByPaging(0, 10);

        // then
        assertThat(responses).hasSize(2);
    }

    @Test
    void 사용자_이름으로_예약_내역을_조회한다() {
        // given
        Reservation reservation = reservationRepository.save(ReservationFixture.createDefaultReservationWithName("바니"));
        reservationRepository.save(ReservationFixture.createDefaultReservationWithName("웨지"));

        // when
        List<ReservationResponse> response = reservationService.getReservationsByUser("바니");

        // then
        assertThat(response).containsOnly(ReservationResponse.from(reservation));
    }

    @Test
    void 사용자_이름으로_예약을_취소한다() {
        // given
        Reservation reservation = reservationRepository.save(ReservationFixture.createDefaultReservationWithName("바니"));

        // when & then
        assertThatCode(() -> reservationService.cancel(reservation.getId(),
                new ReservationCancelRequest("바니"))).doesNotThrowAnyException();
    }

    @Test
    void 사용자_이름이_일치하지_않는_예약을_취소하면_예외가_발생한다() {
        // given
        Reservation reservation = reservationRepository.save(ReservationFixture.createDefaultReservationWithName("바니"));

        // when & then
        assertThatThrownBy(
                () -> reservationService.cancel(reservation.getId(), new ReservationCancelRequest("웨지"))).isInstanceOf(
                ForbiddenException.class);
    }

    @Test
    void 취소한_예약_건에_대해서_예약이_가능하다() {
        // given
        ReservationTime time = reservationTimeRepository.save(
                ReservationTimeFixture.createReservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        LocalDate date = LocalDate.now().plusDays(1);

        Reservation canceled = new Reservation(1L, "바니", date, theme, time, ReservationStatus.CANCELED);
        reservationRepository.save(canceled);

        // when
        ReservationRequest request = new ReservationRequest("웨지", date, theme.getId(), time.getId());

        // then
        assertThatCode(() -> reservationService.reserve(request)).doesNotThrowAnyException();

    }
}
