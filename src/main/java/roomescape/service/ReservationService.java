package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
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

    public ReservationService(ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponseDTO> readAllReservation() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponseDTO::from)
                .collect(Collectors.toList());
    }

    public List<ReservationTime> findReservedTimes(LocalDate targetDate, Long targetThemeId) {
        List<Long> reservedTimesOfTargetThemeOnTargetDate = reservationRepository.findAll()
                .stream()
                .filter(reservation -> reservation.getDate().equals(targetDate))
                .filter(reservation -> reservation.getTheme().getId().equals(targetThemeId))
                .map(reservation -> reservation.getTimeId())
                .toList();

        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .filter(reservationTime -> reservedTimesOfTargetThemeOnTargetDate.contains(reservationTime.getId()))
                .toList();
    }

    public List<Theme> getPopularThemes() {
        List<Long> popularThemeIds = reservationRepository.findPopularThemeIds();
        System.out.println("popularThemeIds = " + popularThemeIds);

        return popularThemeIds.stream()
                .map(id -> themeRepository.findById(id).get())
                .toList();
    }

    public ReservationResponseDTO addReservation(ReservationRequestDTO reservationRequestDTO) {
        ReservationTime time = reservationTimeRepository.findById(reservationRequestDTO.timeId())
                .orElseThrow(
                        () -> new RuntimeException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(reservationRequestDTO.themeId())
                .orElseThrow(
                        () -> new RuntimeException("존재하지 않는 테마입니다."));

        Reservation reservation = new Reservation(reservationRequestDTO.name(),
                reservationRequestDTO.date(), time, theme);

        Reservation savedReservation = reservationRepository.save(reservation);
        ReservationResponseDTO response = ReservationResponseDTO.from(savedReservation);
        return response;
    }

    public void deleteReservation(Long id) {
        reservationRepository.delete(id);
    }
}
