package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.ReservationTimeBookedRequest;
import roomescape.dto.ReservationTimeBookedResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeSaveRequest;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository, ReservationRepository reservationRepository, ThemeRepository themeRepository) {
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
            throw new IllegalArgumentException("중복된 시간입니다.");
        }

    }

    public void deleteTime(final Long id) {
        validateDeleteTime(id);
        reservationTimeRepository.deleteById(id);
    }

    private void validateDeleteTime(final Long id) {
        reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        final boolean existByTimeId = reservationRepository.existByTimeId(id);
        if (existByTimeId) {
            throw new IllegalArgumentException("예약이 존재하는 시간입니다.");
        }
    }

    public List<ReservationTimeBookedResponse> getTimesWithBooked(final ReservationTimeBookedRequest reservationTimeBookedRequest) {
        themeRepository.findById(reservationTimeBookedRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        return reservationTimeRepository.findTimesWithBooked(reservationTimeBookedRequest.date(), reservationTimeBookedRequest.themeId());
    }
}
