package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.dto.ReservedTimeResponseDTO;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ReservationResponseDTO addReservation(ReservationRequestDTO reservationRequestDTO) {
        ReservationTime time = reservationTimeRepository.findById(reservationRequestDTO.timeId())
                .orElseThrow(
                        () -> new RuntimeException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(reservationRequestDTO.themeId())
                .orElseThrow(
                        () -> new RuntimeException("존재하지 않는 테마입니다."));

        Reservation reservation = Reservation.create(reservationRequestDTO.name(),
                reservationRequestDTO.date(), time, theme);

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.from(savedReservation);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> readAllReservation() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservationResponseDTO readReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("예약이 존재하지 않습니다."));
        return ReservationResponseDTO.from(reservation);
    }

    @Transactional(readOnly = true)
    public List<ReservedTimeResponseDTO> findReservedTimes(LocalDate targetDate,
            Long targetThemeId) {
        return reservationTimeRepository.findReservedTimes(targetDate, targetThemeId);
    }

    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }
}
