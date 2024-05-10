package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ReservationFilterRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationSaveRequest;
import roomescape.dto.ReservationWithMemberSaveRequest;
import roomescape.model.*;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("사용자 예약 저장")
    @Test
    void saveReservationByUser() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole(), member.getEmail());
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest(LocalDate.parse("2025-11-11"), reservationTime.getId(), theme.getId());
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest, loginMember);
        final Reservation savedReservation = reservationRepository.findById(reservationResponse.id()).get();

        assertThat(reservationResponse).isEqualTo(new ReservationResponse(savedReservation));
    }

    @DisplayName("관리자용 예약 저장")
    @Test
    void saveReservationByAdmin() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final ReservationWithMemberSaveRequest reservationSaveRequest = new ReservationWithMemberSaveRequest(member.getId(), LocalDate.parse("2025-11-11"), reservationTime.getId(), theme.getId());
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest);
        final Reservation savedReservation = reservationRepository.findById(reservationResponse.id()).get();

        assertThat(reservationResponse).isEqualTo(new ReservationResponse(savedReservation));
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약 저장 시 예외 발생")
    @Test
    void timeForSaveReservationNotFound() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole(), member.getEmail());
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest(LocalDate.parse("2025-11-11"), 1L, theme.getId());

        assertThatThrownBy(() ->
            reservationService.saveReservation(reservationSaveRequest, loginMember)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 예약 시간입니다.");
    }

    @DisplayName("존재하지 않는 테마로 예약 저장 시 예외 발생")
    @Test
    void timeForSaveThemeNotFound() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole(), member.getEmail());
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest(LocalDate.parse("2025-11-11"), reservationTime.getId(), 1L);

        assertThatThrownBy(() ->
            reservationService.saveReservation(reservationSaveRequest, loginMember)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 테마입니다.");
    }

    @DisplayName("지나간 시간에 대한 예약 저장 시 예외 발생")
    @Test
    void saveReservationWithGoneDate() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole(), member.getEmail());
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));

        assertThatThrownBy(() -> {
            reservationService.saveReservation(new ReservationSaveRequest(LocalDate.now().minusDays(1), reservationTime.getId(), theme.getId()), loginMember);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("지나간 시간에 대한 예약은 생성할 수 없습니다.");
    }

    @DisplayName("동일한 날짜, 시간, 테마에 대한 예약 저장 시 예외 발생")
    @Test
    void saveReservationWithDuplicatedDateTimeTheme() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole(), member.getEmail());
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final LocalDate localDate = LocalDate.now().plusMonths(1);
        reservationService.saveReservation(new ReservationSaveRequest(localDate, reservationTime.getId(), theme.getId()), loginMember);

        assertThatThrownBy(() ->
                reservationService.saveReservation(new ReservationSaveRequest(localDate, reservationTime.getId(), theme.getId()), loginMember)
        ).isInstanceOf(IllegalArgumentException.class).hasMessage("동일한 날짜, 시간, 테마에 대한 예약이 이미 존재합니다.");
    }

    @DisplayName("dateFrom이 dateTo 이후의 날짜일 경우 예외 발생")
    @Test
    void invalidReservationFilterDate() {
        assertThatThrownBy(() -> reservationService.getReservations(new ReservationFilterRequest(null, null, LocalDate.now().plusMonths(1), LocalDate.now())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("dateFrom이 dateTo 이후의 날짜입니다.");
    }

    @DisplayName("전체 예약 목록 조회")
    @Test
    void getReservations() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole(), member.getEmail());
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        themeRepository.save(new Theme("이름2", "설명2", "썸네일2"));
        reservationService.saveReservation(new ReservationSaveRequest(LocalDate.now().plusMonths(1), 1L, 1L), loginMember);
        reservationService.saveReservation(new ReservationSaveRequest(LocalDate.now().plusMonths(2), 2L, 2L), loginMember);
        final List<ReservationResponse> reservationResponses = reservationService.getReservations(new ReservationFilterRequest(null, null, null, null));

        assertThat(reservationResponses).hasSize(2)
                .containsExactly(
                        new ReservationResponse(reservationRepository.findById(1L).get()),
                        new ReservationResponse(reservationRepository.findById(2L).get())
                );
    }

    @DisplayName("예약 목록 필터링 후 조회")
    @Test
    void getReservationsByFilter() {
        final List<Long> memberIds = List.of(
                memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234")),
                memberRepository.save(new Member("고구마", Role.USER, "222@aaa.com", "abc1234")))
                .stream()
                .map(Member::getId)
                .toList();
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        themeRepository.save(new Theme("이름2", "설명2", "썸네일2"));

        final LocalDate localDate = LocalDate.now().plusMonths(1);
        reservationService.saveReservation(new ReservationWithMemberSaveRequest(memberIds.get(0), localDate, 1L, 1L));
        reservationService.saveReservation(new ReservationWithMemberSaveRequest(memberIds.get(0), localDate, 2L, 1L));
        reservationService.saveReservation(new ReservationWithMemberSaveRequest(memberIds.get(1), localDate.plusMonths(1), 1L, 2L));
        reservationService.saveReservation(new ReservationWithMemberSaveRequest(memberIds.get(0), LocalDate.now().plusMonths(2), 2L, 2L));
        reservationService.saveReservation(new ReservationWithMemberSaveRequest(memberIds.get(0), localDate, 2L, 2L));
        final List<ReservationResponse> reservationResponses = reservationService.getReservations(new ReservationFilterRequest(1L, memberIds.get(0), localDate, localDate));

        assertThat(reservationResponses).hasSize(2)
                .containsExactly(
                        new ReservationResponse(reservationRepository.findById(1L).get()),
                        new ReservationResponse(reservationRepository.findById(2L).get())
                );
    }

    @DisplayName("예약 삭제")
    @Test
    void deleteReservation() {
        final Member member = memberRepository.save(new Member("감자", Role.USER, "111@aaa.com", "abc1234"));
        final LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole(), member.getEmail());
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final ReservationSaveRequest reservationSaveRequest = new ReservationSaveRequest(LocalDate.parse("2025-11-11"), reservationTime.getId(), theme.getId());
        final ReservationResponse reservationResponse = reservationService.saveReservation(reservationSaveRequest, loginMember);
        reservationService.deleteReservation(reservationResponse.id());

        assertThat(reservationRepository.findById(reservationResponse.id())).isEmpty();
    }

    @DisplayName("존재하지 않는 예약 삭제 시 예외 발생")
    @Test
    void deleteReservationNotFound() {
        assertThatThrownBy(() -> {
            reservationService.deleteReservation(1L);
        }).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 예약입니다.");
    }
}
