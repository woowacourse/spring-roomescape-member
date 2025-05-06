package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationAvailableTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.exception.ResourceNotExistException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationRepository repository;

    public ReservationTimeService(final ReservationRepository repository) {
        this.repository = repository;
    }

    public ReservationTimeResponse save(ReservationTimeRequest request) {
        boolean isTimeAlreadyExist = repository.existReservationTimeByTimeValue(request.startAt());
        if (isTimeAlreadyExist) {
            throw new IllegalArgumentException("[ERROR] 해당 시간이 이미 존재합니다.");
        }
        ReservationTime reservationTime = new ReservationTime(request.startAt());

        return ReservationTimeResponse.from(repository.saveReservationTime(reservationTime));
    }

    public List<ReservationTimeResponse> findAll() {
        return repository.findAllReservationTimes()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void deleteReservationTime(Long id) {
        boolean isTimeInUse = repository.existReservationByTimeId(id);
        if (isTimeInUse) {
            throw new IllegalArgumentException("[ERROR] 해당 시간에 대한 예약이 존재하기 때문에 삭제할 수 없습니다.");
        }
        int count = repository.deleteReservationTimeById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
    }

    public List<ReservationAvailableTimeResponse> findAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> times = repository.findAllReservationTimes();
        List<Long> bookedTimeIds = repository.findBookedTimes(themeId, date);

        return times.stream()
                .map(time -> {
                    boolean isBooked = bookedTimeIds.contains(time.getId());
                    return ReservationAvailableTimeResponse.from(time, isBooked);
                }).toList();
    }
}
