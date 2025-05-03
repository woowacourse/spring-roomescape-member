package roomescape.application;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.ReservationRepository;
import roomescape.infrastructure.ReservationTimeRepository;
import roomescape.presentation.dto.request.AvailableReservationTimeRequest;
import roomescape.presentation.dto.request.ReservationTimeCreateRequest;
import roomescape.presentation.dto.response.AvailableReservationTimeResponse;
import roomescape.presentation.dto.response.ReservationTimeResponse;
import roomescape.domain.ReservationTime;

@Service
public class ReservationTimeServiceImpl implements ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeServiceImpl(ReservationTimeRepository reservationTimeRepository,
                                      ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> getReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return ReservationTimeResponse.from(reservationTimes);
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeCreateRequest request) {

        LocalTime startAt = request.startAt();
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 시간입니다.");
        }

        ReservationTime reservationTime = ReservationTime.create(request.startAt());
        ReservationTime created = reservationTimeRepository.save(reservationTime);

        return ReservationTimeResponse.from(created);
    }

    public void deleteReservationTimeById(Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new IllegalArgumentException("[ERROR] 해당 시간에 이미 예약이 존재하여 삭제할 수 없습니다.");
        }

        ReservationTime reservationTime = findReservationTimeById(id);
        reservationTimeRepository.deleteById(reservationTime.getId());
    }

    public ReservationTime findReservationTimeById(Long id) {
        return reservationTimeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("[ERROR] 예약 시간을 찾을 수 없습니다."));
    }

    public List<AvailableReservationTimeResponse> getAvailableReservationTimes(
            AvailableReservationTimeRequest request) {
        return reservationTimeRepository.findAllAvailableReservationTimes(request.date(), request.themeId());
    }
}
