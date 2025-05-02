package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.other.TimeWithBookState;
import roomescape.dto.request.ReservationTimeCreationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(
            ReservationRepository reservationRepository,
            ReservationTimeRepository reservationTimeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> getAllReservationTime() {
        return reservationTimeRepository.findAll();
    }

    public List<TimeWithBookState> getAllReservationTimeWithBookState(LocalDate date, long themeId) {
        return reservationTimeRepository.findAllWithBookState(date, themeId);
    }

    public ReservationTime getReservationTimeById(long reservationTimeId) {
        return loadReservationTimeById(reservationTimeId);
    }

    public long saveReservationTime(ReservationTimeCreationRequest request) {
        validateAlreadySaved(request.startAt());
        ReservationTime newReservation = ReservationTime.createWithoutId(request.startAt());
        return reservationTimeRepository.add(newReservation);
    }

    public void deleteReservationTime(Long reservationTimeID) {
        validateReservationTimeById(reservationTimeID);
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

    private void validateReservationTimeById(long reservationId) {
        Optional<ReservationTime> reservationTime = reservationTimeRepository.findById(reservationId);
        if (reservationTime.isEmpty()) {
            throw new NotFoundException("[ERROR] ID에 해당하는 예약 시간이 존재하지 않습니다.");
        }
    }
}
