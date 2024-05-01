package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeRequestDto;
import roomescape.service.dto.ReservationTimeResponseDto;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
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
        if (reservationRepository.hasReservationOf(id)) {
            throw new IllegalStateException("해당 시간에 예약이 있어 삭제할 수 없습니다.");
        }
        if (!reservationTimeRepository.isExistTimeOf(id)) {
            throw new IllegalArgumentException("존재하지 않는 아이디입니다.");
        }
        reservationTimeRepository.deleteReservationTimeById(id);
    }
}
