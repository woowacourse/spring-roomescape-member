package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.time.TimeRequest;
import roomescape.controller.time.TimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.exception.TimeUsedException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
public class TimeService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository timeRepository;

    public TimeService(final ReservationRepository reservationRepository, final ReservationTimeRepository timeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
    }

    public List<TimeResponse> getTimes() {
        return timeRepository.findAll().stream()
                .map(TimeResponse::from)
                .toList();
    }

    // TODO: [3단계] 4. getTimeAvailable(date, themeId) 같은 함수 만들어서 컨트롤러의 요청에 대해 응답
    // TODO: [3단계] 6. timeRepository.findAll 과 reservationRepository.findAllByDateAndThemeId 을 이용해서 response list 반환, 끝!

    public TimeResponse addTime(final TimeRequest timeRequest) {
        ReservationTime parsedTime = timeRequest.toDomain();
        ReservationTime savedTime = timeRepository.save(parsedTime);
        return TimeResponse.from(savedTime);
    }

    public int deleteTime(final Long id) {
        if (reservationRepository.existsByTimeId(id)) {
            throw new TimeUsedException("예약된 시간은 삭제할 수 없습니다.");
        }
        return timeRepository.deleteById(id);
    }
}
