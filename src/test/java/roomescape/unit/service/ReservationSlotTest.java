package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.ReservationSlot;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.request.AvailableTimeRequest;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.MemberService;
import roomescape.service.ReservationService;
import roomescape.service.ReservationSlotService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;
import roomescape.unit.repository.FakeMemberRepository;
import roomescape.unit.repository.FakeReservationRepository;
import roomescape.unit.repository.FakeReservationTimeRepository;
import roomescape.unit.repository.FakeThemeRepository;

public class ReservationSlotTest {

    private ReservationService reservationService;
    private ReservationSlotService reservationSlotService;

    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository(reservationRepository);
        themeRepository = new FakeThemeRepository(reservationRepository);
        memberRepository = new FakeMemberRepository();

        MemberService memberService = new MemberService(memberRepository);
        ThemeService themeService = new ThemeService(themeRepository);
        ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeRepository);
        reservationService = new ReservationService(reservationTimeService, themeService, memberService,
                reservationRepository);
        reservationSlotService = new ReservationSlotService(reservationService, reservationTimeService);
    }

    @Test
    void 선택된_테마와_날짜에_대해서_가능한_시간들을_확인할_수_있다2() {
        // given
        Member beforeAddMember = new Member("Hula", "test@test.com", "test", Role.USER);
        Member member = memberRepository.add(beforeAddMember);
        LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole());

        LocalDate today = LocalDate.now();
        LocalTime firstTime = LocalTime.now().plusHours(1L);
        LocalTime secondTime = LocalTime.now().plusHours(2L);

        ReservationTime reservationTime1 = new ReservationTime(firstTime);
        ReservationTime firstReservationTime = reservationTimeRepository.add(reservationTime1);
        ReservationTime reservationTime2 = new ReservationTime(secondTime);
        ReservationTime secondReservationTime = reservationTimeRepository.add(reservationTime2);

        Theme theme = new Theme("테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        reservationService.addReservation(
                new CreateReservationRequest(today, firstReservationTime.getId(), themeId), loginMember);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(today, themeId);
        List<ReservationSlot> reservationSlots = reservationSlotService.getReservationSlots(
                        availableTimeRequest)
                .getReservationSlots();

        //then
        List<ReservationSlot> expected = List.of(new ReservationSlot(1L, firstTime, true),
                new ReservationSlot(2L, secondTime, false));

        assertThat(reservationSlots).containsExactlyInAnyOrderElementsOf(expected);
    }
}
