package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.*;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;
    
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(1L);
        themeRepository.save(new Theme("테마명", "테마 설명", "테마 이미지"));
        Theme theme = themeRepository.findByThemeId(1L);
        User user = userRepository.findById(1L);
        Reservation reservation1 = new Reservation(
                user,
                LocalDate.parse("2025-10-05"),
                reservationTime,
                theme
        );
        reservationRepository.save(reservation1);
    }

    @Test
    @DisplayName("날짜와 테마가 주어졌을 때 예약이 되어 있는 시간들을 확인한다.")
    void checkIsAlreadyBookedTime() {
        //given
        LocalDate date = LocalDate.parse("2025-10-05");
        Long themeId = 1L;

        //when
        List<AvailableTimeResponse> availableTimeResponses = reservationService.findByDateAndThemeId(date, themeId);
        AvailableTimeResponse availableTimeResponse = availableTimeResponses.get(0);

        //then
        assertAll(
                () -> assertThat(availableTimeResponse.alreadyBooked()).isTrue(),
                () -> assertThat(availableTimeResponse.timeId()).isEqualTo(1L),
                () -> assertThat(availableTimeResponse.startAt()).isEqualTo("10:00")
        );
    }

    @Test
    @DisplayName("예약 생성 기능을 확인한다.")
    void checkReservationCreate() {
        //given
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(
                LocalDate.parse("2025-04-10"),
                1L,
                1L
        );

        User user = new User("admin1@email.com", "password");

        //when
        ReservationResponse reservationResponse = reservationService.create(reservationCreateRequest, user);

        //then
        assertAll(
                () -> assertThat(reservationResponse.userResponse().name()).isEqualTo("유저"),
                () -> assertThat(reservationResponse.date()).isEqualTo("2025-04-10"),
                () -> assertThat(reservationResponse.time().id()).isEqualTo(1L),
                () -> assertThat(reservationResponse.theme().id()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("예약 삭제 기능을 확인한다.")
    void checkReservationDelete() {
        //given
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(
                LocalDate.parse("2025-04-10"),
                1L,
                1L
        );
        User user = new User("admin1@email.com", "password");
        ReservationResponse reservationResponse = reservationService.create(reservationCreateRequest, user);

        //when & then
        assertDoesNotThrow(() -> reservationService.delete(reservationResponse.id()));
    }

    @Test
    @DisplayName("예약 삭제 기능의 실패를 확인한다.")
    void checkReservationDeleteFail() {
        //given
        ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(
                LocalDate.parse("2025-04-10"),
                1L,
                1L
        );
        User user = new User("admin1@email.com", "password");
        ReservationResponse reservationResponse = reservationService.create(reservationCreateRequest, user);

        //when & then
        assertThatThrownBy(() -> reservationService.delete(0L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("삭제할 예약이 존재하지 않습니다");
    }
}
