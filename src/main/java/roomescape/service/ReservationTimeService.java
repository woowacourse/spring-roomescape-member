package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeAddRequest;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public ReservationTimeResponse addTime(ReservationTimeAddRequest reservationTimeAddRequest) {
        ReservationTime reservationTime = reservationTimeAddRequest.toReservationTime();
        if (reservationTimeRepository.hasSameTime(reservationTime)) {
            throw new IllegalArgumentException("이미 존재하는 시간입니다.");
        }
        ReservationTime addedReservationTime = reservationTimeRepository.save(reservationTime);
        return ReservationTimeResponse.from(addedReservationTime);
    }

    public List<ReservationTimeResponse> findTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public List<AvailableReservationTimeResponse> findAvailableTimes(String date, Long themeId) {
        List<ReservationTime> bookedTimes = reservationRepository.findAll()
                .stream()
                .filter(reservation -> reservation.isSameDate(date))
                .filter(reservation -> reservation.isSameTheme(themeId))
                .map(Reservation::getTime)
                .toList();

        return reservationTimeRepository.findAll()
                .stream()
                .map(reservationTime -> convertToAvailableReservationTimeResponse(reservationTime, bookedTimes))
                .toList();
    }

    private static AvailableReservationTimeResponse convertToAvailableReservationTimeResponse(ReservationTime reservationTime, List<ReservationTime> bookedTimes) {
        boolean alreadyBooked = bookedTimes.contains(reservationTime);
        return new AvailableReservationTimeResponse(reservationTime, alreadyBooked);
    }

    public ReservationTimeResponse getTime(Long id) {
        return ReservationTimeResponse.from(getValidReservationTime(id));
    }

    private ReservationTime getValidReservationTime(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 시간입니다. time_id = " + id));
    }

    public void deleteTime(Long id) {
        reservationTimeRepository.delete(id);
    }
}
