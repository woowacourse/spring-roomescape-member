package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ReservedChecker;
import roomescape.repository.ReservedTimeChecker;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservedTimeChecker reservedTimeChecker;
    private final ReservedChecker reservedChecker;

    public ReservationTimeService(ReservationTimeRepository jdbcReservationTimeRepository,
                                  ReservedTimeChecker reservedTimeChecker, ReservedChecker reservedChecker) {
        this.reservationTimeRepository = jdbcReservationTimeRepository;
        this.reservedTimeChecker = reservedTimeChecker;
        this.reservedChecker = reservedChecker;
    }

    public ReservationTime addTime(LocalTime startAt) {
        ReservationTime reservationTime = new ReservationTime(null, startAt);
        return reservationTimeRepository.addTime(reservationTime);
    }

    public List<ReservationTime> getAllTime() {
        return reservationTimeRepository.getAllTime();
    }

    public void deleteTime(Long id) {
        if (reservedTimeChecker.isReservedTime(id)) {
            throw new IllegalArgumentException("Reservation time is already reserved.");
        }
        int result = reservationTimeRepository.deleteTime(id);
        if (result == 0) {
            throw new IllegalArgumentException("삭제할 시간이 존재하지 않습니다. id: " + id);
        }
    }

    public ReservationTime getReservationTimeById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다. id: " + id));
    }

    public List<AvailableTimeResponse> getAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> reservationTimes = reservationTimeRepository.getAllTime();
        List<AvailableTimeResponse> availableTimeResponses = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimes) {
            boolean alreadyBooked = false;
            if (reservedChecker.contains(date, reservationTime.getId(), themeId)) {
                alreadyBooked = true;
            }
            availableTimeResponses.add(
                    new AvailableTimeResponse(reservationTime.getId(), reservationTime.getStartAt(), alreadyBooked));
        }
        return availableTimeResponses;
    }
}
