package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.auth.config.AuthInfo;
import roomescape.auth.service.FakeMemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.dto.response.FindAvailableTimesResponse;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.FakeReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.util.MemberFixture;
import roomescape.util.ReservationFixture;
import roomescape.util.ReservationTimeFixture;
import roomescape.util.ThemeFixture;

class ReservationServiceTest {

    private ReservationService reservationService;

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository(reservationRepository);
        memberRepository = new FakeMemberRepository();

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
                themeRepository, memberRepository);
    }

    @Nested
    class createReservation {

        @Test
        @DisplayName("예약 생성 시 해당 데이터의 id값을 반환한다.")
        void createReservation() {
            // given
            memberRepository.save(MemberFixture.getOne());
            reservationTimeRepository.save(ReservationTimeFixture.getOne());
            themeRepository.save(ThemeFixture.getOne());

            AuthInfo authInfo = new AuthInfo(1L, "sdf", MemberRole.USER);
            CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                    LocalDate.of(2024, 10, 10), 1L, 1L);

            // when
            CreateReservationResponse createReservationResponse = reservationService.createReservation(
                    authInfo, createReservationRequest);

            // then
            assertThat(createReservationResponse.id()).isEqualTo(1L);
        }

        @Test
        @DisplayName("예약 생성 시 해당하는 예약 시간 id와 동일하게 저장된 예약 시간이 없는 경우 예외가 발생한다.")
        void createReservation_ifReservationTimeNotExist_throwException() {
            // given
            themeRepository.save(ThemeFixture.getOne());

            AuthInfo authInfo = new AuthInfo(1L, "포비", MemberRole.USER);
            CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                    LocalDate.of(10, 10, 10), 1L, 1L);

            // when & then
            assertThatThrownBy(() -> reservationService.createReservation(authInfo, createReservationRequest))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("생성하려는 예약의 예약 시간이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("예약 생성 시 해당하는 테마 id와 동일하게 저장된 테마가 없는 경우 예외가 발생한다.")
        void createReservation_ifThemeNotExist_throwException() {
            // given
            reservationTimeRepository.save(ReservationTimeFixture.getOne());

            AuthInfo authInfo = new AuthInfo(1L, "포비", MemberRole.USER);
            CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                    LocalDate.of(2024, 10, 10), 1L, 1L);

            // when & then
            assertThatThrownBy(() -> reservationService.createReservation(authInfo, createReservationRequest))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("생성하려는 테마가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("예약 생성 시 해당하는 날짜와 시간이 동일하게 저장된 테마가 있는 경우 예외가 발생한다.")
        void createReservation_ifExistSameDateAndTime_throwException() {
            // given
            ReservationTime reservationTime = reservationTimeRepository.save(ReservationTimeFixture.getOne());
            Theme theme = themeRepository.save(ThemeFixture.getOne());
            Member member = memberRepository.save(MemberFixture.getOne());
            reservationRepository.save(
                    new Reservation(null, member, LocalDate.parse("2024-10-10"), reservationTime, theme));

            AuthInfo authInfo = new AuthInfo(1L, "포비", MemberRole.USER);
            CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                    LocalDate.parse("2024-10-10"), 1L, 1L);

            // when & then
            assertThatThrownBy(() -> reservationService.createReservation(authInfo, createReservationRequest))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("동일한 시간의 예약이 존재합니다.");
        }

        @Test
        @DisplayName("예약 생성 시 지나간 날짜에 대한 예약인 경우 예외가 발생한다.")
        void createReservation_checkDateToCreateIsPast_throwException() {
            // given
            reservationTimeRepository.save(ReservationTimeFixture.getOne());
            themeRepository.save(ThemeFixture.getOne());

            AuthInfo authInfo = new AuthInfo(1L, "포비", MemberRole.USER);
            CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                    LocalDate.of(1000, 10, 10), 1L, 1L);

            // when & then
            assertThatThrownBy(() -> reservationService.createReservation(authInfo, createReservationRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("지나간 날짜에 대한 예약 생성은 불가능합니다.");
        }

        @Test
        @DisplayName("예약 생성 시 현재 시간에 대한 예약인 경우 예외가 발생한다.")
        void createReservation_checkDateTimeToCreateIsPast_throwException() {
            // given
            LocalDateTime now = LocalDateTime.now();
            ReservationTime reservationTime = reservationTimeRepository.save(
                    new ReservationTime(null, now.toLocalTime()));
            themeRepository.save(ThemeFixture.getOne());

            AuthInfo authInfo = new AuthInfo(1L, "포비", MemberRole.USER);
            CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                    now.toLocalDate(), reservationTime.getId(), 1L);

            // when & then
            assertThatThrownBy(() -> reservationService.createReservation(authInfo, createReservationRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("지나간 시간 또는 현재 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    @Test
    @DisplayName("예약 목록 조회 시 저장된 예약과 예약 시간에 대한 정보를 반환한다.")
    void getReservations() {
        // give
        List<Reservation> reservations = ReservationFixture.get(2);
        reservationRepository.save(reservations.get(0));
        reservationRepository.save(reservations.get(1));

        // when & then
        assertThat(reservationService.getReservations())
                .containsExactly(
                        FindReservationResponse.of(1L, reservations.get(0)),
                        FindReservationResponse.of(2L, reservations.get(1)));
    }

    @Nested
    class getReservation {

        @Test
        @DisplayName("해당하는 id와 동일한 저장된 예약 대한 정보를 반환한다.")
        void getReservation() {
            // given
            Reservation reservation = ReservationFixture.getOne();
            reservationRepository.save(reservation);
            long id = 1L;

            // when & then
            assertThat(reservationService.getReservation(id))
                    .isEqualTo(FindReservationResponse.of(id, reservation));
        }

        @Test
        @DisplayName("해당하는 id와 동일한 저장된 예약이 없는 경우 예외가 발생한다.")
        void getReservation_ifNotExist_throwException() {
            // when & then
            assertThatThrownBy(() -> reservationService.getReservation(1L))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("조회하려는 예약이 존재하지 않습니다.");
        }
    }

    @Test
    @DisplayName("해당하는 날짜와 테마를 통해 가능한 예약 시간 정보를 반환한다.")
    void getAvailableTimes() {
        LocalDate date = LocalDate.of(2024, 10, 23);

        List<ReservationTime> reservationTimes = ReservationTimeFixture.get(2);
        ReservationTime reservationTime1 = reservationTimeRepository.save(reservationTimes.get(0));
        ReservationTime reservationTime2 = reservationTimeRepository.save(reservationTimes.get(1));

        Theme theme = themeRepository.save(ThemeFixture.getOne());

        reservationRepository.save(ReservationFixture.getOneWithDateTimeTheme(date, reservationTime1, theme));

        // when & then
        assertThat(reservationService.getAvailableTimes(date, 1L))
                .isEqualTo(List.of(
                        FindAvailableTimesResponse.from(reservationTime1, true),
                        FindAvailableTimesResponse.from(reservationTime2, false))
                );
    }

    @Nested
    class deleteReservation {

        @Test
        @DisplayName("해당하는 id와 동일한 저장된 예약을 삭제한다.")
        void deleteReservation() {
            // given
            reservationRepository.save(ReservationFixture.getOne());
            long reservationId = 1L;

            // when
            reservationService.deleteReservation(reservationId);

            // then
            assertThat(reservationRepository.findAll()).isEmpty();
        }

        @Test
        @DisplayName("해당하는 id와 동일한 저장된 예약이 없는 경우 예외가 발생한다.")
        void deleteReservation_ifNotExist_throwException() {
            // given
            long timeId = 1L;

            // when & then
            assertThatThrownBy(() -> reservationService.deleteReservation(timeId))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("삭제하려는 예약이 존재하지 않습니다.");
        }
    }
}
