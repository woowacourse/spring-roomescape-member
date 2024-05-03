package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.ReservationTimeBookedRequest;
import roomescape.dto.ReservationTimeBookedResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeSaveRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository, final ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public ReservationTimeResponse saveTime(final ReservationTimeSaveRequest reservationTimeSaveRequest) {
        final ReservationTime reservationTime = reservationTimeSaveRequest.toReservationTime();

        validateUniqueReservationTime(reservationTime);

        final ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        return new ReservationTimeResponse(savedReservationTime);
    }

    private void validateUniqueReservationTime(final ReservationTime reservationTime) {
        final boolean isTimeExist = reservationTimeRepository.existByStartAt(reservationTime.getStartAt());

        if (isTimeExist) {
            throw new IllegalArgumentException("이미 저장된 예약 시간입니다.");
        }
    }

    public void deleteTime(final Long id) {
        validateDeleteTime(id);
        reservationTimeRepository.deleteById(id);
    }

    private void validateDeleteTime(final Long id) {
        reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다."));

        final boolean existByTimeId = reservationRepository.existByTimeId(id);
        if (existByTimeId) {
            throw new IllegalArgumentException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
    }

    public List<ReservationTimeBookedResponse> getTimesWithBooked(final ReservationTimeBookedRequest reservationTimeBookedRequest) {
        final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        final List<Reservation> reservations = reservationRepository.findByDateAndThemeId(reservationTimeBookedRequest.date(), reservationTimeBookedRequest.themeId());

        return reservationTimes.stream()
                .map(reservationTime -> createReservationTimeBooked(reservationTime, reservations))
                .toList();
    }

    private ReservationTimeBookedResponse createReservationTimeBooked(final ReservationTime reservationTime, final List<Reservation> reservations) {
        final Long reservationTimeId = reservationTime.getId();
        final boolean alreadyBooked = reservations.stream()
                .anyMatch(reservation -> reservation.getTimeId().equals(reservationTimeId));
        return new ReservationTimeBookedResponse(reservationTime, alreadyBooked);
    }
}
