package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationResponse> findAllReservationResponses() {
        List<Reservation> allReservations = reservationRepository.findAll();

        return allReservations.stream()
                .map(reservation -> ReservationResponse.from(reservation, reservation.getTime(), reservation.getTheme()))
                .toList();
    }

    public List<ReservationResponse> searchReservations(Long themeId, Long memberId, LocalDate from, LocalDate to) {
        List<Reservation> searchResults = reservationRepository.findByThemeMemberDateRange(themeId, memberId, from, to);

        return searchResults.stream()
                .map(reservation -> ReservationResponse.from(reservation, reservation.getTime(), reservation.getTheme()))
                .toList();
    }
}
