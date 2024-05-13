package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.LoginUtils;
import roomescape.domain.*;
import roomescape.dto.request.ReservationAddMemberRequest;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.member.MemberRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static roomescape.InitialDataFixture.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    private Reservation reservation;
    private Member member;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(USER_1);
        Theme theme = themeRepository.save(THEME_1);
        ReservationTime time = reservationTimeRepository.save(RESERVATION_TIME_1);
        reservation = reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(1), time, theme));
    }

    @Test
    @DisplayName("예약을 추가하고 id값을 붙여서 응답 DTO를 생성한다.")
    void addReservation() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(LocalDate.now().plusDays(1), 1L, 1L);
        ReservationAddMemberRequest reservationAddMemberRequest = new ReservationAddMemberRequest(new MemberResponse(member));
        ReservationResponse reservationResponse = reservationService.addReservation(reservationAddRequest, reservationAddMemberRequest);

        assertThat(reservationResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 time_id로 예약을 추가하면 예외를 발생시킨다.")
    void addReservationInvalidGetTimeGetId() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(LocalDate.now().plusDays(1), -1L, 1L);
        ReservationAddMemberRequest reservationAddMemberRequest = new ReservationAddMemberRequest(new MemberResponse(member));
        assertThatThrownBy(() -> reservationService.addReservation(reservationAddRequest, reservationAddMemberRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("id에 맞는 예약을 삭제한다.")
    void deleteReservation() {
        Integer beforeCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
        reservationService.deleteReservation(1L);

        Integer afterCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
        assertThat(afterCount).isEqualTo(beforeCount - 1);
    }

    @Test
    @DisplayName("같은 날짜, 시간, 테마에 예약을 하는 경우 예외를 발생시킨다.")
    void saveSameReservation() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId());
        assertThatThrownBy(() -> reservationService.addReservation(reservationAddRequest, new ReservationAddMemberRequest(new MemberResponse(member)))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("시간과 날짜만 같고 테마가 다른 경우 예약에 성공한다.")
    void saveOnlySameGetDateGetTime() {
        Theme theme = themeRepository.save(new Theme(null, new Name("some"), "test", "test"));
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(
                reservation.getDate(),
                reservation.getTime().getId(),
                theme.getId());
        assertThatNoException().isThrownBy(() -> reservationService.addReservation(reservationAddRequest, new ReservationAddMemberRequest(new MemberResponse(member))));
    }

    @Test
    @DisplayName("테마가 같고 날짜가 다른 경우 예약에 성공한다.")
    void saveOnlySameTheme() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(
                reservation.getDate().plusDays(1),
                reservation.getTime().getId(),
                reservation.getTheme().getId());
        assertThatNoException().isThrownBy(() -> reservationService.addReservation(reservationAddRequest, new ReservationAddMemberRequest(new MemberResponse(member))));
    }
}
