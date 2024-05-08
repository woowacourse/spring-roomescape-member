package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.JdbcReservationRepository;
import roomescape.repository.JdbcReservationTimeRepository;

@Service
public class ReservationTimeService {

    private final JdbcReservationTimeRepository reservationTimeRepository;
    private final JdbcReservationRepository reservationRepository;

    public ReservationTimeService(JdbcReservationTimeRepository reservationTimeRepository,
                                  JdbcReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> getAllReservationTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public ReservationTimeResponse addReservationTime(ReservationTimeRequest request) {
        ReservationTime reservationTime = request.toReservationTime();
        ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(savedReservationTime);
    }

    public void deleteReservationTimeById(Long id) {
        boolean exist = reservationRepository.existByReservationTimeId(id);
        if (exist) {
            throw new IllegalArgumentException("해당 시간에 예약이 존재합니다.");
        }

        reservationTimeRepository.deleteById(id);
    }

    public List<AvailableReservationTimeResponse> getReservationTimeBookedStatus(LocalDate date, Long themeId) {
        List<ReservationTime> bookedTimes = reservationTimeRepository.findByReservationDateAndThemeId(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(reservationTime -> AvailableReservationTimeResponse.from(
                        reservationTime,
                        bookedTimes.contains(reservationTime)
                ))
                .toList();
    }
}
