package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeCreateRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.DuplicateContentException;
import roomescape.repository.ReservationTimeRepository;

import java.util.List;

@Service
public class ReservationTimeService {

    private ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponseDto createReservationTime(final ReservationTimeCreateRequestDto requestDto) {
        ReservationTime requestTime = requestDto.createWithoutId();
        try {
            ReservationTime savedTime = reservationTimeRepository.save(requestTime)
                    .orElseThrow(() -> new IllegalStateException("[ERROR] 알 수 없는 오류로 인해 예약시간을 생성 실패하였습니다."));

            return ReservationTimeResponseDto.from(savedTime);
        } catch (IllegalArgumentException e) {
            throw new DuplicateContentException(e.getMessage());
        }
    }

    public List<ReservationTimeResponseDto> findAllReservationTimes() {
        List<ReservationTime> allReservationTime = reservationTimeRepository.findAll();
        return allReservationTime.stream()
                .map(reservationTime -> ReservationTimeResponseDto.from(reservationTime))
                .toList();
    }

    public void deleteReservationTimeById(final Long id) {
        int deletedReservationCount = reservationTimeRepository.deleteById(id);

        if (deletedReservationCount == 0) {
            throw new IllegalStateException("[ERROR] 등록된 예약 시간 번호만 삭제할 수 있습니다. 입력된 번호는 " + id + "입니다.");
        }
    }
}
