package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.dto.CreateReservationRequest;
import roomescape.domain.reservation.dto.CreateReservationResponse;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.support.exception.BadRequestException;
import roomescape.support.exception.RoomescapeException;
import roomescape.support.fake.FakeReservationDateRepository;
import roomescape.support.fake.FakeReservationRepository;
import roomescape.support.fake.FakeReservationTimeRepository;
import roomescape.support.fake.FakeThemeRepository;

class ReservationServiceTest {

    private static final ZoneId ZONE_ID = ZoneId.systemDefault();
    private FakeReservationRepository reservationRepository;
    private FakeReservationTimeRepository reservationTimeRepository;
    private FakeReservationDateRepository reservationDateRepository;
    private FakeThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationDateRepository = new FakeReservationDateRepository();
        themeRepository = new FakeThemeRepository();
    }

    @Test
    void 존재하는_예약_시간으로_예약을_생성한다() {
        // given
        Clock now = fixedClockAt(LocalDateTime.of(2026, 5, 12, 13, 0));
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        ReservationDate reservationDate = ReservationDate.createWithoutId(LocalDate.of(2026, 5, 13));
        ReservationDate savedReservationDate = reservationDateRepository.save(reservationDate);
        Theme theme = themeRepository.save(Theme.createWithoutId("공포", "무서운 테마", "theme-url"));
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository,
            now
        );
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            savedReservationDate.getId(),
            savedReservationTime.getId(),
            theme.getId()
        );

        // when
        CreateReservationResponse response = reservationService.createReservation(request);

        // then
        assertSoftly(softly -> {
            assertThat(response.name()).isEqualTo("보예");
            assertThat(response.date()).isEqualTo(LocalDate.of(2026, 5, 13));
            assertThat(response.time()).isEqualTo(LocalTime.of(10, 0));
            assertThat(response.theme().name()).isEqualTo("공포");
        });
    }

    @Test
    void 존재하지_않는_예약_시간으로_예약을_생성하면_예외가_발생한다() {
        // given
        Clock now = fixedClockAt(LocalDateTime.of(2026, 5, 12, 13, 0));
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository,
            now
        );
        CreateReservationRequest request = new CreateReservationRequest("보예", 1L, 1L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("존재하지 않는 예약 시간대 입니다.");
    }

    @Test
    void 존재하지_않는_테마로_예약을_생성하면_예외가_발생한다() {
        // given
        Clock now = fixedClockAt(LocalDateTime.of(2026, 5, 12, 13, 0));
        ReservationTime reservationTime = reservationTimeRepository.save(
            ReservationTime.createWithoutId(LocalTime.of(10, 0)));
        ReservationDate reservationDate = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 13)));
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository,
            now
        );
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            reservationDate.getId(),
            reservationTime.getId(),
            3L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("존재하지 않는 테마 입니다.");
    }

    @Test
    void 예약_목록을_조회한다() {
        // given
        Clock now = fixedClockAt(LocalDateTime.of(2026, 5, 12, 13, 0));
        ReservationDate savedReservationDate = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 13))
        );
        ReservationTime reservationTime = reservationTimeRepository.save(
            ReservationTime.createWithoutId(LocalTime.of(10, 0))
        );
        Theme theme = themeRepository.save(Theme.createWithoutId("공포", "무서운테마", "theme-url"));
        reservationRepository.save(
            Reservation.createWithoutId(
                "보예",
                savedReservationDate,
                reservationTime,
                theme
            )
        );
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository,
            now
        );

        // when
        List<ReservationResponse> responses = reservationService.getAllReservations();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(1);
            assertThat(responses.getFirst().name()).isEqualTo("보예");
            assertThat(responses.getFirst().date()).isEqualTo(LocalDate.of(2026, 5, 13));
            assertThat(responses.getFirst().time().id()).isEqualTo(reservationTime.getId());
            assertThat(responses.getFirst().time().startAt()).isEqualTo(LocalTime.of(10, 0));
            assertThat(responses.getFirst().theme().id()).isEqualTo(theme.getId());
            assertThat(responses.getFirst().theme().name()).isEqualTo("공포");
        });
    }

    @Test
    void 오늘보다_이전_날짜는_예약할_수_없다() {
        // given
        Clock now = fixedClockAt(LocalDateTime.of(2026, 5, 12, 13, 0));
        ReservationTime reservationTime = reservationTimeRepository.save(
            ReservationTime.createWithoutId(LocalTime.of(14, 0))
        );
        ReservationDate beforeToday = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 10))
        );
        Theme theme = themeRepository.save(Theme.createWithoutId("공포", "무서운 테마", "theme-url"));
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository,
            now
        );
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            beforeToday.getId(),
            reservationTime.getId(),
            theme.getId()
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("예약 날짜는 오늘 이후여야 합니다. 오늘 날짜:" + LocalDate.of(2026, 5, 12));
    }

    @Test
    void 오늘_예약일_경우_현재_시간_이전은_예약할_수_없다() {
        // given
        Clock now = fixedClockAt(LocalDateTime.of(2026, 5, 12, 13, 0));
        ReservationTime beforeNow = reservationTimeRepository.save(
            ReservationTime.createWithoutId(LocalTime.of(12, 59))
        );
        ReservationDate today = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 12))
        );
        Theme theme = themeRepository.save(Theme.createWithoutId("공포", "무서운 테마", "theme-url"));
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository,
            now
        );
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            today.getId(),
            beforeNow.getId(),
            theme.getId()
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("예약 시간은 현재 이후여야 합니다. 현재 시각:" + LocalTime.of(13, 0));
    }

    @Test
    void 오늘_예약이지만_현재_시간은_예약할_수_있다() {
        // given
        Clock now = fixedClockAt(LocalDateTime.of(2026, 5, 12, 13, 0));
        ReservationTime nowTime = reservationTimeRepository.save(
            ReservationTime.createWithoutId(LocalTime.of(13, 0))
        );
        ReservationDate today = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 12))
        );
        Theme theme = themeRepository.save(Theme.createWithoutId("공포", "무서운 테마", "theme-url"));
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository,
            now
        );
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            today.getId(),
            nowTime.getId(),
            theme.getId()
        );

        // when
        CreateReservationResponse response = reservationService.createReservation(request);
        Reservation reservation = reservationRepository.findById(response.id()).orElseThrow();

        // then
        assertSoftly(softly -> {
                assertThat(response.id()).isEqualTo(reservation.getId());
                assertThat(response.date()).isEqualTo(LocalDate.of(2026, 5, 12));
                assertThat(response.time()).isEqualTo(LocalTime.of(13, 0));
                assertThat(response.name()).isEqualTo("보예");
                assertThat(response.theme().name()).isEqualTo("공포");
                assertThat(response.theme().content()).isEqualTo("무서운 테마");
                assertThat(response.theme().url()).isEqualTo("theme-url");
            }
        );
    }

    @Test
    void 날짜가_오늘_이후이고_현재_시간보다_이전이면_정상_예약_된다() {
        // given
        Clock now = fixedClockAt(LocalDateTime.of(2026, 5, 12, 13, 0));
        ReservationTime reservationTime = reservationTimeRepository.save(
            ReservationTime.createWithoutId(LocalTime.of(10, 0))
        );
        ReservationDate today = reservationDateRepository.save(
            ReservationDate.createWithoutId(LocalDate.of(2026, 5, 13))
        );
        Theme theme = themeRepository.save(Theme.createWithoutId("공포", "무서운 테마", "theme-url"));
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository,
            now
        );
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            today.getId(),
            reservationTime.getId(),
            theme.getId()
        );

        // when
        CreateReservationResponse response = reservationService.createReservation(request);
        Reservation reservation = reservationRepository.findById(response.id()).orElseThrow();

        // then
        assertSoftly(softly -> {
                assertThat(response.id()).isEqualTo(reservation.getId());
                assertThat(response.date()).isEqualTo(LocalDate.of(2026, 5, 13));
                assertThat(response.time()).isEqualTo(LocalTime.of(10, 0));
                assertThat(response.name()).isEqualTo("보예");
                assertThat(response.theme().name()).isEqualTo("공포");
                assertThat(response.theme().content()).isEqualTo("무서운 테마");
                assertThat(response.theme().url()).isEqualTo("theme-url");
            }
        );
    }

    @Test
    void 중복된_예약은_예외가_발생한다() {
        // given
        Clock now = fixedClockAt(LocalDateTime.of(2026, 5, 12, 13, 0));
        ReservationTime reservationTime = ReservationTime.createWithoutId(LocalTime.of(10, 0));
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        ReservationDate reservationDate = ReservationDate.createWithoutId(LocalDate.of(2026, 5, 13));
        ReservationDate savedReservationDate = reservationDateRepository.save(reservationDate);
        Theme theme = themeRepository.save(Theme.createWithoutId("공포", "무서운 테마", "theme-url"));
        ReservationService reservationService = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            reservationDateRepository,
            themeRepository,
            now
        );
        CreateReservationRequest request = new CreateReservationRequest(
            "보예",
            savedReservationDate.getId(),
            savedReservationTime.getId(),
            theme.getId()
        );
        reservationService.createReservation(request);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(BadRequestException.class)
            .hasMessage("중복 예약입니다. 예약 정보를 다시 확인해주세요.");
    }

    private Clock fixedClockAt(LocalDateTime dateTime) {
        return Clock.fixed(dateTime.atZone(ZONE_ID).toInstant(), ZONE_ID);
    }
}
