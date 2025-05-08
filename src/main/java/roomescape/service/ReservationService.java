package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.dto.request.CreateReservationRequest;
import roomescape.dto.response.ReservationResponse;
import lombok.RequiredArgsConstructor;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;

    public ReservationResponse create(CreateReservationRequest request) {
        ReservationTime reservationTime = reservationTimeRepository.getById(request.timeId());
        LocalDateTime requestedDateTime = LocalDateTime.of(request.date(), reservationTime.startAt());
        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 지나간 시간으로 예약할 수 없습니다.");
        }
        if (reservationRepository.existDuplicatedDateTime(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
        ReservationTheme reservationTheme = reservationThemeRepository.getById(request.themeId());
        Reservation reservation = request.toReservation(reservationTime, reservationTheme);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public List<ReservationResponse> getAll() {
        List<Reservation> reservations = reservationRepository.getAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void delete(Long id) {
        reservationRepository.findById(id)
                .ifPresent(reservationRepository::remove);
    }
}
