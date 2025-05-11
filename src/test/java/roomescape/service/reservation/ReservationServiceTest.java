package roomescape.service.reservation;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.member.FakeMemberDaoImpl;
import roomescape.dao.reservation.FakeReservationDaoImpl;
import roomescape.dao.reservationtime.FakeReservationTimeDaoImpl;
import roomescape.dao.theme.FakeThemeDaoImpl;
import roomescape.dao.member.MemberDao;
import roomescape.dao.reservation.ReservationDao;
import roomescape.dao.reservationtime.ReservationTimeDao;
import roomescape.dao.theme.ThemeDao;
import roomescape.domain.member.LoginMember;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.dto.admin.request.AdminReservationRequest;
import roomescape.dto.admin.request.SearchConditionRequest;
import roomescape.dto.member.response.MemberResponse;
import roomescape.dto.reservation.request.ReservationRequestDto;
import roomescape.dto.reservation.response.ReservationResponseDto;
import roomescape.dto.reservationtime.response.ReservationTimeResponseDto;
import roomescape.dto.theme.response.ThemeResponseDto;

public class ReservationServiceTest {

    private static final LocalDate NOW_DATE = LocalDate.now().plusDays(1);

    private ReservationService reservationService;
    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ThemeDao themeDao;
    private MemberDao memberDao;

    @BeforeEach
    void init() {
        reservationDao = new FakeReservationDaoImpl();
        reservationTimeDao = new FakeReservationTimeDaoImpl();
        themeDao = new FakeThemeDaoImpl();
        memberDao = new FakeMemberDaoImpl();
        reservationService = new ReservationService(reservationDao, reservationTimeDao, themeDao, memberDao);
    }

    @DisplayName("ReservationRequestDto가 주어졌을 때, Fake 객체에 정상적으로 저장되어야 한다.")
    @Test
    void given_reservation_request_dto_then_save() {
        //given
        LoginMember loginMember = new LoginMember(1L, "testName", "testEmail", MemberRole.USER);

        memberDao.save(Member.from(1L, "testName", "testEmail", "1234", MemberRole.USER));
        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 30)));
        themeDao.saveTheme(new Theme(1L, "name", "description", "thumbnail"));
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
                NOW_DATE.toString(), 1L, 1L);

        //when
        reservationService.saveReservation(reservationRequestDto, loginMember);

        //then
        assertThat(reservationService.getAllReservations().size()).isEqualTo(1);
    }

    @DisplayName("여러 번 Reservation을 저장할 때, 성공적으로 Fake객체에 저장되고, 읽어올 수 있어야 한다.")
    @Test
    void given_multiple_reservation_request_dto_then_all_save() {

        memberDao.save(Member.from(1L, "testName1", "testEmail1", "12", MemberRole.USER));
        memberDao.save(Member.from(2L, "testName2", "testEmail2", "123", MemberRole.USER));
        memberDao.save(Member.from(3L, "testName3", "testEmail3", "1234", MemberRole.USER));

        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 30)));
        reservationTimeDao.saveReservationTime(new ReservationTime(2L, LocalTime.of(11, 30)));
        reservationTimeDao.saveReservationTime(new ReservationTime(3L, LocalTime.of(12, 30)));

        themeDao.saveTheme(new Theme(1L, "name1", "description1", "thumbnail1"));
        themeDao.saveTheme(new Theme(2L, "name2", "description2", "thumbnail2"));
        themeDao.saveTheme(new Theme(3L, "name3", "description3", "thumbnail3"));

        LoginMember loginMember1 = new LoginMember(1L, "testName1", "testEmail1", MemberRole.USER);
        LoginMember loginMember2 = new LoginMember(2L, "testName2", "testEmail2", MemberRole.USER);
        LoginMember loginMember3 = new LoginMember(3L, "testName3", "testEmail3", MemberRole.USER);

        //given
        ReservationRequestDto reservationRequestDto1 = new ReservationRequestDto(
                NOW_DATE.toString(), 1L, 1L);
        ReservationRequestDto reservationRequestDto2 = new ReservationRequestDto(
                NOW_DATE.plusDays(1).toString(), 2L, 2L);
        ReservationRequestDto reservationRequestDto3 = new ReservationRequestDto(
                NOW_DATE.plusDays(2).toString(), 3L, 3L);

        //when
        reservationService.saveReservation(reservationRequestDto1, loginMember1);
        reservationService.saveReservation(reservationRequestDto2, loginMember2);
        reservationService.saveReservation(reservationRequestDto3, loginMember3);

        //then
        assertThat(reservationService.getAllReservations().size()).isEqualTo(3);
    }

    @DisplayName("reservationId가 주어졌을 떄, Fake 객체에서 삭제되어야 한다.")
    @Test
    void given_reservation_id_then_remove_db() {
        //given
        LoginMember loginMember = new LoginMember(1L, "testName", "testEmail", MemberRole.USER);
        memberDao.save(Member.from(1L, "testName", "testEmail", "1234", MemberRole.USER));
        reservationTimeDao.saveReservationTime(new ReservationTime(1L, LocalTime.of(10, 30)));
        themeDao.saveTheme(new Theme(1L, "name", "description", "thumbnail"));

        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(NOW_DATE.toString(), 1L, 1L);
        reservationService.saveReservation(reservationRequestDto, loginMember);
        //when
        reservationService.deleteReservation(1L);

        //then
        assertThat(reservationService.getAllReservations().size()).isEqualTo(0);
    }

    @DisplayName("관리자가 예약 생성 시, 유저를 조회하여 선택 후 예약을 생성할 수 있다.")
    @Test
    void reservationAsAdmin() {
        //given
        Theme theme = new Theme(1L, "tesTheme", "testDescription", "testThumbnail");
        themeDao.saveTheme(theme);

        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime);

        Member member = Member.from(1L, "testName", "testEmail", "1234", MemberRole.USER);
        memberDao.save(member);

        AdminReservationRequest request = new AdminReservationRequest(NOW_DATE, 1L, 1L, 1L);

        //when
        ReservationResponseDto response = reservationService.saveReservation(request);

        //then
        assertThat(response).isEqualTo(
                new ReservationResponseDto(
                        1L,
                        MemberResponse.from(member),
                        NOW_DATE.toString(),
                        ReservationTimeResponseDto.from(reservationTime),
                        ThemeResponseDto.from(theme)
                )
        );
    }

    @DisplayName("관리자는 예약자별, 테마별, 날짜별 검색 조건을 사용해 예약 검색이 가능하다.")
    @Test
    void findByCondition() {
        //given
        Theme theme = new Theme(1L, "tesTheme", "testDescription", "testThumbnail");
        themeDao.saveTheme(theme);

        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        reservationTimeDao.saveReservationTime(reservationTime);

        Member member = Member.from(1L, "testName", "testEmail", "1234", MemberRole.ADMIN);
        memberDao.save(member);

        reservationDao.saveReservation(
                new Reservation(
                        1L,
                        member,
                        new ReservationDate(LocalDate.of(2025, 5, 1)),
                        reservationTime,
                        theme)
        );

        reservationDao.saveReservation(
                new Reservation(
                        2L,
                        member,
                        new ReservationDate(LocalDate.of(2025, 5, 3)),
                        reservationTime,
                        theme)
        );

        reservationDao.saveReservation(
                new Reservation(
                        3L,
                        member,
                        new ReservationDate(LocalDate.of(2025, 5, 5)),
                        reservationTime,
                        theme)
        );

        SearchConditionRequest request = new SearchConditionRequest(
                1L,
                1L,
                LocalDate.of(2025, 5, 1),
                LocalDate.of(2025, 5, 4)
        );

        //when
        List<ReservationResponseDto> actual = reservationService.findByCondition(request);

        //then
        assertThat(actual).hasSize(2);
    }


}
