package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.reservation.repository.ReservationDao;
import roomescape.time.repository.ReservationTimeDao;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.entity.ReservationEntity;
import roomescape.time.entity.ReservationTimeEntity;
import roomescape.exception.BadRequestException;
import roomescape.exception.ConflictException;
import roomescape.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
    }

    public List<ReservationResponse> getAllReservation() {
        return reservationDao.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse createReservation(ReservationRequest request) {
        ReservationTimeEntity timeEntity = timeDao.findById(request.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 id 입니다."));

        ReservationEntity newReservation = request.toEntity(timeEntity);
        validateDateTime(newReservation);
        validateDuplicated(newReservation);

        ReservationEntity saved = reservationDao.save(newReservation);
        return ReservationResponse.from(saved);
    }

    private void validateDateTime(ReservationEntity reservation) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reservationDateTime = reservation.getDateTime();
        if (reservationDateTime.isBefore(now)) {
            throw new BadRequestException("과거 날짜/시간의 예약은 생성할 수 없습니다.");
        }
    }

    private void validateDuplicated(ReservationEntity newReservation) {
        if (isExistDuplicatedWith(newReservation)) {
            throw new ConflictException("해당 날짜에는 이미 예약이 존재합니다.");
        }
    }

    private boolean isExistDuplicatedWith(ReservationEntity target) {
        List<ReservationEntity> reservations = reservationDao.findAll();
        return reservations.stream().anyMatch(reservation -> reservation.isDuplicatedWith(target));
    }

    public void deleteReservation(final Long id) {
        final boolean deleted = reservationDao.deleteById(id);
        if (!deleted) {
            throw new NotFoundException("존재하지 않는 id 입니다.");
        }
    }
}
