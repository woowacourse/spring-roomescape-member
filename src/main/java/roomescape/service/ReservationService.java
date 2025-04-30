package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.request.CreateReservationServiceRequest;
import roomescape.service.dto.response.ReservationServiceResponse;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;

    public ReservationServiceResponse create(CreateReservationServiceRequest command) {
        ReservationTime reservationTime = getReservationTimeById(command.timeId());
        LocalDateTime requestedDateTime = LocalDateTime.of(command.date(), reservationTime.getStartAt());
        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 지나간 시간으로 예약할 수 없습니다.");
        }
        if (reservationRepository.existDuplicatedDateTime(command.date(), command.timeId())) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
        ReservationTheme reservationTheme = reservationThemeRepository.getById(command.themeId());
        Reservation reservation = command.toReservation(reservationTime, reservationTheme);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationServiceResponse.from(savedReservation);
    }

    public List<ReservationServiceResponse> getAll() {
        List<Reservation> reservations = reservationRepository.getAll();
        return reservations.stream()
                .map(ReservationServiceResponse::from)
                .toList();
    }

    public void delete(Long id) {
        Reservation target = getById(id);
        reservationRepository.remove(target);
    }

    private ReservationTime getReservationTimeById(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 시간이 존재하지 않습니다."));
    }

    private Reservation getById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("id에 해당하는 예약이 존재하지 않습니다."));
    }
}
