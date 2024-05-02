package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.request.ReservationRequest;
import roomescape.controller.response.ReservationResponse;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        ReservationTime requestedReservationTime = reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 시간입니다. timeId: " + reservationRequest.timeId()));
        Theme requestedTheme = themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("예약할 수 없는 테마입니다. themeId: " + reservationRequest.themeId()));
        Reservation requestedReservation = reservationRequest.toEntity(requestedReservationTime, requestedTheme);

        rejectPastTimeReservation(requestedReservation);
        rejectDuplicateReservation(requestedReservation);

        Reservation savedReservation = reservationRepository.save(requestedReservation);
        return ReservationResponse.from(savedReservation);
    }

    private void rejectPastTimeReservation(Reservation reservation) {
        LocalDateTime reservationDataTime = reservation.getDateTime();
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (reservationDataTime.isBefore(currentDateTime)) {
            throw new IllegalArgumentException("이미 지난 시간입니다. 입력한 시간: " + reservationDataTime.toLocalDate() + " "
                                               + reservationDataTime.toLocalTime());
        }
    }

    private void rejectDuplicateReservation(Reservation reservation) {
        List<Reservation> savedReservations = reservationRepository.findAll();
        boolean isDuplicateReservationPresent = savedReservations.stream()
                .filter(reservation::hasSameTheme)
                .anyMatch(reservation::hasSameDateTime);

        if (isDuplicateReservationPresent) {
            throw new IllegalArgumentException("중복된 예약이 존재합니다.");
        }
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}
