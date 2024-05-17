package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.fixture.Fixture;
import roomescape.member.repositoy.JdbcMemberRepository;
import roomescape.reservation.dto.request.CreateReservationUserRequest;
import roomescape.reservation.dto.response.FindAvailableTimesResponse;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.theme.repository.JdbcThemeRepository;

@ActiveProfiles("test")
@SpringBootTest
@Sql(scripts = "/delete-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private JdbcThemeRepository themeRepository;
    @Autowired
    private JdbcReservationTimeRepository reservationTimeRepository;
    @Autowired
    private JdbcMemberRepository memberRepository;

    @Test
    @DisplayName("예약을 생성한다.")
    void createReservation() {
        // given
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        memberRepository.save(Fixture.MEMBER_1);
        themeRepository.save(Fixture.THEME_1);
        var request = RequestFixture.REQUEST_1;

        // when
        var response = reservationService.createReservation(1L, request.date(), request.themeId(), request.timeId());

        // then
        Assertions.assertAll(
                () -> assertThat(response.id()).isEqualTo(1L),
                () -> assertThat(response.member()).isEqualTo(Fixture.MEMBER_1),
                () -> assertThat(response.date()).isEqualTo(RequestFixture.REQUEST_1.date().toString()),
                () -> assertThat(response.time().id()).isEqualTo(RequestFixture.REQUEST_1.timeId()),
                () -> assertThat(response.theme().id()).isEqualTo(RequestFixture.REQUEST_1.themeId())
        );
    }

    @Test
    @DisplayName("예약 생성 시 해당하는 예약 시간 id 가 없는 경우 예외가 발생한다.")
    void createReservation_ifReservationTimeNotExist_throwException() {
        // given
        themeRepository.save(Fixture.THEME_1);
        memberRepository.save(Fixture.MEMBER_1);
        var request = RequestFixture.REQUEST_1;

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(1L, request.date(), request.themeId(), request.timeId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("예약 생성 시 해당하는 테마 id가 없는 경우 예외가 발생한다.")
    void createReservation_ifThemeNotExist_throwException() {
        // given
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        memberRepository.save(Fixture.MEMBER_1);
        var request = RequestFixture.REQUEST_1;

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(
                1L, request.date(), request.themeId(), request.timeId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 테마가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("예약 생성 시 동일한 테마, 날짜, 시간이 겹치는 경우 예외가 발생한다.")
    void createReservation_ifExistSameDateAndTime_throwException() {
        // given
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        memberRepository.save(Fixture.MEMBER_1);
        var request = RequestFixture.REQUEST_1;
        reservationService.createReservation(1L, request.date(), request.themeId(), request.timeId());

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(
                1L, request.date(), request.themeId(), request.timeId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("동일한 시간의 예약이 존재합니다.");
    }

    @Test
    @DisplayName("예약 생성 시 지나간 날짜와 시간에 대한 예약인 경우 예외가 발생한다.")
    void createReservation_validateReservationDateTime_throwException() {
        // given
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        memberRepository.save(Fixture.MEMBER_1);
        var request = new CreateReservationUserRequest(
                LocalDate.of(2021, 1, 1),
                1L, 1L
        );

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(1L, request.date(), request.themeId(), request.timeId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
    }

    @Test
    @DisplayName("예약 목록 조회 시 저장된 예약과 예약 시간에 대한 정보를 반환한다.")
    void getReservations() {
        // given
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        memberRepository.save(Fixture.MEMBER_1);
        var request1 = RequestFixture.REQUEST_1;
        var request2 = new CreateReservationUserRequest(
                Fixture.RESERVATION_2.getDate(),
                1L, 1L
        );
        reservationService.createReservation(1L, request1.date(), request1.themeId(), request1.timeId());
        reservationService.createReservation(
                1L, request2.date(), request2.themeId(), request2.timeId()
        );

        // when
        List<FindReservationResponse> reservations = reservationService.getReservations();

        // then
        assertThat(reservations)
                .containsExactly(
                        FindReservationResponse.of(Fixture.RESERVATION_1),
                        FindReservationResponse.of(new Reservation(
                                2L,
                                Fixture.MEMBER_1,
                                Fixture.RESERVATION_2.getDate(),
                                Fixture.RESERVATION_TIME_1, Fixture.THEME_1)));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 대한 정보를 반환한다.")
    void getReservation() {
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        memberRepository.save(Fixture.MEMBER_1);
        var request1 = RequestFixture.REQUEST_1;
        reservationService.createReservation(1L, request1.date(), request1.themeId(), request1.timeId());

        // when & then
        assertThat(reservationService.getReservation(1L)).isEqualTo(FindReservationResponse.of(Fixture.RESERVATION_1));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약이 없는 경우 예외가 발생한다.")
    void getReservation_ifNotExist_throwException() {
        // when & then
        assertThatThrownBy(() -> reservationService.getReservation(99999L)).isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약이 존재하지 않습니다.");
    }


    @Test
    @DisplayName("해당하는 날짜와 테마를 통해 가능한 예약 시간 정보를 반환한다.")
    void getAvailableTimes() {
        // given
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_2);
        themeRepository.save(Fixture.THEME_1);
        memberRepository.save(Fixture.MEMBER_1);
        var request1 = RequestFixture.REQUEST_1;
        reservationService.createReservation(1L, request1.date(), request1.themeId(), request1.timeId());
        LocalDate date = Fixture.RESERVATION_1.getDate();

        // when & then
        assertThat(reservationService.getAvailableTimes(date, 1L)).isEqualTo(
                List.of(FindAvailableTimesResponse.of(1L, Fixture.RESERVATION_TIME_1.getTime(), true),
                        FindAvailableTimesResponse.of(2L, Fixture.RESERVATION_TIME_2.getTime(), false)));
    }

    @Test
    @DisplayName("해당하는 id의 예약을 삭제한다.")
    void deleteReservation() {
        // given
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        memberRepository.save(Fixture.MEMBER_1);
        var request = RequestFixture.REQUEST_1;
        reservationService.createReservation(1L, request.date(), request.themeId(), request.timeId());

        // when
        reservationService.deleteReservation(1L);

        // then
        assertThatThrownBy(() -> reservationService.getReservation(1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    static class RequestFixture {

        static CreateReservationUserRequest REQUEST_1 = new CreateReservationUserRequest(
                Fixture.RESERVATION_1.getDate(), 1L, 1L);
    }
}
