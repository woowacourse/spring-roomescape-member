package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repostiory.ReservationRepository;
import roomescape.reservation.domain.repostiory.ReservationTimeRepository;
import roomescape.reservation.domain.repostiory.ThemeRepository;
import roomescape.exception.InvalidReservationException;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.ReservationRequest;
import roomescape.reservation.service.dto.ReservationResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    private ReservationTime reservationTime;
    private Theme theme;

    @BeforeEach
    void setUp() {
        reservationTime = reservationTimeRepository.save(new ReservationTime("10:00"));
        theme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

    }

    @DisplayName("새로운 예약을 저장한다.")
    @Test
    void create() {
        //given
        String name = "lini";
        String date = "2024-10-04";
        ReservationRequest reservationRequest = new ReservationRequest(name, date, reservationTime.getId(), theme.getId());

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
        Reservation reservation = new Reservation("lini", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE), reservationTime, theme);
        reservationRepository.save(reservation);

        //when
        List<ReservationResponse> reservations = reservationService.findAll();

        //then
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void deleteById() {
        //given
        Reservation reservation = new Reservation("lini", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE), reservationTime, theme);
        Reservation target = reservationRepository.save(reservation);

        //when
        reservationService.deleteById(target.getId());

        //then
        assertThat(reservationService.findAll()).hasSize(0);
    }

    @DisplayName("해당 테마와 일정으로 예약이 존재하면 예외를 발생시킨다.")
    @Test
    void duplicatedReservation() {
        //given
        String date = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE);
        Reservation reservation = new Reservation("lini", date, reservationTime, theme);
        reservationRepository.save(reservation);

        ReservationRequest reservationRequest = new ReservationRequest("lini", date, reservationTime.getId(), theme.getId());

        //when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("선택하신 테마와 일정은 이미 예약이 존재합니다.");
    }

    @DisplayName("존재하지 않는 시간으로 예약을 추가하면 예외를 발생시킨다.")
    @Test
    void cannotCreateByUnknownTime() {
        //given
        String name = "lini";
        String date = "2024-10-04";
        ReservationRequest reservationRequest = new ReservationRequest(name, date, 0, theme.getId());

        //when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("더이상 존재하지 않는 시간입니다.");
    }

    @DisplayName("존재하지 않는 테마로 예약을 추가하면 예외를 발생시킨다.")
    @Test
    void cannotCreateByUnknownTheme() {
        //given
        String name = "lini";
        String date = "2024-10-04";
        ReservationRequest reservationRequest = new ReservationRequest(name, date, reservationTime.getId(), 0);

        //when & then
        assertThatThrownBy(() -> reservationService.create(reservationRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("더이상 존재하지 않는 테마입니다.");
    }
}
