package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.exception.DuplicatedReservationException;
import roomescape.exception.EmptyNameException;
import roomescape.exception.ReservationByPastDateTimeException;
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
        ReservationRequestDTO reservationRequestDTO = new ReservationRequestDTO(
                "루드비코", LocalDate.now().plusDays(1), 1L, 1L
        );

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

    @DisplayName("지나간 날짜/시간에 대한 예약은 거부한다")
    @Test
    void 지나간_시점에_대한_예약_요청에는_ReservationByPastDateTimeException_예외를_던진다() {
        ReservationRequestDTO outdatedRequest = new ReservationRequestDTO(
                "sample",
                LocalDate.now().minusDays(1),
                1L,
                1L
        );

        assertThatThrownBy(() -> reservationService.addReservation(outdatedRequest))
                .isExactlyInstanceOf(ReservationByPastDateTimeException.class);
    }

    @DisplayName("비어 있는 이름에 대한 예약은 거부한다")
    @ParameterizedTest(name = "이름이 {0}이면 예외를 던진다")
    @NullAndEmptySource
    void 이름이_비어_있는_요청에는_EmptyNameException_예외를_던진다(String emptyName) {
        ReservationRequestDTO emptyNameRequest = new ReservationRequestDTO(
                emptyName,
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        assertThatThrownBy(() -> reservationService.addReservation(emptyNameRequest))
                .isExactlyInstanceOf(EmptyNameException.class);
    }

    @DisplayName("중복된 예약은 거부한다")
    @Test
    void 날짜와_시간_그리고_테마가_중복된_예약_요청에는_DuplicatedReservationException_예외를_던진다() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        long timeId = 1L;
        long themeId = 1L;
        ReservationRequestDTO reservationRequestDTO = new ReservationRequestDTO(
                "루드비코", date, timeId, themeId
        );
        reservationService.addReservation(reservationRequestDTO);

        // when and then
        ReservationRequestDTO duplicatedRequestDto = new ReservationRequestDTO("에코", date, timeId, themeId);
        assertThatThrownBy(() -> reservationService.addReservation(duplicatedRequestDto))
                .isExactlyInstanceOf(DuplicatedReservationException.class);
    }

    @DisplayName("모든 예약을 조회한다")
    @Test
    void 존재하는_모든_예약의_ReservationResponseDTO가_담긴_리스트를_리턴한다() {
        // given
        ReservationRequestDTO rudevicoReservationRequestDTO =
                new ReservationRequestDTO("루드비코", LocalDate.now().plusDays(1), 1L, 1L);
        ReservationRequestDTO echoReservationRequestDTO =
                new ReservationRequestDTO("에코", LocalDate.now().plusDays(1), 2L, 1L);

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
                new ReservationRequestDTO("루드비코", LocalDate.now().plusDays(1), 1L, 1L)
        );

        reservationService.deleteReservation(addedReservation.id());

        assertThat(reservationService.readAllReservation()).isEmpty();
    }
}
