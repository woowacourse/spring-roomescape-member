package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.dto.AvailabilityOfTimeRequest;
import roomescape.service.dto.AvailabilityOfTimeResponse;
import roomescape.service.dto.ReservationTimeRequest;
import roomescape.service.dto.ReservationTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationTimeResponse> findAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes()
                .stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public List<AvailabilityOfTimeResponse> findReservationTimesAvailability(AvailabilityOfTimeRequest requestDto) {
        List<ReservationTime> allTimes = reservationTimeRepository.findAllReservationTimes();
        List<ReservationTime> bookedTimes = reservationTimeRepository.findBookedTimeForThemeAtDate(
                requestDto.getDate(), requestDto.getThemeId());

        return allTimes.stream()
                .map(reservationTime -> new AvailabilityOfTimeResponse(
                        reservationTime,
                        bookedTimes.contains(reservationTime)))
                .toList();
    }

    public ReservationTimeResponse createReservationTime(ReservationTimeRequest requestDto) {
        ReservationTime reservationTime = requestDto.toReservationTime();
        if (reservationTimeRepository.isExistTimeOf(reservationTime.getStartAt().toString())) {
            throw new IllegalArgumentException("중복된 시간을 입력할 수 없습니다.");
        }
        ReservationTime savedTime = reservationTimeRepository.insertReservationTime(reservationTime);
        return new ReservationTimeResponse(savedTime);
    }

    public void deleteReservationTime(long id) {
        if (!reservationTimeRepository.isExistTimeOf(id)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        if (reservationRepository.hasReservationOfTimeId(id)) {
            throw new IllegalArgumentException("해당 시간에 예약이 있어 삭제할 수 없습니다.");
        }
        reservationTimeRepository.deleteReservationTimeById(id);
    }
}
