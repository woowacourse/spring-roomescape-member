package roomescape.reservation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.exception.InvalidReservationException;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repostiory.ReservationRepository;
import roomescape.reservation.domain.repostiory.ReservationTimeRepository;
import roomescape.reservation.service.dto.AvailableReservationTimeResponse;
import roomescape.reservation.service.dto.ReservationTimeCreateRequest;
import roomescape.reservation.service.dto.ReservationTimeReadRequest;
import roomescape.reservation.service.dto.ReservationTimeResponse;

import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse create(ReservationTimeCreateRequest reservationTimeCreateRequest) {
        validateDuplicated(reservationTimeCreateRequest);
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(reservationTimeCreateRequest.startAt()));
        return new ReservationTimeResponse(reservationTime);
    }

    private void validateDuplicated(ReservationTimeCreateRequest reservationTimeCreateRequest) {
        if (reservationTimeRepository.existsByTime(reservationTimeCreateRequest.startAt())) {
            throw new InvalidReservationException("이미 같은 시간이 존재합니다.");
        }
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public void deleteById(long id) {
        validateByReservation(id);
        reservationTimeRepository.deleteById(id);
    }

    private void validateByReservation(long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new InvalidReservationException("해당 시간에 예약이 존재해서 삭제할 수 없습니다.");
        }
    }

    public List<AvailableReservationTimeResponse> findAvailableTimes(ReservationTimeReadRequest reservationTimeReadRequest) {
        List<ReservationTime> bookedReservationTimes = reservationTimeRepository.findBookedTimesByDateAndTheme(reservationTimeReadRequest.date(),
                reservationTimeReadRequest.themeId());
        return reservationTimeRepository.findAll().stream()
                .map(time -> new AvailableReservationTimeResponse(time.getId(), time.getStartAt(), isBooked(bookedReservationTimes, time)))
                .toList();
    }

    private boolean isBooked(List<ReservationTime> bookedReservationTimes, ReservationTime time) {
        return bookedReservationTimes.contains(time);
    }
}
