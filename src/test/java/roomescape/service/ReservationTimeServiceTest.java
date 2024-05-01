package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.dao.ReservationTimeRepository;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationResponse;
import roomescape.service.dto.ReservationTimeRequest;
import roomescape.service.dto.ReservationTimeResponse;
import roomescape.service.dto.ThemeRequest;
import roomescape.service.dto.ThemeResponse;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeService themeService;
    @Autowired
    private ReservationTimeService reservationTimeService;
    @Autowired
    private ReservationService reservationService;

    @AfterEach
    void init() {
        for (final ReservationResponse reservationResponse : reservationService.findAll()) {
            reservationService.deleteById(reservationResponse.id());
        }
        for (final ReservationTime reservationTime : reservationTimeRepository.findAll()) {
            reservationTimeRepository.deleteById(reservationTime.getId());
        }
        for (ThemeResponse themeResponse : themeService.findAll()) {
            themeService.deleteById(themeResponse.id());
        }
    }

    @DisplayName("새로운 예약 시간을 저장한다.")
    @Test
    void create() {
        //given
        String startAt = "10:00";
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(startAt);

        //when
        ReservationTimeResponse result = reservationTimeService.create(reservationTimeRequest);

        //then
        assertAll(
                () -> assertThat(result.id()).isNotZero(),
                () -> assertThat(result.startAt()).isEqualTo(startAt)
        );
    }

    @DisplayName("모든 예약 시간 내역을 조회한다.")
    @Test
    void findAll() {
        //given
        String startAt = "10:00";
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(startAt);
        reservationTimeService.create(reservationTimeRequest);

        //when
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.findAll();

        //then
        assertThat(reservationTimes).hasSize(1);
    }

    @DisplayName("시간이 이미 존재하면 예외를 발생시킨다.")
    @Test
    void duplicatedTime() {
        //given
        String startAt = "10:00";
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(startAt);
        reservationTimeService.create(reservationTimeRequest);

        //when&then
        assertThatThrownBy(() -> reservationTimeService.create(reservationTimeRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("이미 같은 시간이 존재합니다.");
    }

    @DisplayName("예약이 존재하는 시간으로 삭제를 시도하면 예외를 발생시킨다.")
    @Test
    void cannotDeleteTime() {
        //given
        String startAt = "10:00";
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(startAt);
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(reservationTimeRequest);
        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ThemeResponse themeResponse = themeService.create(themeRequest);
        reservationService.create(
                new ReservationRequest("lilly", "2222-10-04", reservationTimeResponse.id(), themeResponse.id()));

        //when&then
        assertThatThrownBy(() -> reservationTimeService.deleteById(reservationTimeResponse.id()))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("해당 시간에 예약이 존재해서 삭제할 수 없습니다.");
    }
}
