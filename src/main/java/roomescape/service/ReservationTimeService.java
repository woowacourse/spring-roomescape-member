package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeCreationRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeWithBookState;
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

    public List<ReservationTimeResponse> getAllReservationTime() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<ReservationTimeWithBookState> getAllReservationTimeWithBookState(LocalDate date, long themeId) {
        return reservationTimeRepository.findAllWithBookState(date, themeId);
    }

    public ReservationTimeResponse getReservationTimeById(long reservationTimeId) {
        ReservationTime reservationTime = loadReservationTimeById(reservationTimeId);
        return ReservationTimeResponse.from(reservationTime);
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
