package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeRequestDto;
import roomescape.service.dto.ReservationTimeResponseDto;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public List<ReservationTimeResponseDto> findAllReservationTimes() {
        return reservationTimeRepository.findAllReservationTimes()
                .stream()
                .map(ReservationTimeResponseDto::new)
                .toList();
    }

    public ReservationTimeResponseDto createReservationTime(ReservationTimeRequestDto requestDto) {
        if (reservationTimeRepository.isExistTimeOf(requestDto.getStartAt())) {
            throw new IllegalArgumentException("중복된 시간을 입력할 수 없습니다.");
        }
        ReservationTime time = reservationTimeRepository.insertReservationTime(requestDto.toReservationTime());
        return new ReservationTimeResponseDto(time);
    }

    public void deleteReservationTime(long id) {
        reservationTimeRepository.deleteReservationTimeById(id);
    }
}
