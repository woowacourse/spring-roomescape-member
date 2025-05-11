package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.fixture.MemberFixture.MEMBER;
import static roomescape.fixture.ThemeFixture.THEME;
import static roomescape.fixture.TimeFixture.TIME;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.reservation.Reservation;
import roomescape.fake.FakeMemberDao;
import roomescape.fake.FakeReservationDao;
import roomescape.fake.FakeReservationTimeDao;
import roomescape.fake.FakeThemeDao;
import roomescape.presentation.dto.ReservationRequest;
import roomescape.presentation.dto.ReservationResponse;
import roomescape.presentation.exception.BadRequestException;

class ReservationServiceTest {

    private final FakeReservationTimeDao reservationTimeDao = new FakeReservationTimeDao();
    private final FakeReservationDao reservationDao = new FakeReservationDao();
    private final FakeThemeDao themeDao = new FakeThemeDao();
    private final FakeMemberDao fakeMemberDao = new FakeMemberDao();
    private final ReservationService reservationService = new ReservationService(reservationDao, reservationTimeDao,
            themeDao, fakeMemberDao);

    private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);

    @BeforeEach
    void setUp() {
        reservationTimeDao.save(TIME);
        themeDao.save(THEME);
        ReservationRequest request1 = new ReservationRequest(TOMORROW, TIME.getId(), THEME.getId());
        ReservationRequest request2 = new ReservationRequest(TOMORROW.plusDays(1), TIME.getId(), THEME.getId());
        reservationService.createReservation(request1, MEMBER);
        reservationService.createReservation(request2, MEMBER);
    }

    @DisplayName("예약 생성 테스트")
    @Nested
    class CreateReservationTest {

        private static final ReservationRequest REQUEST = new ReservationRequest(
                TOMORROW.plusDays(2),
                TIME.getId(),
                THEME.getId());

        @DisplayName("예약을 생성할 수 있다.")
        @Test
        void testCreate() {
            // when
            ReservationResponse result = reservationService.createReservation(REQUEST, MEMBER);
            // then
            Reservation savedReservation = reservationDao.findById(3L);
            assertAll(
                    () -> assertThat(result.id()).isEqualTo(3L),
                    () -> assertThat(result.member().name()).isEqualTo(MEMBER.getName()),
                    () -> assertThat(result.date()).isEqualTo(REQUEST.date()),
                    () -> assertThat(result.time().startAt()).isEqualTo(TIME.getStartAt()),
                    () -> assertThat(result.time().id()).isEqualTo(REQUEST.timeId()),

                    () -> assertThat(result.theme().id()).isEqualTo(THEME.getId()),
                    () -> assertThat(result.theme().name()).isEqualTo(THEME.getName()),
                    () -> assertThat(result.theme().description()).isEqualTo(THEME.getDescription()),
                    () -> assertThat(result.theme().thumbnail()).isEqualTo(THEME.getThumbnail()),

                    () -> assertThat(savedReservation.getDateTime().getDate()).isEqualTo(REQUEST.date()),
                    () -> assertThat(savedReservation.getTime().getStartAt()).isEqualTo(TIME.getStartAt()),
                    () -> assertThat(savedReservation.getTime().getId()).isEqualTo(REQUEST.timeId()),

                    () -> assertThat(savedReservation.getTheme().getId()).isEqualTo(THEME.getId()),
                    () -> assertThat(savedReservation.getTheme().getName()).isEqualTo(THEME.getName()),
                    () -> assertThat(savedReservation.getTheme().getDescription()).isEqualTo(THEME.getDescription()),
                    () -> assertThat(savedReservation.getTheme().getThumbnail()).isEqualTo(THEME.getThumbnail())
            );
        }

        @DisplayName("중복 예약일 경우 예외가 발생한다.")
        @Test
        void testValidateDuplication() {
            // given
            reservationService.createReservation(REQUEST, MEMBER);
            // when
            // then
            assertThatThrownBy(() -> reservationService.createReservation(REQUEST, MEMBER))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("해당 시간에 이미 예약이 존재합니다.");
        }

        @DisplayName("예약 시간이 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void testValidateTime() {
            // given
            ReservationRequest request = new ReservationRequest(TOMORROW, 2L, THEME.getId());
            // when
            // then
            assertThatThrownBy(() -> reservationService.createReservation(request, MEMBER))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("예약 시간이 존재하지 않습니다.");
        }

        @DisplayName("테마가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void testValidateTheme() {
            // given
            ReservationRequest request = new ReservationRequest(TOMORROW, TIME.getId(), 2L);
            // when
            // then
            assertThatThrownBy(() -> reservationService.createReservation(request, MEMBER))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("테마가 존재하지 않습니다.");
        }

        @DisplayName("과거 시간에 예약할 경우 예외가 발생한다.")
        @Test
        void testValidatePastTime() {
            // given
            LocalDate yesterday = LocalDate.now().minusDays(1);
            ReservationRequest request = new ReservationRequest(yesterday, TIME.getId(), THEME.getId());
            // when
            // then
            assertThatThrownBy(() -> reservationService.createReservation(request, MEMBER))
                    .isInstanceOf(BadRequestException.class)
                    .hasMessage("지나간 날짜와 시간은 예약 불가합니다.");
        }
    }

    @DisplayName("예약 목록을 조회할 수 있다.")
    @Test
    void testFindAll() {
        // given
        // when
        List<ReservationResponse> reservations = reservationService.getReservations();
        // then
        assertThat(reservations).hasSize(2);
    }

    @DisplayName("예약을 삭제할 수 있다.")
    @Test
    void testCancelById() {
        // given
        // when
        reservationService.cancelReservationById(1L);
        // then
        assertThat(reservationService.getReservations()).hasSize(1);
    }
}
