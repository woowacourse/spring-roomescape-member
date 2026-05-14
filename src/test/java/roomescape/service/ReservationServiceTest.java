package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
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
import roomescape.dto.ReservationUpdateDtoDateAndTimeIdOnly;
import roomescape.exception.CannotDeleteReservationException;
import roomescape.exception.DuplicatedReservationException;
import roomescape.exception.EmptyNameException;
import roomescape.exception.ReservationByPastDateTimeException;
import roomescape.exception.ReservationDoesNotExistsException;
import roomescape.exception.ReservationTimeDoesNotExistsException;
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

        reservationService.deleteReservationById(addedReservation.id());

        assertThat(reservationService.readAllReservation()).isEmpty();
    }

    @DisplayName("사용자가 본인의 예약을 취소한다")
    @Test
    void 사용자_이름과_날짜와_시간과_테마가_일치하는_예약을_취소한다() {
        // given
        ReservationRequestDTO reservationRequestDTO = new ReservationRequestDTO(
                "루드비코", LocalDate.now().plusDays(1), 1L, 1L
        );

        ReservationResponseDTO addedReservation = reservationService.addReservation(reservationRequestDTO);

        // when
        reservationService.deleteReservationByUsernameAndDateAndTimeIdAndThemeId(reservationRequestDTO);

        // then
        assertThatThrownBy(() -> reservationService.findById(addedReservation.id()))
                .isExactlyInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("과거 시점의 예약은 취소할 수 없다")
    @Sql("/data.sql")
    @Test
    void 과거_시점의_예약을_취소하면_CannotDeleteReservationException을_던진다() {
        // when and then
        assertThatThrownBy(() -> reservationService.deleteReservationByUsernameAndDateAndTimeIdAndThemeId(
                new ReservationRequestDTO(
                        "루드비코",
                        LocalDate.now().minusDays(7),
                        1L,
                        1L
                )
        )).isExactlyInstanceOf(CannotDeleteReservationException.class)
                .hasMessageContaining("과거");
    }

    @DisplayName("존재하지 않는 예약은 취소할 수 없다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 존재하지_않는_예약을_취소하면_ReservationDoesNotExistsException을_던진다() {
        // when and then
        assertThatThrownBy(() -> reservationService.deleteReservationByUsernameAndDateAndTimeIdAndThemeId(
                new ReservationRequestDTO(
                        "루드비코",
                        LocalDate.now().plusDays(7),
                        1L,
                        1L
                )
        )).isExactlyInstanceOf(ReservationDoesNotExistsException.class);
    }

    @DisplayName("사용자 이름으로 예약을 조회한다")
    @Test
    void 사용자_이름이_일치하는_모든_예약의_ReservationResponseDTO가_담긴_리스트를_리턴한다() {
        // given
        ReservationRequestDTO rudevicoReservationRequestDTO =
                new ReservationRequestDTO("루드비코", LocalDate.now().plusDays(1), 1L, 1L);
        ReservationRequestDTO echoReservationRequestDTO =
                new ReservationRequestDTO("에코", LocalDate.now().plusDays(1), 2L, 1L);

        ReservationResponseDTO rudevicoReservation = reservationService.addReservation(rudevicoReservationRequestDTO);
        ReservationResponseDTO echoReservation = reservationService.addReservation(echoReservationRequestDTO);

        // when and then
        assertThat(reservationService.findAllByUsername("루드비코"))
                .hasSize(1)
                .containsExactlyInAnyOrder(rudevicoReservation);
    }

    @DisplayName("사용자가 본인의 예약의 날짜나 시간을 변경한다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 얘약_id가_일치하는_예약의_날짜나_시간을_변경하고_ReservationResponseDTO를_리턴한다() {
        // given
        ReservationResponseDTO added = reservationService.addReservation(new ReservationRequestDTO(
                "루드비코", LocalDate.now().plusDays(1), 1L, 1L
        ));

        // when
        LocalDate dateForUpdate = added.date().plusDays(1);
        reservationService.update(
                added.id(),
                new ReservationUpdateDtoDateAndTimeIdOnly(
                        dateForUpdate,
                        added.time().id()
                )
        );

        // then
        ReservationResponseDTO foundAfterUpdate = reservationService.findById(added.id());

        assertThat(foundAfterUpdate)
                .usingRecursiveComparison()
                .ignoringFields("date")
                .isEqualTo(added);

        assertThat(foundAfterUpdate.date()).isEqualTo(dateForUpdate);
    }

    @DisplayName("과거 시점으로 변경할 수는 없다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 과거_시점으로_변경하면_ReservationByPastDateTimeException을_던진다() {
        // given
        ReservationResponseDTO added = reservationService.addReservation(new ReservationRequestDTO(
                "루드비코", LocalDate.now().plusDays(1), 1L, 1L
        ));

        // when and then
        LocalDate dateForUpdate = added.date().minusDays(1);
        assertThatThrownBy(() -> reservationService.update(
                added.id(),
                new ReservationUpdateDtoDateAndTimeIdOnly(
                        dateForUpdate,
                        added.time().id()
                )
        )).isExactlyInstanceOf(ReservationByPastDateTimeException.class);
    }

    @DisplayName("과거 시점의 예약을 변경할 수 없다")
    @Sql("/data.sql")
    @Test
    void 과거_시점의_예약을_변경하면_ReservationByPastDateTimeException을_던진다() {
        assertThatThrownBy(() -> reservationService.update(
                1L,
                new ReservationUpdateDtoDateAndTimeIdOnly(
                        LocalDate.now().plusDays(1),
                        1L
                )
        )).isExactlyInstanceOf(ReservationByPastDateTimeException.class);
    }

    @DisplayName("존재하지 않는 예약 시간으로 변경할 수는 없다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 존재하지_않는_예약_시간으로_변경하면_ReservationTimeDoesNotExistsException을_던진다() {
        // given
        ReservationResponseDTO added = reservationService.addReservation(new ReservationRequestDTO(
                "루드비코", LocalDate.now().plusDays(1), 1L, 1L
        ));

        // when and then
        LocalDate dateForUpdate = added.date().plusDays(1);
        assertThatThrownBy(() -> reservationService.update(
                added.id(),
                new ReservationUpdateDtoDateAndTimeIdOnly(
                        dateForUpdate,
                        Long.MAX_VALUE
                )
        )).isExactlyInstanceOf(ReservationTimeDoesNotExistsException.class);
    }

    @DisplayName("존재하지 않는 예약을 변경할 수는 없다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 존재하지_않는_예약을_변경하면_ReservationDoesNotExistsException을_던진다() {
        // given
        ReservationResponseDTO added = reservationService.addReservation(new ReservationRequestDTO(
                "루드비코", LocalDate.now().plusDays(1), 1L, 1L
        ));

        // when and then
        LocalDate dateForUpdate = added.date().plusDays(1);
        assertThatThrownBy(() -> reservationService.update(
                Long.MAX_VALUE,
                new ReservationUpdateDtoDateAndTimeIdOnly(
                        dateForUpdate,
                        added.time().id()
                )
        )).isExactlyInstanceOf(ReservationDoesNotExistsException.class);
    }

    @DisplayName("이미 다른 예약이 존재하는 시점으로 변경할 수는 없다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 이미_다른_예약이_존재하는_시점으로_예약을_변경하면_DuplicatedReservationException을_던진다() {
        // given
        ReservationResponseDTO added = reservationService.addReservation(new ReservationRequestDTO(
                "루드비코", LocalDate.now().plusDays(1), 1L, 1L
        ));

        LocalDate dateForUpdate = added.date().plusDays(2);
        ReservationResponseDTO alreadyExistence = reservationService.addReservation(new ReservationRequestDTO(
                "루드비코", dateForUpdate, 1L, 1L
        ));

        // when and then
        assertThatThrownBy(() -> reservationService.update(
                added.id(),
                new ReservationUpdateDtoDateAndTimeIdOnly(
                        dateForUpdate,
                        added.time().id()
                )
        )).isExactlyInstanceOf(DuplicatedReservationException.class);
    }
}
