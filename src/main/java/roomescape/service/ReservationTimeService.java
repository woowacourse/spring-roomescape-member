package roomescape.service;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.app.ReservationTimeAppRequest;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
        ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTime save(ReservationTimeAppRequest request) {
        try {
            LocalTime parsedTime = LocalTime.parse(request.startAt());
            validateDuplication(parsedTime);
            ReservationTime newReservationTime = new ReservationTime(parsedTime);
            return reservationTimeRepository.save(newReservationTime);
        } catch (DateTimeParseException | NullPointerException exception) {
            throw new IllegalArgumentException("잘못된 시간 입력입니다.");
        }
    }

    private void validateDuplication(LocalTime parsedTime) {
        if (reservationTimeRepository.countByStartTime(parsedTime) > 0) {
            throw new IllegalArgumentException("예약 시간이 이미 존재합니다.");
        }
    }

    // TODO: void 메소드일 때도 모킹이 가능한지 확인
    public long delete(Long id) {
        long count = reservationRepository.countByTimeId(id);
        if (count > 0) {
            throw new IllegalArgumentException("해당 시간을 사용하는 예약이 존재합니다.");
        }
        reservationTimeRepository.deleteById(id);
        return id;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }
}
