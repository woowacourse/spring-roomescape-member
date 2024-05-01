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
import roomescape.exception.DuplicatedReservationTimeException;
import roomescape.exception.ReservationExistsException;

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
        LocalTime parsedTime = parseTime(request.startAt());
        validateDuplication(parsedTime);
        ReservationTime newReservationTime = new ReservationTime(parsedTime);

        return reservationTimeRepository.save(newReservationTime);
    }

    private LocalTime parseTime(String rawTime) {
        try {
            return LocalTime.parse(rawTime);
        } catch (DateTimeParseException | NullPointerException e) {
            throw new IllegalArgumentException("잘못된 시간 형식을 입력하셨습니다.");
        }
    }

    private void validateDuplication(LocalTime parsedTime) {
        if (reservationTimeRepository.countByStartTime(parsedTime) > 0) {
            throw new DuplicatedReservationTimeException();
        }
    }

    public int delete(Long id) {
        long count = reservationRepository.countByTimeId(id);
        if (count > 0) {
            throw new ReservationExistsException();
        }
        return reservationTimeRepository.deleteById(id);
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }
}
