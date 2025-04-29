package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeCreationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> getAllReservationTime() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime getReservationTimeById(long reservationTimeId) {
        return loadReservationTimeById(reservationTimeId);
    }

    public long saveReservationTime(ReservationTimeCreationRequest request) {
        validateAlreadySaved(request.getStartAt());
        ReservationTime newReservation = ReservationTime.createWithoutId(request.getStartAt());
        return reservationTimeRepository.add(newReservation);
    }

    public void deleteReservationTime(Long reservationTimeID) {
        loadReservationTimeById(reservationTimeID);
        validateReservationsExistenceInTime(reservationTimeID);
        reservationTimeRepository.deleteById(reservationTimeID);
    }

    private ReservationTime loadReservationTimeById(long reservationId) {
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(reservationId);
        return reservationTime
                .orElseThrow(() -> new NotFoundException("[ERROR] ID에 해당하는 예약 시간이 존재하지 않습니다."));
    }

    private void validateReservationsExistenceInTime(long timeId) {
        boolean isExistenceInTime = reservationRepository.checkExistenceInTime(timeId);
        if (isExistenceInTime) {
            throw new BadRequestException("[ERROR] 이미 해당 시간에 대한 예약 데이터들이 존재합니다.");
        }
    }

    private void validateAlreadySaved(LocalTime time) {
        boolean isAlreadySaved = reservationTimeRepository.findByStartAt(time);
        if (isAlreadySaved) {
            throw new BadRequestException("[Error] 이미 추가가 완료된 예약 가능 시간입니다.");
        }
    }
}
