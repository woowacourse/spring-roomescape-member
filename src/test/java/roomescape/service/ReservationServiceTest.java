package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.dao.ThemeRepository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReservationServiceTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationService reservationService;
    private ReservationTime reservationTime;
    private Theme theme;

    @BeforeEach
    void setUp() {
        reservationTime = reservationTimeRepository.save(new ReservationTime("10:00"));
        theme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

    }

    @AfterEach
    void init() {
        for (final Reservation reservation : reservationRepository.findAll()) {
            reservationRepository.deleteById(reservation.getId());
        }
        for (final ReservationTime reservationTime : reservationTimeRepository.findAll()) {
            reservationTimeRepository.deleteById(reservationTime.getId());
        }
        for (final Theme theme : themeRepository.findAll()) {
            themeRepository.deleteById(theme.getId());
        }
    }

    @DisplayName("새로운 예약을 저장한다.")
    @Test
    void create() {
        //given
        String name = "lini";
        String date = "2024-10-04";
        ReservationRequest reservationRequest = new ReservationRequest(name, date, reservationTime.getId(),
                theme.getId());

        //when
        ReservationResponse result = reservationService.create(reservationRequest);

        //then
        assertAll(
                () -> assertThat(result.id()).isNotZero(),
                () -> assertThat(result.time().id()).isEqualTo(reservationTime.getId()),
                () -> assertThat(result.theme().id()).isEqualTo(theme.getId())
        );
    }

    @DisplayName("모든 예약 내역을 조회한다.")
    @Test
    void findAll() {
        //given
        String name = "lini";
        String date = "2024-10-04";
        ReservationRequest reservationRequest = new ReservationRequest(name, date, reservationTime.getId(),
                theme.getId());
        reservationService.create(reservationRequest);

        //when
        List<ReservationResponse> reservations = reservationService.findAll();

        //then
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void deleteById() {
        //given
        String name = "lini";
        String date = "2024-10-04";
        ReservationRequest reservationRequest = new ReservationRequest(name, date, reservationTime.getId(),
                theme.getId());
        ReservationResponse target = reservationService.create(reservationRequest);

        //when
        reservationService.deleteById(target.id());

        //then
        assertThat(reservationService.findAll()).hasSize(0);
    }

    @DisplayName("해당 테마와 일정으로 예약이 존재하면 예외를 발생시킨다.")
    @Test
    void duplicatedReservation() {
        //given
        String name = "lini";
        String date = "2024-10-04";
        ReservationRequest reservationRequest = new ReservationRequest(name, date, reservationTime.getId(),
                theme.getId());
        reservationService.create(reservationRequest);

        //when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("선택하신 테마와 일정은 이미 예약이 존재합니다.");
    }
}
