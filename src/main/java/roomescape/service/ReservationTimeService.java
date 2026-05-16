package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.ReservationTime;
import roomescape.global.exception.DuplicateEntityException;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.global.exception.ForbiddenException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.web.dto.reservationTime.ReservationTimeRequest;
import roomescape.web.dto.reservationTime.ReservationTimeResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationTimeResponse register(ReservationTimeRequest request) {
        LocalTime startAt = request.startAt();

        validateDuplicateTime(startAt);
        ReservationTime reservationTime = ReservationTime.create(startAt);

        return ReservationTimeResponse.from(reservationTimeRepository.save(reservationTime));
    }

    @Transactional
    public void deactivate(Long id) {
        ReservationTime time = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 시간 정보입니다."));

        if (reservationRepository.existsByTimeId(id)) {
            throw new ForbiddenException("예약이 존재하는 시간대는 삭제할 수 없습니다.");
        }
        ReservationTime deactivatedTime = time.deactivate();
        reservationTimeRepository.update(deactivatedTime);
    }

    public List<ReservationTimeResponse> getAllReservationTimesByPaging(int page, int size) {
        return reservationTimeRepository.findAllByPaging(page, size).stream().map(ReservationTimeResponse::from)
                .toList();
    }

    private void validateDuplicateTime(LocalTime startAt) {
        if (reservationTimeRepository.existsByStartAt(startAt)) {
            throw new DuplicateEntityException("이미 등록된 예약 시간 입니다. %s", startAt);
        }
    }
}
