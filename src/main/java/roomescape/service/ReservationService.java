package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationFactory;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.service.dto.request.ReservationRequest;
import roomescape.service.dto.response.ReservationResponse;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final ReservationFactory reservationFactory;

    public ReservationService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository,
            ThemeRepository themeRepository,
            ReservationFactory reservationFactory
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.reservationFactory = reservationFactory;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        ReservationTime requestedReservationTime = reservationTimeRepository.getById(reservationRequest.timeId());
        Theme requestedTheme = themeRepository.getById(reservationRequest.themeId());

        Reservation requestedReservation = reservationFactory.createReservation(reservationRequest.name(),
                reservationRequest.date(), requestedReservationTime, requestedTheme);

        rejectDuplicateReservation(requestedReservation);

        Reservation savedReservation = reservationRepository.save(requestedReservation);
        return ReservationResponse.from(savedReservation);
    }

    private void rejectDuplicateReservation(Reservation reservation) {
        List<Reservation> savedReservations = reservationRepository.findAll();
        boolean isDuplicateReservationPresent = savedReservations.stream()
                .filter(reservation::hasSameTheme)
                .anyMatch(reservation::hasSameDateTime);

        if (isDuplicateReservationPresent) {
            throw new IllegalStateException("중복된 예약이 존재합니다.");
        }
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
