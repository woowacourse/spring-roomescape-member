package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.global.exception.error.ConflictException;
import roomescape.global.exception.error.InvalidRequestException;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;
import roomescape.reservation.controller.dto.AvailableTimeResponse;
import roomescape.reservation.controller.dto.ReservationCreate;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.repository.ThemeRepository;
import roomescape.reservation.repository.fake.MemberFakeRepository;
import roomescape.reservation.repository.fake.ReservationFakeRepository;
import roomescape.reservation.repository.fake.ReservationTimeFakeRepository;
import roomescape.reservation.repository.fake.ThemeFakeRepository;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setup() {
        reservationRepository = new ReservationFakeRepository();
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
        ThemeRepository themeRepository = new ThemeFakeRepository(reservationRepository);
        MemberFakeRepository memberRepository = new MemberFakeRepository();

        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(0, 0)));
        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(11, 33)));
        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(16, 54)));
        reservationTimeRepository.saveAndReturnId(new ReservationTime(null, LocalTime.of(23, 53)));

        themeRepository.saveAndReturnId(new Theme(null, "레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        themeRepository.saveAndReturnId(new Theme(null, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        themeRepository.saveAndReturnId(new Theme(null, "레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        memberRepository.save(new Member(null, "루키", "rookie123@woowa.com", "rookierookie123", Role.USER));
        memberRepository.save(new Member(null, "하루", "haru123@woowa.com", "haruharu123", Role.USER));
        memberRepository.save(new Member(null, "베루스", "verus@woowa.com", "verusverus123", Role.ADMIN));

        reservationRepository.saveAndReturnId(
                new Reservation(null, LocalDate.now().minusDays(3), reservationTimeRepository.findById(1L).get(),
                        themeRepository.findById(1L).get(), memberRepository.findById(1L).get()));
        reservationRepository.saveAndReturnId(
                new Reservation(null, LocalDate.now().minusDays(1), reservationTimeRepository.findById(2L).get(),
                        themeRepository.findById(2L).get(), memberRepository.findById(2L).get()));
        reservationRepository.saveAndReturnId(
                new Reservation(null, LocalDate.now().plusDays(3), reservationTimeRepository.findById(3L).get(),
                        themeRepository.findById(3L).get(), memberRepository.findById(3L).get()));

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository,
                memberRepository);
    }

    @DisplayName("전체 예약 정보를 조회한다")
    @Test
    void get_all_test() {
        // when
        List<ReservationResponse> reservationResponses = reservationService.getAll();

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        List<LocalDate> expectedDates = reservations.stream()
                .map(Reservation::getDate)
                .toList();

        assertAll(
                () -> assertThat(reservationResponses).hasSize(reservations.size()),
                () -> assertThat(reservationResponses)
                        .extracting(ReservationResponse::date)
                        .containsExactlyInAnyOrderElementsOf(expectedDates)
        );
    }

    @DisplayName("예약 정보를 추가한다")
    @Test
    void add_test() {
        // given
        ReservationCreate request = new ReservationCreate(LocalDate.now().plusDays(2), 4L, 3L, 1L);

        // when
        ReservationResponse response = reservationService.add(request);

        // then
        Reservation savedReservation = reservationRepository.findById(4L).get();
        assertAll(
                () -> assertThat(response.id()).isEqualTo(savedReservation.getId()),
                () -> assertThat(response.theme().id()).isEqualTo(savedReservation.getThemeId()),
                () -> assertThat(response.time().id()).isEqualTo(savedReservation.getTimeId())
        );
    }

    @DisplayName("예약 정보를 삭제한다")
    @Test
    void remove_test() {
        // given
        Long removeId = 3L;

        // when
        reservationService.remove(removeId);

        // then
        assertThat(reservationRepository.findById(removeId).isEmpty()).isTrue();
    }

    @DisplayName("지난 날짜를 예약하는 경우 예외가 발생한다")
    @Test
    void past_day_exception_test() {
        // given
        ReservationCreate request = new ReservationCreate(LocalDate.now().minusDays(1), 4L, 3L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("지난 날짜는 예약할 수 없습니다.");
    }

    @DisplayName("같은 날짜에 지난 시각인 경우 예외가 발생한다")
    @Test
    void past_time_exception_test() {
        // given
        ReservationCreate request = new ReservationCreate(LocalDate.now(), 1L, 3L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("지난 시각은 예약할 수 없습니다.");
    }

    @DisplayName("미래 날짜인데 시간이 현재보다 과거인 경우 예약을 저장한다")
    @Test
    void future_test() {
        // given
        ReservationCreate request = new ReservationCreate(LocalDate.now().plusDays(3), 1L, 1L, 1L);

        // when
        ReservationResponse response = reservationService.add(request);

        // then
        Reservation savedReservation = reservationRepository.findById(4L).get();
        assertAll(
                () -> assertThat(response.id()).isEqualTo(savedReservation.getId()),
                () -> assertThat(response.date()).isEqualTo(savedReservation.getDate()),
                () -> assertThat(response.theme().id()).isEqualTo(savedReservation.getThemeId()),
                () -> assertThat(response.time().id()).isEqualTo(savedReservation.getTimeId())
        );
    }

    @DisplayName("동일한 날짜, 시간, 테마에 대한 중복 예약을 하면 예외가 발생한다")
    @Test
    void reservation_duplicate_exception() {
        // given
        ReservationCreate request = new ReservationCreate(LocalDate.now().plusDays(3), 3L, 3L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(ConflictException.class)
                .hasMessage("해당 날짜, 시간, 테마에 대한 동일한 예약이 존재합니다.");
    }

    @DisplayName("날짜, 테마 ID에 해당하는 예약 가능 시간 정보를 반환한다")
    @Test
    void get_available_times_test() {
        // given
        LocalDate date = LocalDate.now().minusDays(3);
        Long themeId = 1L;

        // when
        List<AvailableTimeResponse> availableTimes = reservationService.getAvailableTimes(date, themeId);

        // then
        assertAll(
                () -> assertThat(availableTimes.get(0).alreadyBooked()).isTrue(),
                () -> assertThat(availableTimes.get(1).alreadyBooked()).isFalse()
        );
    }

    @DisplayName("필터 조건에 해당하는 예약 목록을 반환한다")
    @MethodSource
    @ParameterizedTest
    void get_all_by_filter_test(
            Long memberId,
            Long themeId,
            LocalDate dateFrom,
            LocalDate dateTo,
            List<Long> expectedReservationIds
    ) {
        // when
        List<ReservationResponse> reservations = reservationService.getAllByFilter(memberId, themeId, dateFrom, dateTo);

        // then
        assertThat(reservations).extracting(ReservationResponse::id)
                .containsExactlyInAnyOrderElementsOf(expectedReservationIds);
    }

    static Stream<Arguments> get_all_by_filter_test() {
        return Stream.of(
                Arguments.of(1L, null, null, null, List.of(1L)),
                Arguments.of(null, 2L, null, null, List.of(2L)),
                Arguments.of(null, null, LocalDate.now().minusDays(2), null, List.of(2L, 3L)),
                Arguments.of(null, null, null, LocalDate.now().minusDays(2), List.of(1L)),
                Arguments.of(null, null, LocalDate.now().minusDays(2), LocalDate.now().plusDays(2), List.of(2L)),
                Arguments.of(3L, 3L, null, null, List.of(3L)),
                Arguments.of(1L, 1L, LocalDate.now().minusDays(5), LocalDate.now().minusDays(2), List.of(1L)),
                Arguments.of(null, null, null, null, List.of(1L, 2L, 3L))
        );
    }

}
