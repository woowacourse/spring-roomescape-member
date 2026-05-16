package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import roomescape.web.dto.reservation.ReservationModifyRequest;
import roomescape.web.dto.reservation.ReservationRequest;
import roomescape.web.dto.reservation.ReservationResponse;
import roomescape.web.dto.reservationTime.ReservationTimeResponse;
import roomescape.web.dto.theme.ReservationTimeStatusResponse;
import roomescape.web.dto.theme.ThemeResponse;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private ReservationService reservationService;

    static Stream<Arguments> provideReservationStatusScenarios() {
        LocalDate today = LocalDate.now();
        return Stream.of(Arguments.of(today.minusDays(7), false, false), Arguments.of(today, false, true),
                Arguments.of(today.plusDays(7), true, true));
    }

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
        ReservationTime time = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(10, 0)));
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
    void 비활성화된_시간대로_예약하면_예외가_발생한다() {
        // given
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        ReservationTime inactiveTime = ReservationTime.create(LocalTime.of(10, 0)).deactivate();
        ReservationTime savedTime = reservationTimeRepository.save(inactiveTime);
        ReservationRequest request = new ReservationRequest("웨지", LocalDate.now().plusDays(1), theme.getId(),
                savedTime.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.reserve(request)).isInstanceOf(InactiveException.class)
                .hasMessage("비활성화 된 시간대는 예약할 수 없습니다.");
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
        ReservationTime time = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        LocalDate date = LocalDate.now().plusDays(1);

        Reservation canceled = Reservation.restore(1L, "바니", date, theme, time, ReservationStatus.CANCELED);
        reservationRepository.save(canceled);

        // when
        ReservationRequest request = new ReservationRequest("웨지", date, theme.getId(), time.getId());

        // then
        assertThatCode(() -> reservationService.reserve(request)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @MethodSource("provideReservationStatusScenarios")
    void 테마별_예약_가능한_시간_조회_시_시점에_따라_상태를_처리한다(LocalDate date, boolean expectedForEarlyTime, boolean expectedForLateTime) {
        // given
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        for (int hour = 0; hour <= 23; hour++) {
            reservationTimeRepository.save(
                    ReservationTime.create(LocalTime.of(hour, 0)));
        }
        LocalTime nowTime = LocalTime.now();

        // when
        List<ReservationTimeStatusResponse> response = reservationService.getReservationStatusByTheme(theme.getId(),
                date);

        // then
        assertThat(response).filteredOn(time -> time.startAt().isBefore(nowTime))
                .allSatisfy(time -> assertThat(time.isReservable()).isEqualTo(expectedForEarlyTime));

        assertThat(response).filteredOn(time -> time.startAt().isAfter(nowTime))
                .allSatisfy(time -> assertThat(time.isReservable()).isEqualTo(expectedForLateTime));
    }

    @Test
    void 테마별_예약_가능한_시간_조회_시_이미_예약된_시간은_예약_불가능하다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        ReservationTime reservedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(10, 0)));
        reservationTimeRepository.save(ReservationTime.create(LocalTime.of(11, 0)));
        reservationRepository.save(Reservation.create("이프", date, theme, reservedTime));

        // when
        List<ReservationTimeStatusResponse> response = reservationService.getReservationStatusByTheme(theme.getId(),
                date);

        // then
        assertThat(response).filteredOn(time -> time.id().equals(reservedTime.getId())).singleElement()
                .extracting(ReservationTimeStatusResponse::isReservable).isEqualTo(false);
    }

    @Test
    void 테마별_예약_가능한_시간_조회_시_비활성화된_시간은_예약_불가능하다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        ReservationTime activeTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(10, 0)));
        ReservationTime inactiveTime = ReservationTime.create(LocalTime.of(11, 0)).deactivate();
        ReservationTime savedInactiveTime = reservationTimeRepository.save(inactiveTime);

        // when
        List<ReservationTimeStatusResponse> response = reservationService.getReservationStatusByTheme(theme.getId(),
                date);

        // then
        assertThat(response).filteredOn(time -> time.id().equals(activeTime.getId())).singleElement()
                .extracting(ReservationTimeStatusResponse::isReservable).isEqualTo(true);
        assertThat(response).filteredOn(time -> time.id().equals(savedInactiveTime.getId())).singleElement()
                .extracting(ReservationTimeStatusResponse::isReservable).isEqualTo(false);
    }

    @Test
    void 존재하지_않는_테마_정보로_예약_가능한_시간_조회_시_예외가_발생한다() {
        // given
        Long id = 999L;
        LocalDate date = LocalDate.now();

        // when & then
        assertThatThrownBy(() -> reservationService.getReservationStatusByTheme(id, date)).isInstanceOf(
                EntityNotFoundException.class).hasMessage("존재하지 않는 테마 정보입니다.");
    }

    @Test
    void 예약을_수정한다() {
        // given
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        ReservationTime originalTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(10, 0)));
        ReservationTime modifiedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(11, 0)));

        Reservation reservation = reservationRepository.save(
                ReservationFixture.createDefaultReservationWithName("바니", theme, originalTime));
        LocalDate modifiedDate = LocalDate.now().plusDays(2);
        ReservationModifyRequest request = new ReservationModifyRequest("바니", modifiedDate, modifiedTime.getId());

        // when
        reservationService.modify(reservation.getId(), request);

        // then
        Reservation modifiedReservation = reservationRepository.findById(reservation.getId()).get();
        assertThat(modifiedReservation.getDate()).isEqualTo(modifiedDate);
        assertThat(modifiedReservation.getTime()).isEqualTo(modifiedTime);
        assertThat(modifiedReservation.getTheme()).isEqualTo(theme);
        assertThat(modifiedReservation.getStatus()).isEqualTo(ReservationStatus.RESERVED);
    }

    @Test
    void 예약자_이름이_일치하지_않으면_예약_수정_시_예외가_발생한다() {
        // given
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        ReservationTime originalTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(10, 0)));
        ReservationTime modifiedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(11, 0)));
        Reservation reservation = reservationRepository.save(
                ReservationFixture.createDefaultReservationWithName("바니", theme, originalTime));
        ReservationModifyRequest request = new ReservationModifyRequest("웨지", LocalDate.now().plusDays(2),
                modifiedTime.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.modify(reservation.getId(), request)).isInstanceOf(
                ForbiddenException.class).hasMessage("예약자 명이 일치하지 않습니다.");
    }

    @Test
    void 비활성화된_테마의_예약을_수정하면_예외가_발생한다() {
        // given
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        ReservationTime originalTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(10, 0)));
        ReservationTime modifiedTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(11, 0)));
        Reservation reservation = reservationRepository.save(
                ReservationFixture.createDefaultReservationWithName("바니", theme, originalTime));

        theme.deactivate();
        themeRepository.update(theme);
        ReservationModifyRequest request = new ReservationModifyRequest("바니", LocalDate.now().plusDays(2),
                modifiedTime.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.modify(reservation.getId(), request)).isInstanceOf(
                InactiveException.class).hasMessage("비활성화 된 테마는 예약할 수 없습니다.");
    }

    @Test
    void 비활성화된_시간대로_예약을_수정하면_예외가_발생한다() {
        // given
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        ReservationTime originalTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(10, 0)));
        ReservationTime inactiveTime = ReservationTime.create(LocalTime.of(11, 0)).deactivate();
        ReservationTime savedInactiveTime = reservationTimeRepository.save(inactiveTime);
        Reservation reservation = reservationRepository.save(
                ReservationFixture.createDefaultReservationWithName("바니", theme, originalTime));
        ReservationModifyRequest request = new ReservationModifyRequest("바니", LocalDate.now().plusDays(2),
                savedInactiveTime.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.modify(reservation.getId(), request)).isInstanceOf(
                InactiveException.class).hasMessage("비활성화 된 시간대는 예약할 수 없습니다.");
    }

    @Test
    void 수정하려는_예약이_기존_예약과_중복되면_예외가_발생한다() {
        // given
        Theme theme = themeRepository.save(ThemeFixture.createDefaultTheme());
        ReservationTime originalTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(10, 0)));
        ReservationTime duplicateTime = reservationTimeRepository.save(ReservationTime.create(LocalTime.of(11, 0)));
        LocalDate duplicateDate = LocalDate.now().plusDays(2);

        Reservation reservation = reservationRepository.save(
                ReservationFixture.createDefaultReservationWithName("바니", theme, originalTime));
        reservationRepository.save(Reservation.create("웨지", duplicateDate, theme, duplicateTime));
        ReservationModifyRequest request = new ReservationModifyRequest("바니", duplicateDate, duplicateTime.getId());

        // when & then
        assertThatThrownBy(() -> reservationService.modify(reservation.getId(), request)).isInstanceOf(
                DuplicateEntityException.class).hasMessageContaining("이미 예약 된 날짜입니다.");
    }
}
