package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.dto.ReservationUpdateDtoDateAndTimeIdOnly;
import roomescape.exception.CannotDeleteReservationException;
import roomescape.exception.DuplicatedReservationException;
import roomescape.exception.ReservationByPastDateTimeException;
import roomescape.exception.ReservationDoesNotExistsException;
import roomescape.exception.ReservationTimeDoesNotExistsException;
import roomescape.exception.ThemeDoesNotExistsException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponseDTO> readAllReservation() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponseDTO::from)
                .toList();
    }

    public List<ReservationResponseDTO> findAllByUsername(String username) {
        return reservationRepository.findAllByUsername(username)
                .stream()
                .map(ReservationResponseDTO::from)
                .toList();
    }

    public ReservationResponseDTO findById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow();
        return ReservationResponseDTO.from(reservation);
    }

    public ReservationResponseDTO addReservation(ReservationRequestDTO reservationRequestDTO) {
        ReservationTime time = reservationTimeRepository.findById(reservationRequestDTO.timeId())
                .orElseThrow();
        Theme theme = themeRepository.findById(reservationRequestDTO.themeId())
                .orElseThrow();

        validateNotPast(LocalDateTime.of(reservationRequestDTO.date(), time.getStartAt()));
        validateNotDuplicated(reservationRequestDTO.date(), time, theme);

        Reservation reservation = Reservation.withoutId(
                reservationRequestDTO.name(),
                reservationRequestDTO.date(),
                time,
                theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.from(savedReservation);
    }

    public int update(Long reservationId, ReservationUpdateDtoDateAndTimeIdOnly updateDto) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(ReservationDoesNotExistsException::new);
        Theme theme = themeRepository.findById(reservation.getThemeId())
                .orElseThrow(ThemeDoesNotExistsException::new);
        ReservationTime reservationTimeForUpdate = reservationTimeRepository.findById(updateDto.timeId())
                .orElseThrow(ReservationTimeDoesNotExistsException::new);

        validateNotPast(LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt()));
        validateNotPast(LocalDateTime.of(updateDto.date(), reservationTimeForUpdate.getStartAt()));
        validateNotDuplicated(updateDto.date(), reservationTimeForUpdate, theme);

        reservation.changeDateAndTime(updateDto.date(), reservationTimeForUpdate);

        return reservationRepository.update(reservation);
    }

    // TODO: 도메인 맞게 cancel로 바꾸고, 예약도 booking 고려
    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }

    public int deleteReservationByUsernameAndDateAndTimeIdAndThemeId(ReservationRequestDTO requestDTO) {
        if (requestDTO.date().isBefore(LocalDate.now())) {
            throw new CannotDeleteReservationException("과거 시점의 예약입니다.");
        }

        int deletedRows = reservationRepository.deleteByNameAndDateAndTimeIdAndThemeId(
                requestDTO.name(),
                requestDTO.date(),
                requestDTO.timeId(),
                requestDTO.themeId()
        );
        if (deletedRows == 0) {
            throw new ReservationDoesNotExistsException();
        }

        return deletedRows;
    }

    private static void validateNotPast(LocalDateTime targetDateTime) {
        LocalDateTime now = LocalDateTime.now();
        if (targetDateTime.isBefore(now)) {
            throw new ReservationByPastDateTimeException(now, targetDateTime);
        }
    }

    private void validateNotDuplicated(LocalDate date, ReservationTime time, Theme theme) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                date,
                time.getId(),
                theme.getId()
        )) {
            throw new DuplicatedReservationException(date, time.getStartAt(), theme.getName());
        }
    }
}
