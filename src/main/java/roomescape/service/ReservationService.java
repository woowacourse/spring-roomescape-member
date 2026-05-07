package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequestDTO;
import roomescape.dto.ReservationResponseDTO;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
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

    public List<Theme> getPopularThemes() {
        List<Long> popularThemeIds = reservationRepository.findPopularThemeIds();

        return popularThemeIds.stream()
                .map(id -> themeRepository.findById(id).orElseThrow())
                .toList();
    }

    public ReservationResponseDTO addReservation(ReservationRequestDTO reservationRequestDTO) {
        ReservationTime time = reservationTimeRepository.findById(reservationRequestDTO.timeId())
                .orElseThrow();
        Theme theme = themeRepository.findById(reservationRequestDTO.themeId())
                .orElseThrow();

        Reservation reservation = new Reservation(
                reservationRequestDTO.name(),
                reservationRequestDTO.date(),
                time,
                theme
        );

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponseDTO.from(savedReservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }
}
