package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.repository.JdbcReservationRepository;
import roomescape.repository.JdbcReservationTimeRepository;
import roomescape.repository.JdbcThemeRepository;

@JdbcTest
@Import({JdbcReservationRepository.class, JdbcReservationTimeRepository.class, JdbcThemeRepository.class,
        ReservationService.class})
@Sql(value = "/initialize_theme_and_time.sql")
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @DisplayName("예약을 생성한다")
    @Test
    void ReservationRequestDTO를_받아_ReservationResponseDTO를_리턴한다() {
        ReservationRequestDTO reservationRequestDTO = new ReservationRequestDTO("루드비코", LocalDate.now(), 1L, 1L);

        ReservationResponseDTO addedReservation = reservationService.addReservation(reservationRequestDTO);

        assertThat(addedReservation)
                .extracting(responseDTO -> new ReservationRequestDTO(
                        responseDTO.name(),
                        responseDTO.date(),
                        responseDTO.time().id(),
                        responseDTO.theme().id()
                ))
                .isEqualTo(reservationRequestDTO);
    }

    @DisplayName("모든 예약을 조회한다")
    @Test
    void 존재하는_모든_예약의_ReservationResponseDTO가_담긴_리스트를_리턴한다() {
        // given
        ReservationRequestDTO rudevicoReservationRequestDTO =
                new ReservationRequestDTO("루드비코", LocalDate.now(), 1L, 1L);
        ReservationRequestDTO echoReservationRequestDTO =
                new ReservationRequestDTO("에코", LocalDate.now(), 2L, 1L);

        ReservationResponseDTO rudevicoReservation = reservationService.addReservation(rudevicoReservationRequestDTO);
        ReservationResponseDTO echoReservation = reservationService.addReservation(echoReservationRequestDTO);

        // when
        List<ReservationResponseDTO> allReservations = reservationService.readAllReservation();

        // then
        assertThat(allReservations)
                .hasSize(2)
                .containsExactlyInAnyOrder(rudevicoReservation, echoReservation);
    }

    @DisplayName("예약을 삭제한다")
    @Test
    void 예약의_id로_예약을_삭제한다() {
        ReservationResponseDTO addedReservation = reservationService.addReservation(
                new ReservationRequestDTO("루드비코", LocalDate.now(), 1L, 1L)
        );

        reservationService.deleteReservation(addedReservation.id());

        assertThat(reservationService.readAllReservation()).isEmpty();
    }
}
