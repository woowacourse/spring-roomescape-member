package roomescape.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.domain.*;
import roomescape.fake.FakeMemberRepository;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.fake.FakeThemeRepository;
import roomescape.persistence.query.CreateMemberQuery;
import roomescape.persistence.query.CreateReservationQuery;
import roomescape.service.param.CreateReservationParam;
import roomescape.service.result.LoginMemberResult;
import roomescape.service.result.ReservationResult;
import roomescape.service.result.ReservationTimeResult;
import roomescape.service.result.ThemeResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceTest {

    public static final LocalDate RESERVATION_DATE = LocalDate.now().plusDays(1);

    FakeReservationRepository reservationRepository = new FakeReservationRepository();
    FakeThemeRepository themeRepository = new FakeThemeRepository();
    FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
    FakeMemberRepository memberRepository = new FakeMemberRepository();

    ReservationService reservationService = new ReservationService(reservationTimeRepository, reservationRepository, themeRepository, memberRepository);

    @Test
    void 예약을_생성한다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(reservationTime);
        themeRepository.create(theme);
        memberRepository.create(new CreateMemberQuery("name", MemberRole.USER, "email", "password"));
        CreateReservationParam createReservationParam = new CreateReservationParam(1L, RESERVATION_DATE, 1L, 1L);

        //when
        Long createdId = reservationService.create(createReservationParam, LocalDateTime.now());

        //then
        assertThat(reservationRepository.findById(createdId))
                .hasValue(new Reservation(1L, new Member(1L, "name", MemberRole.USER, "email", "password"), RESERVATION_DATE, reservationTime, theme));
    }

    @Test
    void 예약을_생성할때_timeId가_데이터베이스에_존재하지_않는다면_예외가_발생한다() {
        //give
        themeRepository.create(new Theme(1L, "test", "description", "thumbnail"));
        memberRepository.create(new CreateMemberQuery("name", MemberRole.USER, "email", "password"));
        CreateReservationParam createReservationParam = new CreateReservationParam(1L, RESERVATION_DATE, 1L, 1L);

        //when & then
        assertThatThrownBy(() -> reservationService.create(createReservationParam, LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("1에 해당하는 정보가 없습니다.");
    }

    @Test
    void id값으로_예약을_삭제할_수_있다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(reservationTime);
        themeRepository.create(theme);
        memberRepository.create(new CreateMemberQuery("name", MemberRole.USER, "email", "password"));

        reservationRepository.create(new CreateReservationQuery(new Member(1L, "name", MemberRole.USER, "email", "password"), RESERVATION_DATE, reservationTime, theme));

        //when
        reservationService.deleteById(1L);

        //then
        assertThat(reservationRepository.findById(1L)).isEmpty();
    }

    @Test
    void 전체_예약을_조회할_수_있다() {
        //given
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(12, 1));
        ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(13, 1));
        themeRepository.create(theme);
        reservationTimeRepository.create(reservationTime1);
        reservationTimeRepository.create(reservationTime2);
        memberRepository.create(new CreateMemberQuery("name1", MemberRole.USER, "email1", "password1"));
        memberRepository.create(new CreateMemberQuery("name2", MemberRole.USER, "email1", "password2"));


        reservationRepository.create(new CreateReservationQuery(new Member(1L, "name1", MemberRole.USER, "email1", "password1"), RESERVATION_DATE, reservationTime1, theme));
        reservationRepository.create(new CreateReservationQuery(new Member(2L ,"name2", MemberRole.USER, "email2", "password2"), RESERVATION_DATE, reservationTime2, theme));

        //when
        List<ReservationResult> reservationResults = reservationService.findAll();

        //then
        assertThat(reservationResults).isEqualTo(List.of(
                new ReservationResult(1L, new LoginMemberResult(1L, "name1", "email1", "password1"), RESERVATION_DATE,
                        new ReservationTimeResult(1L, LocalTime.of(12, 1)),
                        new ThemeResult(1L, "test", "description", "thumbnail")),
                new ReservationResult(2L, new LoginMemberResult(2L, "name2", "email2", "password2"), RESERVATION_DATE,
                        new ReservationTimeResult(2L, LocalTime.of(13, 1)),
                        new ThemeResult(1L, "test", "description", "thumbnail"))
        ));
    }

    @Test
    void id_로_예약을_찾을_수_있다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(reservationTime);
        themeRepository.create(theme);
        memberRepository.create(new CreateMemberQuery("name1", MemberRole.USER, "email1", "password1"));

        reservationRepository.create(new CreateReservationQuery(new Member(1L, "name1", MemberRole.USER, "email1", "password1"), RESERVATION_DATE, reservationTime, theme));

        //when
        ReservationResult reservationResult = reservationService.findById(1L);

        //then
        assertThat(reservationResult).isEqualTo(
                new ReservationResult(1L, new LoginMemberResult(1L, "name1", "email1", "password1"),
                        RESERVATION_DATE,
                        new ReservationTimeResult(1L, LocalTime.of(12, 0)),
                        new ThemeResult(1L, "test", "description", "thumbnail"))
        );
    }

    @Test
    void id에_해당하는_예약이_없는경우_예외가_발생한다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(reservationTime);
        themeRepository.create(theme);
        memberRepository.create(new CreateMemberQuery("name1", MemberRole.USER, "email1", "password1"));

        reservationRepository.create(new CreateReservationQuery(new Member(1L ,"name1", MemberRole.USER, "email1", "password1"), RESERVATION_DATE, reservationTime, theme));

        //when & then
        assertThatThrownBy(() -> reservationService.findById(2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("2에 해당하는 reservation 튜플이 없습니다.");
    }

    @Test
    void 날짜와_시간이_중복된_예약이_있으면_예외가_발생한다() {
        //given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(reservationTime);
        themeRepository.create(theme);
        memberRepository.create(new CreateMemberQuery("name1", MemberRole.USER, "email1", "password1"));


        reservationRepository.create(new CreateReservationQuery(new Member(1L, "name1", MemberRole.USER, "email1", "password1"), RESERVATION_DATE, reservationTime, theme));

        //when & then
        assertThatThrownBy(() -> reservationService.create(new CreateReservationParam(1L, RESERVATION_DATE, 1L, 1L), LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마에 대해 날짜와 시간이 중복된 예약이 존재합니다.");
    }

    @ParameterizedTest
    @CsvSource({"2025-04-23T12:30, 2025-04-22T12:30", "2025-04-23T12:30, 2025-04-23T12:00"})
    void 지난_날짜에_대한_예약이라면_예외가_발생한다(LocalDateTime currentDateTime, LocalDateTime reservationDateTime) {
        //given
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(new ReservationTime(1L, reservationDateTime.toLocalTime()));
        themeRepository.create(theme);
        memberRepository.create(new CreateMemberQuery("name1", MemberRole.USER, "email1", "password1"));

        //when & then
        assertThatThrownBy(() -> reservationService.create(new CreateReservationParam(1L, reservationDateTime.toLocalDate(), 1L, 1L), currentDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 날짜와 시간에 대한 예약은 불가능합니다.");
    }

    @ParameterizedTest
    @CsvSource({"2025-04-23T12:30, 2025-04-23T12:30", "2025-04-23T12:30, 2025-04-23T12:39"})
    void 예약일이_오늘인_경우_예약_시간까지_10분도_남지_않았다면_예외가_발생한다(LocalDateTime currentDateTime, LocalDateTime reservationDateTime) {
        //given
        Theme theme = new Theme(1L, "test", "description", "thumbnail");
        reservationTimeRepository.create(new ReservationTime(1L, reservationDateTime.toLocalTime()));
        themeRepository.create(theme);
        memberRepository.create(new CreateMemberQuery("name1", MemberRole.USER, "email1", "password1"));

        //when & then
        assertThatThrownBy(() -> reservationService.create(new CreateReservationParam(1L, reservationDateTime.toLocalDate(), 1L, 1L), currentDateTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간까지 10분도 남지 않아 예약이 불가합니다.");
    }

}