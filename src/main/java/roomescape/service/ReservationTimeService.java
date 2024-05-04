package roomescape.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationRepository;
import roomescape.dao.ReservationTimeRepository;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.AvailableReservationTimeResponse;
import roomescape.service.dto.ReservationTimeCreateRequest;
import roomescape.service.dto.ReservationTimeReadRequest;
import roomescape.service.dto.AllReservationTimeResponse;

import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public AllReservationTimeResponse create(final ReservationTimeCreateRequest reservationTimeCreateRequest) {
        validateDuplicated(reservationTimeCreateRequest);
        ReservationTime reservationTime = reservationTimeRepository.save(
                new ReservationTime(reservationTimeCreateRequest.startAt()));
        return new AllReservationTimeResponse(reservationTime);
    }

    private void validateDuplicated(ReservationTimeCreateRequest reservationTimeCreateRequest) {
        if (reservationTimeRepository.existsByTime(reservationTimeCreateRequest.startAt())) {
            throw new InvalidReservationException("이미 같은 시간이 존재합니다.");
        }
    }

    public List<AllReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll().stream()
                .map(AllReservationTimeResponse::new)
                .toList();
    }

    public void deleteById(final long id) {
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

    private boolean isBooked(final List<ReservationTime> bookedReservationTimes, final ReservationTime time) {
        return bookedReservationTimes.contains(time);
    }
}
