package roomescape.service;

import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.ThemeRepository;
import roomescape.dto.ReservationTimeBookedRequest;
import roomescape.dto.ReservationTimeBookedResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeSaveRequest;
import roomescape.exception.ReservationBusinessException;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository, ThemeRepository themeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationTimeResponse> getTimes() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public ReservationTimeResponse saveTime(final ReservationTimeSaveRequest reservationTimeSaveRequest) {
        final ReservationTime reservationTime = reservationTimeSaveRequest.toReservationTime();

        validateUniqueReservationTime(reservationTime);

        final ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        return new ReservationTimeResponse(savedReservationTime);
    }

    private void validateUniqueReservationTime(final ReservationTime reservationTime) {
        boolean isTimeExist = reservationTimeRepository.existByStartAt(reservationTime.getStartAt());

        if (isTimeExist) {
            throw new ReservationBusinessException("중복된 시간입니다.");
        }

    }

    public void deleteTime(final Long id) {
        validateDeleteTime(id);
        reservationTimeRepository.deleteById(id);
    }

    private void validateDeleteTime(final Long id) {
        if (reservationTimeRepository.findById(id).isEmpty()) {
            throw new ReservationBusinessException("존재하지 않는 시간입니다.");
        }

        if (reservationRepository.existByTimeId(id)) {
            throw new ReservationBusinessException("예약이 존재하는 시간입니다.");
        }
    }

    public List<ReservationTimeBookedResponse> getTimesWithBooked(
            final ReservationTimeBookedRequest reservationTimeBookedRequest) {
        if (themeRepository.findById(reservationTimeBookedRequest.themeId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }

        final List<ReservationTime> bookedTimes = reservationRepository.findTimeByDateAndThemeId(reservationTimeBookedRequest.date(),
                reservationTimeBookedRequest.themeId());
        final List<ReservationTime> times = reservationTimeRepository.findAll();

        return times.stream()
                .sorted(Comparator.comparing(ReservationTime::getStartAt))
                .map(time -> ReservationTimeBookedResponse.of(time, bookedTimes.contains(time)))
                .toList();
    }
}
