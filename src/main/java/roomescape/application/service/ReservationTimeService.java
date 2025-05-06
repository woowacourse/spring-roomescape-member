package roomescape.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.exception.ResourceNotExistException;
import roomescape.domain.model.ReservationTime;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.presentation.dto.request.ReservationTimeRequest;
import roomescape.presentation.dto.response.ReservationAvailableTimeResponse;
import roomescape.presentation.dto.response.ReservationTimeResponse;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationRepository reservationRepository, final ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    @Transactional
    public ReservationTimeResponse save(ReservationTimeRequest request) {
        boolean isTimeAlreadyExist = reservationTimeRepository.existByTimeValue(request.startAt());
        if (isTimeAlreadyExist) {
            throw new IllegalArgumentException("[ERROR] 해당 시간이 이미 존재합니다.");
        }
        ReservationTime reservationTime = new ReservationTime(request.startAt());

        return ReservationTimeResponse.from(reservationTimeRepository.save(reservationTime));
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteReservationTime(Long id) {
        boolean isTimeInUse = reservationRepository.existByTimeId(id);
        if (isTimeInUse) {
            throw new IllegalArgumentException("[ERROR] 해당 시간에 대한 예약이 존재하기 때문에 삭제할 수 없습니다.");
        }
        int count = reservationTimeRepository.deleteById(id);
        if (count == 0) {
            throw new ResourceNotExistException();
        }
    }

    public List<ReservationAvailableTimeResponse> findAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> times = reservationTimeRepository.findAll();
        List<Long> bookedTimeIds = reservationRepository.findBookedTimes(themeId, date);

        return times.stream()
                .map(time -> {
                    boolean isBooked = bookedTimeIds.contains(time.getId());
                    return ReservationAvailableTimeResponse.from(time, isBooked);
                }).toList();
    }
}
