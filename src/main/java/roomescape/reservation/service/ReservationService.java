package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.TimeResponse;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

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

    public ReservationResponse createReservation(ReservationRequest request) {
        ReservationTime time = reservationTimeRepository.findById(request.timeId())
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 time id입니다."));
        Theme theme = themeRepository.findById(request.themeId())
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 존재하지 않는 theme id입니다."));
        Reservation reservation = Reservation.of(request.name(), request.date(), time, theme);
        Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    public List<TimeResponse> getReservations(LocalDate date, Long themeId) {
        return reservationTimeRepository.findAvailableByDateAndThemeId(date, themeId).stream()
                .map(TimeResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}