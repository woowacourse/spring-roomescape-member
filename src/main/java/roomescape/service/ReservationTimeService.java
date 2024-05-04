package roomescape.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.controller.request.ReservationTimeRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes();
    }

    public ReservationTime addReservationTime(ReservationTimeRequest request) {
        LocalTime startAt = request.getStartAt();
        Long countReservationTimeByStartAt = reservationTimeRepository.countReservationTimeByStartAt(startAt);
        if (countReservationTimeByStartAt == null || countReservationTimeByStartAt > 0) {
            throw new DuplicatedException("이미 존재하는 시간입니다.");
        }
        ReservationTime reservationTime = new ReservationTime(startAt);
        return reservationTimeRepository.addReservationTime(reservationTime);
    }

    public ReservationTime findReservationTime(long id) {
        return reservationTimeRepository.findReservationById(id);
    }

    public void deleteReservationTime(long id) {
        Long countedReservationTime = reservationTimeRepository.countReservationTimeById(id);
        if (countedReservationTime == null || countedReservationTime <= 0) {
            throw new NotFoundException("id(%s)에 해당하는 예약 시간이 존재하지 않습니다.".formatted(id));
        }
        Long countedReservationByTime = reservationRepository.countReservationByTimeId(id);
        if (countedReservationByTime == null || countedReservationByTime > 0) {
            throw new BadRequestException("해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteReservationTime(id);
    }
}
