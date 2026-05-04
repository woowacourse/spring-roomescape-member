package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.CreateReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.ReservationTimeResponse;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse create(CreateReservationRequest createReservationRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findById(createReservationRequest.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));

        validateDuplicateReservation(createReservationRequest);
        Long id = reservationRepository.save(
                Reservation.create(createReservationRequest.name(), createReservationRequest.date(),
                        reservationTime));
        return new ReservationResponse(id, createReservationRequest.name(), createReservationRequest.date(),
                ReservationTimeResponse.from(reservationTime));
    }

    private void validateDuplicateReservation(CreateReservationRequest createReservationRequest) {
        if (reservationRepository.existsByDateAndTimeId(createReservationRequest.date(),
                createReservationRequest.timeId())) {
            throw new ConflictException("이미 존재하는 예약 날짜/시간 입니다.");
        }
    }

    @Transactional
    public ReservationResponse delete(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
        reservationRepository.delete(id);
        return ReservationResponse.from(reservation);
    }
}
