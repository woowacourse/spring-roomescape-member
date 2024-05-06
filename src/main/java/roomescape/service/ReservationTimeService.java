package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeDto;

import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTime> findAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes();
    }

    public ReservationTime saveReservationTime(ReservationTimeDto reservationTimeDto) {
        LocalTime startAt = reservationTimeDto.getStartAt();
        validateDuplication(startAt);
        ReservationTime reservationTime = ReservationTime.from(reservationTimeDto);
        return reservationTimeRepository.saveReservationTime(reservationTime)
                .orElseThrow(() -> new BadRequestException("[ERROR] 데이터가 저장되지 않습니다."));
    }

    private void validateDuplication(LocalTime startAt) {
        boolean isExist = reservationTimeRepository.isExistReservationTimeByStartAt(startAt);
        if (isExist) {
            throw new DuplicatedException("[ERROR] 중복되는 시간은 추가할 수 없습니다.");
        }
    }

    public ReservationTime findReservationTime(long id) {
        return reservationTimeRepository.findReservationById(id)
                .orElseThrow(() -> new BadRequestException("[ERROR] 데이터가 저장되지 않습니다."));
    }

    public void deleteReservationTime(Long id) {
        validate(id);
        reservationTimeRepository.deleteReservationTimeById(id);
    }

    private void validate(Long id) {
        validateNull(id);
        validateExistence(id);
        validateDependence(id);
    }

    private void validateNull(Long id) {
        if (id == null) {
            throw new BadRequestException("[ERROR] id에 null이 입력될 수 없습니다.");
        }
    }

    private void validateDependence(Long id) {
        boolean isExist = reservationTimeRepository.isExistReservationByTimeId(id);
        if (isExist) {
            throw new BadRequestException("[ERROR] 해당 시간을 사용하고 있는 예약이 있습니다.");
        }
    }

    private void validateExistence(Long id) {
        boolean isNotExist = !reservationTimeRepository.isExistReservationTimeById(id);
        if (isNotExist) {
            throw new NotFoundException("[ERROR] 존재하지 않는 시간입니다.");
        }
    }
}
