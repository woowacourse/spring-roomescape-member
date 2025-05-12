package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.Member;
import roomescape.business.MemberRole;
import roomescape.business.ReservationTheme;
import roomescape.business.ReservationTime;
import roomescape.persistence.FakeMemberRepository;
import roomescape.persistence.FakeReservationRepository;
import roomescape.persistence.FakeReservationThemeRepository;
import roomescape.persistence.FakeReservationTimeRepository;
import roomescape.exception.ReservationException;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.ReservationRepository;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.ReservationTimeRepository;
import roomescape.presentation.dto.request.ReservationRequestDto;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationTimeRepository reservationTimeRepository;
    private ReservationThemeRepository reservationThemeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        reservationThemeRepository = new FakeReservationThemeRepository();
        memberRepository = new FakeMemberRepository();
        reservationService = new ReservationService(
                reservationRepository,
                reservationTimeRepository,
                reservationThemeRepository,
                memberRepository
        );
    }

    @DisplayName("예약한다.")
    @Test
    void createReservation() {
        // given
        LocalDate tomorrow = LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now(ZoneId.of("Asia/Seoul"))));
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일"));
        Long memberId = memberRepository.add(new Member("예약자", "hi@hi.hi", "1234", MemberRole.ADMIN));

        // when
        reservationService.createReservation(new ReservationRequestDto(tomorrow, timeId, themeId), memberId);

        // then
        assertThat(reservationService.readReservationAll())
                .isNotEmpty();
    }

    @DisplayName("과거 일시로 예약을 생성할 경우 예외가 발생한다.")
    @Test
    void createPastReservation() {
        // given
        LocalDateTime pastDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(pastDateTime.toLocalTime()));
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일"));
        Long memberId = memberRepository.add(new Member("예약자", "hi@hi.hi", "1234", MemberRole.ADMIN));
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(pastDateTime.toLocalDate(), timeId,
                themeId);

        // when
        // then
        assertThatCode(() -> reservationService.createReservation(reservationRequestDto, memberId))
                .isInstanceOf(ReservationException.class)
                .hasMessage("과거 일시로 예약을 생성할 수 없습니다.");
    }

    @DisplayName("이미 예약된 경우 예약할 수 없다.")
    @Test
    void failCreateReservation() {
        // given
        LocalDate tomorrow = LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1);
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now(ZoneId.of("Asia/Seoul"))));
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일"));
        Long memberId = memberRepository.add(new Member("예약자", "hi@hi.hi", "1234", MemberRole.ADMIN));
        reservationService.createReservation(new ReservationRequestDto(tomorrow, timeId, themeId), memberId);

        // when
        // then
        assertThatCode(
                () -> reservationService.createReservation(
                        new ReservationRequestDto(tomorrow, timeId, themeId),
                        memberId))
                .isInstanceOf(ReservationException.class)
                .hasMessage("이미 예약되었습니다.");
    }

    @DisplayName("예약을 취소한다.")
    @Test
    void deleteReservation() {
        // given
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now(ZoneId.of("Asia/Seoul"))));
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일"));
        Long memberId = memberRepository.add(new Member("예약자", "hi@hi.hi", "1234", MemberRole.ADMIN));
        Long id = reservationService.createReservation(new ReservationRequestDto(
                        LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1),
                        timeId,
                        themeId),
                memberId
        );

        // when
        reservationService.deleteReservation(id);

        // then
        assertThat(reservationService.readReservationAll()).isEmpty();
    }

    @DisplayName("예약 목록을 불러온다")
    @Test
    void readReservationAll() {
        // given
        Long timeId = reservationTimeRepository.add(new ReservationTime(LocalTime.now(ZoneId.of("Asia/Seoul"))));
        Long themeId = reservationThemeRepository.add(new ReservationTheme("테마", "설명", "썸네일"));
        Long memberId = memberRepository.add(new Member("예약자", "hi@hi.hi", "1234", MemberRole.ADMIN));
        Long id = reservationService.createReservation(
                new ReservationRequestDto(
                        LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1),
                        timeId,
                        themeId),
                memberId
        );

        // when
        int firstReadSize = reservationService.readReservationAll().size();
        reservationService.deleteReservation(id);
        int secondReadSize = reservationService.readReservationAll().size();

        // then
        assertThat(firstReadSize).isEqualTo(1);
        assertThat(secondReadSize).isEqualTo(0);
    }
}
