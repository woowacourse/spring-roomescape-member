package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationSlot;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.dto.request.AvailableTimeRequest;
import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.request.CreateReservationTimeRequest;
import roomescape.exception.InvalidReservationTimeException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.ReservationService;
import roomescape.service.ReservationTimeService;
import roomescape.unit.repository.FakeMemberRepository;
import roomescape.unit.repository.FakeReservationRepository;
import roomescape.unit.repository.FakeReservationTimeRepository;
import roomescape.unit.repository.FakeThemeRepository;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;
    private ReservationService reservationService;

    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository();
        memberRepository = new FakeMemberRepository();

        reservationTimeService = new ReservationTimeService(reservationRepository, reservationTimeRepository);
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository,
                themeRepository, memberRepository);
    }

    @Test
    void 예약시간을_추가하고_조회할_수_있다() {
        reservationTimeService.addReservationTime(new CreateReservationTimeRequest(LocalTime.now().plusMinutes(30L)));
        assertThat(reservationTimeService.findAll()).hasSize(1);
    }

    @Test
    void 예약시간을_삭제하고_조회할_수_있다() {
        // given
        ReservationTime reservationTime = reservationTimeService.addReservationTime(
                new CreateReservationTimeRequest(LocalTime.now().plusMinutes(30L)));

        // when
        int before = reservationTimeService.findAll().size();
        reservationTimeService.deleteReservationTime(reservationTime.getId());
        int after = reservationTimeService.findAll().size();

        //then
        assertAll(() -> {
            assertThat(before).isEqualTo(1);
            assertThat(after).isEqualTo(0);
        });
    }

    @Test
    void 특정_시간에_대한_예약이_존재할때_시간을_삭제하려고하면_예외가_발생한다() {
        // given
        Member member = new Member(0L, "Hula", "test@test.com", "test", Role.USER);

        LocalTime startAt = LocalTime.now().plusMinutes(30L);
        CreateReservationTimeRequest requset = new CreateReservationTimeRequest(startAt);
        ReservationTime reservationTime = reservationTimeService.addReservationTime(requset);

        Theme theme = new Theme(0L, "공포", "공포테마입니다.", "thumbnail");

        Reservation reservation = new Reservation(member,
                LocalDate.now().plusDays(1), reservationTime, theme);
        reservationRepository.add(reservation);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(reservationTime.getId()))
                .isInstanceOf(InvalidReservationTimeException.class);
    }

    @Test
    void 중복_시간을_설정할_수_없다() {
        // given
        LocalTime startAt = LocalTime.now().plusMinutes(30L);
        CreateReservationTimeRequest initialReservationTime = new CreateReservationTimeRequest(startAt);
        reservationTimeService.addReservationTime(initialReservationTime);

        // when
        CreateReservationTimeRequest duplicateAddReservationTime = new CreateReservationTimeRequest(startAt);

        //then
        assertThatThrownBy(() -> reservationTimeService.addReservationTime(duplicateAddReservationTime))
                .isInstanceOf(InvalidReservationTimeException.class);
    }

    @Test
    void 존재하지_않는_시간을_조회시_예외가_발생한다() {
        assertThatThrownBy(() -> reservationTimeService.getReservationTimeById(-1L))
                .isInstanceOf(InvalidReservationTimeException.class);
    }



    @Test
    void 선택된_테마와_날짜에_대해서_가능한_시간들을_확인할_수_있다2() {
        // given
        Member beforeAddMember = new Member( "Hula", "test@test.com", "test", Role.USER);
        Member member = memberRepository.add(beforeAddMember);
        LoginMember loginMember = new LoginMember(member.getId(), member.getName(), member.getRole());

        LocalDate today = LocalDate.now();
        LocalTime firstTime = LocalTime.now().plusHours(1L);
        LocalTime secondTime = LocalTime.now().plusHours(2L);

        ReservationTime reservationTime1 = new ReservationTime( firstTime);
        ReservationTime firstReservationTime = reservationTimeRepository.add(reservationTime1);
        ReservationTime reservationTime2 = new ReservationTime( secondTime);
        ReservationTime secondReservationTime = reservationTimeRepository.add(reservationTime2);

        Theme theme = new Theme( "테마", "설명", "image.png");
        long themeId = themeRepository.add(theme).getId();

        reservationService.addReservation(
                new CreateReservationRequest(today, firstReservationTime.getId(), themeId), loginMember);

        // when
        AvailableTimeRequest availableTimeRequest = new AvailableTimeRequest(today, themeId);
        List<ReservationSlot> reservationSlots = reservationTimeService.getReservationSlots(
                        availableTimeRequest)
                .getReservationSlots();

        //then
        List<ReservationSlot> expected = List.of(new ReservationSlot(1L, firstTime, true),
                new ReservationSlot(2L, secondTime, false));

        assertThat(reservationSlots).containsExactlyInAnyOrderElementsOf(expected);
    }

}
