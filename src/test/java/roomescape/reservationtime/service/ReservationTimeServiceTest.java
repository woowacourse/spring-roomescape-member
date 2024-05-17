package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.fixture.Fixture;
import roomescape.member.repositoy.JdbcMemberRepository;
import roomescape.reservation.repository.JdbcReservationRepository;
import roomescape.reservationtime.dto.request.CreateReservationTimeRequest;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.dto.response.FindReservationTimeResponse;
import roomescape.reservationtime.repository.JdbcReservationTimeRepository;
import roomescape.theme.repository.JdbcThemeRepository;

@ActiveProfiles("test")
@SpringBootTest
@Sql("/delete-data.sql")
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private JdbcReservationTimeRepository reservationTimeRepository;
    @Autowired
    private JdbcThemeRepository themeRepository;
    @Autowired
    private JdbcReservationRepository reservationRepository;
    @Autowired
    private JdbcMemberRepository memberRepository;

    @Test
    @DisplayName("예약 시간 생성한다.")
    void createReservationTime() {
        // given
        var request = new CreateReservationTimeRequest(Fixture.RESERVATION_TIME_1.getTime());

        // when
        CreateReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(request);

        // then
        assertThat(reservationTime.id()).isEqualTo(1L);
        assertThat(reservationTime.startAt()).isEqualTo(Fixture.RESERVATION_TIME_1.getTime().toString());
    }

    @Test
    @DisplayName("예약 시간 목록 조회 시 저장된 예약 시간에 대한 정보를 반환한다.")
    void getReservationTimes() {
        // given
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_2);

        // when & then
        assertThat(reservationTimeService.getReservationTimes())
                .containsExactly(
                        FindReservationTimeResponse.of(Fixture.RESERVATION_TIME_1),
                        FindReservationTimeResponse.of(Fixture.RESERVATION_TIME_2));
    }

    @Test
    @DisplayName("해당하는 id의 예약 시간을 반환한다.")
    void getReservationTime() {
        // given
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);

        // when & then
        assertThat(reservationTimeService.getReservationTime(1L))
                .isEqualTo(FindReservationTimeResponse.of(Fixture.RESERVATION_TIME_1));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간을 삭제한다.")
    void deleteById() {
        // given
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);

        // when
        reservationTimeService.deleteById(1L);

        // then
        assertThatThrownBy(() -> reservationTimeService.getReservationTime(1L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("삭제할 시간을 사용 중인 예약이 존재할 경우 예외가 발생한다.")
    void deleteById_ifAlreadyUsed_throwException() {
        // given
        memberRepository.save(Fixture.MEMBER_1);
        reservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        themeRepository.save(Fixture.THEME_1);
        reservationRepository.save(Fixture.RESERVATION_1);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("시간을 사용 중인 예약이 존재합니다.");
    }
}
