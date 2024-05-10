package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.EMAIL_FIXTURE;
import static roomescape.TestFixture.MEMBER_NAME_FIXTURE;
import static roomescape.TestFixture.MEMBER_PARAMETER_SOURCE;
import static roomescape.TestFixture.PASSWORD_FIXTURE;
import static roomescape.TestFixture.THEME_NAME_FIXTURE;
import static roomescape.TestFixture.TIME_FIXTURE;
import static roomescape.TestFixture.createReservationTime;
import static roomescape.TestFixture.createTheme;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.TestFixture;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Role;
import roomescape.dto.request.MemberReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.InvalidInputException;
import roomescape.exception.TargetNotExistException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationServiceTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");
    }

    @DisplayName("모든 예약 검색")
    @Test
    void findAll() {
        assertThat(reservationService.findAll()).isEmpty();
    }

    @DisplayName("특정 예약 검색")
    @Test
    void findAllMatching() {
        // given
        Member member = createAndGetMember();
        MemberReservationRequest request = createMemberReservationRequest(
                LocalDate.of(4000, 12, 12));
        MemberReservationRequest outOfFilterRequest = createMemberReservationRequest(
                LocalDate.of(4000, 12, 12));
        ReservationResponse response = reservationService.save(request, member);
        reservationService.save(outOfFilterRequest, member);
        // when
        List<ReservationResponse> filtered = reservationService.findAllMatching(
                request.themeId(),
                member.getId(),
                LocalDate.of(3000, 12, 12),
                LocalDate.of(5000, 12, 12));
        // then
        assertThat(filtered).hasSize(1);
        assertThat(filtered.get(0).id()).isEqualTo(response.id());
    }

    @DisplayName("예약 저장")
    @Test
    void save() {
        // given
        Member member = createAndGetMember();
        MemberReservationRequest memberReservationRequest = createMemberReservationRequest(
                DATE_FIXTURE);
        // when
        ReservationResponse response = reservationService.save(memberReservationRequest, member);
        // then
        assertAll(
                () -> assertThat(reservationService.findAll()).hasSize(1),
                () -> assertThat(response.member().id()).isEqualTo(member.getId()),
                () -> assertThat(response.date()).isEqualTo(DATE_FIXTURE),
                () -> assertThat(response.time().startAt()).isEqualTo(TIME_FIXTURE),
                () -> assertThat(response.theme().name()).isEqualTo(THEME_NAME_FIXTURE)
        );
    }

    @DisplayName("지난 예약을 저장하려 하면 예외가 발생한다.")
    @Test
    void pastReservationSave() {
        // given
        Member member = createAndGetMember();
        MemberReservationRequest memberReservationRequest = createMemberReservationRequest(
                LocalDate.of(2000, 11, 9));
        // when & then
        assertThatThrownBy(() -> reservationService.save(memberReservationRequest, member))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("지난 날짜에는 예약할 수 없습니다.");
    }

    @DisplayName("중복 예약을 저장하려 하면 예외가 발생한다.")
    @Test
    void duplicatedReservationSave() {
        // given
        Member member = createAndGetMember();
        MemberReservationRequest memberReservationRequest = createMemberReservationRequest(
                DATE_FIXTURE);
        reservationService.save(memberReservationRequest, member);
        // when & then
        assertThatThrownBy(() -> reservationService.save(memberReservationRequest, member))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약이 이미 존재합니다.");
    }

    @DisplayName("삭제 테스트")
    @Test
    void deleteById() {
        // given
        Member member = createAndGetMember();
        MemberReservationRequest memberReservationRequest = createMemberReservationRequest(
                DATE_FIXTURE);
        ReservationResponse response = reservationService.save(memberReservationRequest, member);
        // when
        reservationService.deleteById(response.id());
        // then
        assertThat(reservationService.findAll()).isEmpty();
    }

    @DisplayName("존재하지 않는 id의 대상을 삭제하려 하면 예외가 발생한다.")
    @Test
    void deleteByNotExistingId() {
        assertThatThrownBy(() -> reservationService.deleteById(-1L))
                .isInstanceOf(TargetNotExistException.class)
                .hasMessage("삭제할 예약이 존재하지 않습니다.");
    }

    private Member createAndGetMember() {
        Long memberId = TestFixture.createMember(jdbcTemplate, MEMBER_PARAMETER_SOURCE);
        return new Member(memberId, new Name(MEMBER_NAME_FIXTURE), Role.NORMAL, EMAIL_FIXTURE,
                PASSWORD_FIXTURE);
    }

    private MemberReservationRequest createMemberReservationRequest(LocalDate date) {
        Long timeId = createReservationTime(jdbcTemplate);
        Long themeId = createTheme(jdbcTemplate);
        return new MemberReservationRequest(date, timeId, themeId);
    }
}
