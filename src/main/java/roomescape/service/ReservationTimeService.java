package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.entity.ReservationTimeEntity;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;

import java.util.List;

@Service
public class ReservationTimeService {
    private final ReservationTimeDao timeDao;

    public ReservationTimeService(ReservationTimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public ReservationTimeResponse create(ReservationTimeRequest requestDto) {
        ReservationTimeEntity entity = requestDto.toEntity();
        validateOperatingTime(entity);
        validateDuplicated(entity);
        ReservationTimeEntity saved = timeDao.save(entity);
        return ReservationTimeResponse.from(saved);
    }

    private void validateOperatingTime(ReservationTimeEntity entity) {
        if (!entity.isAvailable()) {
            throw new BadRequestException("운영 시간 이외의 날짜는 예약할 수 없습니다.");
        }
    }

    private void validateDuplicated(ReservationTimeEntity entity) {
        List<ReservationTimeEntity> times = timeDao.findAll();
        if (times.stream().anyMatch(time -> time.isDuplicatedWith(entity))) {
            throw new ConflictException("러닝 타임이 겹치는 시간이 존재합니다.");
        }
    }

    public List<ReservationTimeResponse> getAllTimes() {
        return timeDao.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void delete(final Long id) {
        final boolean deleted = timeDao.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }
}
