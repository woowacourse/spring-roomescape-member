package roomescape.service.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.controller.login.LoginMember;
import roomescape.controller.login.MemberCheckResponse;
import roomescape.controller.reservation.CreateReservationRequest;
import roomescape.controller.reservation.ReservationResponse;
import roomescape.controller.theme.ReservationThemeResponse;
import roomescape.controller.time.TimeResponse;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.repository.*;
import roomescape.repository.exception.ThemeNotFoundException;
import roomescape.repository.exception.TimeNotFoundException;
import roomescape.service.reservation.exception.PreviousTimeException;
import roomescape.service.reservation.exception.ReservationDuplicatedException;
import roomescape.service.reservation.exception.ReservationNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import({ReservationService.class,
        H2ReservationRepository.class,
        H2ReservationTimeRepository.class,
        H2ThemeRepository.class,
        H2MemberRepository.class})
class ReservationServiceTest {

    final String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);

    List<ReservationTime> sampleTimes = List.of(
            new ReservationTime(null, "08:00"),
            new ReservationTime(null, "09:10")
    );
    List<Theme> sampleThemes = List.of(
            new Theme(null, "Theme 1", "Description 1", "Thumbnail 1"),
            new Theme(null, "Theme 2", "Description 2", "Thumbnail 2")
    );
    List<Member> sampleMembers = List.of(
            new Member(null, "User", "user@test.com", "user", Role.USER),
            new Member(null, "Admin", "admin@test.com", "admin", Role.ADMIN)
    );
    List<CreateReservationRequest> sampleReservations = List.of(
            new CreateReservationRequest(tomorrow, null, null, null),
            new CreateReservationRequest(tomorrow, null, null, null)
    );

    @Autowired
    ReservationService reservationService;
    @Autowired
    ReservationTimeRepository reservationTimeRepository;
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        sampleTimes = sampleTimes.stream()
                .map(reservationTimeRepository::save)
                .toList();
        sampleThemes = sampleThemes.stream()
                .map(themeRepository::save)
                .toList();
        sampleMembers = sampleMembers.stream()
                .map(memberRepository::save)
                .toList();
        sampleReservations = IntStream.range(0, sampleReservations.size())
                .mapToObj(i -> new CreateReservationRequest(
                        sampleReservations.get(i).date(),
                        sampleTimes.get(i % sampleTimes.size()).getId(),
                        sampleThemes.get(i % sampleThemes.size()).getId(),
                        sampleMembers.get(i % sampleMembers.size()).getId()
                )).toList();
    }

    @Test
    @DisplayName("예약 목록을 조회한다.")
    void getReservations() {
        // given
        List<ReservationResponse> expected = sampleReservations.stream()
                .map(reservationService::addReservation)
                .toList();

        // given & when
        List<ReservationResponse> actual = reservationService.getReservations();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void addReservation() {
        // given
        CreateReservationRequest createReservationRequest = sampleReservations.get(0);

        // when
        ReservationResponse actual = reservationService.addReservation(createReservationRequest);

        Optional<ReservationTime> timeOptional = sampleTimes.stream().filter(time -> time.getId().equals(createReservationRequest.timeId())).findAny();
        Optional<Theme> themeOptional = sampleThemes.stream().filter(theme -> theme.getId().equals(createReservationRequest.themeId())).findAny();
        Optional<Member> memberOptional = sampleMembers.stream().filter(member -> member.getId().equals(createReservationRequest.memberId())).findAny();
        assertThat(timeOptional).isPresent();
        assertThat(themeOptional).isPresent();
        assertThat(memberOptional).isPresent();

        ReservationResponse expected = new ReservationResponse(
                actual.id(),
                createReservationRequest.date(),
                TimeResponse.from(timeOptional.get(), false),
                ReservationThemeResponse.from(themeOptional.get()),
                new MemberCheckResponse(memberOptional.get().getName())
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() {
        // given
        CreateReservationRequest createReservationRequest = sampleReservations.get(0);
        LoginMember member = new LoginMember(createReservationRequest.memberId(), null, null, null);

        // when
        ReservationResponse actual = reservationService.addReservation(createReservationRequest);

        // then
        assertThat(reservationService.deleteReservation(actual.id(), member)).isOne();
        assertThatThrownBy(() -> reservationService.deleteReservation(actual.id(), member))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약을 할 때 예외가 발생한다.")
    void exceptionOnAddingReservationWithNonExistTime() {
        // given
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        Long notExistTimeId = sampleTimes.stream()
                .map(ReservationTime::getId)
                .max(Long::compare).stream()
                .findAny()
                .orElseThrow() + 1;
        Long themeId = sampleThemes.get(0).getId();
        Long memberId = sampleMembers.get(0).getId();
        CreateReservationRequest request = new CreateReservationRequest(tomorrow, notExistTimeId, themeId, memberId);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(TimeNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약을 할 때 예외가 발생한다.")
    void exceptionOnAddingReservationWithNonExistTheme() {
        // given
        String tomorrow = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        Long timeId = sampleTimes.get(0).getId();
        Long notExistThemeId = sampleThemes.stream()
                .map(Theme::getId)
                .max(Long::compare).stream()
                .findAny()
                .orElseThrow() + 1;
        Long memberId = sampleMembers.get(0).getId();
        CreateReservationRequest request = new CreateReservationRequest(tomorrow, timeId, notExistThemeId, memberId);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @Test
    @DisplayName("예약 하려는 일정이 오늘 1분 전일 경우 예외가 발생한다.")
    void validateReservationTimeAfterThanNow() {
        // given
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String oneMinAgo = LocalTime.now().minusMinutes(1).format(DateTimeFormatter.ofPattern("HH:mm"));

        ReservationTime time = reservationTimeRepository.save(new ReservationTime(null, oneMinAgo));
        Long themeId = sampleThemes.get(0).getId();
        Long memberId = sampleMembers.get(0).getId();
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(today, time.getId(), themeId, memberId);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(createReservationRequest))
                .isInstanceOf(PreviousTimeException.class);
    }

    @Test
    @DisplayName("중복된 시간으로 예약을 할 때 예외가 발생한다.")
    void duplicateDateTimeReservation() {
        // given
        CreateReservationRequest request = sampleReservations.get(0);
        reservationService.addReservation(request);


        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(request))
                .isInstanceOf(ReservationDuplicatedException.class);
    }
}
