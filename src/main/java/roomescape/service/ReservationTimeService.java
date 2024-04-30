package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;
import roomescape.dto.reservationtime.ReservationTimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.repository.reservationtime.ReservationTimeRepository;

import java.util.List;

@Service
public class ReservationTimeService {

    private static final String RESERVATION_TIME_NOT_FOUND = "존재하지 않는 예약 시간입니다.";

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse createTime(ReservationTimeCreateRequest request) {
        ReservationTime reservationTime = request.toReservationTime();

        validateDuplicated(reservationTime);

        ReservationTime newReservationTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponse.from(newReservationTime);
    }

    private void validateDuplicated(ReservationTime reservationTime) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        boolean isDuplicated = reservationTimes.stream()
                .anyMatch(reservationTime::isSame);
        if (isDuplicated) {
            throw new BadRequestException("중복된 예약 시간입니다.");
        }
    }

    public ReservationTimeResponse readReservationTime(Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESERVATION_TIME_NOT_FOUND));
        return ReservationTimeResponse.from(reservationTime);
    }

    public List<ReservationTimeResponse> readReservationTimes() {
        return reservationTimeRepository.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void deleteTime(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
