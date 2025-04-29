package roomescape.application;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.presentation.dto.request.ReservationRequest;
import roomescape.presentation.dto.response.ReservationResponse;
import roomescape.application.mapper.ReservationMapper;
import roomescape.domain.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeService timeService;

    public ReservationService(ReservationRepository reservationRepository, TimeService timeService) {
        this.reservationRepository = reservationRepository;
        this.timeService = timeService;
    }

    public ReservationResponse registerReservation(ReservationRequest request) {
        ReservationTime reservationTime = timeService.getTimeById(request.timeId());
        validateNotPast(request.date(), reservationTime.getStartAt());
        Reservation reservation = ReservationMapper.toDomain(request, reservationTime);
        validteNotDuplicate(reservation);
        Long id = reservationRepository.save(reservation);

        return ReservationMapper.toDto(Reservation.assignId(id, reservation));
    }

    private void validteNotDuplicate(Reservation reservation) {
        List<Reservation> allReservations = reservationRepository.findAll();
        boolean duplicated = allReservations.stream()
                .anyMatch(r -> r.isDuplicated(reservation));
        if(duplicated) {
            throw new IllegalArgumentException("이미 예약된 일시입니다");
        }
    }

    private void validateNotPast(LocalDate date, LocalTime startAt) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = LocalDateTime.of(date, startAt);
        if (reservationDateTime.isBefore(now)) {
            throw new IllegalArgumentException("과거 일시로 예약할 수 없습니다.");
        }
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ReservationMapper.toDtos(reservations);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
