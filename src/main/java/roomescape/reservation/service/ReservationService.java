package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.CreateReservationResponse;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(final ReservationRepository reservationRepository,
                              final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public CreateReservationResponse createReservation(final CreateReservationRequest createReservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findById(createReservationRequest.timeId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약 시간이 존재하지 않습니다."));

        validateReservationDateTime(createReservationRequest.date(), reservationTime.getTime());

        if (reservationRepository.existByReservation(createReservationRequest.toReservation(reservationTime))) {
            throw new IllegalStateException("동일한 시간의 예약이 존재합니다.");
        }

        Long id = reservationRepository.save(createReservationRequest.toReservation(reservationTime));
        return CreateReservationResponse.of(getReservation(id));
    }

    private void validateReservationDateTime(final LocalDate reservationDate, final LocalTime reservationTime) {
        LocalDateTime reservationDateTime = LocalDateTime.of(reservationDate, reservationTime);
        if (reservationDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    public List<FindReservationResponse> getReservations() {
        return reservationRepository.findAll().stream()
                .map(FindReservationResponse::of)
                .toList();
    }

    public FindReservationResponse getReservation(final Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약이 존재하지 않습니다."));
        return FindReservationResponse.of(reservation);
    }

    public void deleteReservation(final Long id) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 예약이 존재하지 않습니다."));
        reservationRepository.deleteById(id);
    }
}
