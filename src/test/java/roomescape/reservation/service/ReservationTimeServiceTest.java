package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.LoginMember;
import roomescape.reservation.dao.FakeMemberDao;
import roomescape.reservation.dao.FakeReservationDao;
import roomescape.reservation.dao.FakeReservationTimeDao;
import roomescape.reservation.dao.FakeThemeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;
import roomescape.reservation.dto.AvailableTimeResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationTimeRequest;
import roomescape.reservation.dto.ReservationTimeResponse;

@DisplayName("예약 시간 로직 테스트")
class ReservationTimeServiceTest {
    ReservationRepository reservationRepository;
    ReservationTimeRepository reservationTimeRepository;
    ThemeRepository themeRepository;
    MemberRepository memberRepository;

    ReservationTimeService reservationTimeService;
    ReservationService reservationService;

    @BeforeEach
    void setData() {
        memberRepository = new FakeMemberDao();
        themeRepository = new FakeThemeDao(reservationRepository);
        reservationRepository = new FakeReservationDao(new FakeMemberDao());
        reservationTimeRepository = new FakeReservationTimeDao(reservationRepository);

        reservationTimeService = new ReservationTimeService(reservationRepository, reservationTimeRepository);
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository, memberRepository);
    }

    @DisplayName("예약 시간 생성에 성공한다.")
    @Test
    void create() {
        //given
        String localTime = "12:00";
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(localTime);

        //when
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(reservationTimeRequest);

        //then
        assertThat(reservationTimeResponse.startAt()).isEqualTo(localTime);
        assertThat(reservationTimeRepository.findAll()).hasSize(1);
    }

    @DisplayName("예약 시간이 증복일 경우, 예외가 발생한다.")
    @Test
    void duplicatedTime() {
        //given
        String localTime = "12:00";
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(localTime);
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse(localTime)));

        //when & then
        assertThatThrownBy(() -> reservationTimeService.create(reservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 시간 조회에 성공한다.")
    @Test
    void findAll() {
        //given
        long id = 1L;
        LocalTime localTime = LocalTime.MIDNIGHT;
        reservationTimeRepository.save(new ReservationTime(id, localTime));

        //when
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();

        //then
        assertThat(reservationTimes).hasSize(1);
    }

    @DisplayName("예약 시간 삭제에 성공한다.")
    @Test
    void delete() {
        //given
        long id = 1L;
        LocalTime localTime = LocalTime.MIDNIGHT;
        reservationTimeRepository.save(new ReservationTime(id, localTime));

        //when
        reservationTimeService.delete(id);

        //then
        assertThat(reservationTimeRepository.findAll()).hasSize(0);
    }

    @DisplayName("예약이 존재하는 예약 시간을 삭제할 경우 예외가 발생한다.")
    @Test
    void deleteTimeWithReservation() {
        //given
        long id = 1L;
        LocalTime localTime = LocalTime.MIDNIGHT;
        ReservationTime saveTime = reservationTimeRepository.save(new ReservationTime(id, localTime));

        Reservation reservation = new Reservation(1L, LocalDate.now().plusYears(1), saveTime,
                new Theme("name", "description", "thumbnail"));
        reservationRepository.save(reservation);

        //when & then
        assertThatThrownBy(() -> reservationTimeService.delete(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 예약 시간을 삭제할 경우 예외가 발생한다.")
    @Test
    void deleteNotExistTime() {
        // given & when & then
        assertThatThrownBy(() -> reservationTimeService.delete(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약이 가능한 시간 찾기에 성공한다.")
    @Test
    void findAvailableTimes() {
        //given
        long id1 = 1L;
        LocalTime localTime1 = LocalTime.MIDNIGHT;
        ReservationTime saveTime1 = reservationTimeRepository.save(new ReservationTime(id1, localTime1));

        long id2 = 2L;
        LocalTime localTime2 = LocalTime.NOON;
        ReservationTime saveTime2 = reservationTimeRepository.save(new ReservationTime(id2, localTime2));

        LocalDate date = LocalDate.now().plusYears(1);


        Member member = memberRepository.save(new Member("name", "email@email.com", "Password", Role.MEMBER));
        themeRepository.save(new Theme("test", "test", "test"));
        ReservationRequest reservationRequest = new ReservationRequest(date.toString(), 1, 1);
        reservationService.create(reservationRequest,
                new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole()));

        //when
        List<AvailableTimeResponse> availableTimeResponses = reservationTimeService.findAvailableTimes(date, 1L);

        //then
        assertAll(
                () -> assertThat(availableTimeResponses).hasSize(2),
                () -> assertThat(availableTimeResponses.get(0)).isEqualTo(
                        new AvailableTimeResponse(id1, localTime1, true)),
                () -> assertThat(availableTimeResponses.get(1)).isEqualTo(
                        new AvailableTimeResponse(id2, localTime2, false))
        );
    }
}
