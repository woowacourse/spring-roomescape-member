package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.request.ReservationTimeRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationRepository reservationRepository, ReservationTimeRepository reservationTimeRepository) {
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
            throw new DuplicatedException("[ERROR] 중복되는 시간은 추가할 수 없습니다.");
        }
        ReservationTime reservationTime = new ReservationTime(startAt);
        return reservationTimeRepository.addReservationTime(reservationTime);
    }

    public ReservationTime findReservationTime(long id) {
        return reservationTimeRepository.findReservationById(id);
    }

    public void deleteReservationTime(long id) {
        Long count = reservationTimeRepository.countReservationTimeById(id);
        if (count == null || count <= 0) {
            throw new NotFoundException("[ERROR] 존재하지 않는 시간입니다.");
        }
        Long countOfReservationUsingTime = reservationRepository.countReservationByTimeId(id);
        if (countOfReservationUsingTime == null || countOfReservationUsingTime > 0) {
            throw new BadRequestException("[ERROR] 해당 시간을 사용하고 있는 예약이 있습니다.");
        }
        reservationTimeRepository.deleteReservationTime(id);
    }
}
