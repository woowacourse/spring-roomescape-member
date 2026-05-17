package roomescape.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.CreateReservationRequest;
import roomescape.dto.UpdateReservationRequest;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.PastReservationException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.exception.UnauthorizedReservationException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final Clock clock;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, Clock clock) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.clock = clock;
    }

    public List<Reservation> getReservations() {
        return reservationDao.findAll();
    }

    public Reservation getReservation(Long id) {
        return reservationDao.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("존재하지 않는 예약입니다."));
    }

    public List<Reservation> getMyReservations(Long userId) {
        return reservationDao.findAllByUserId(userId);
    }

    public Reservation createReservation(CreateReservationRequest request) {
        checkDuplicateReservation(request);
        checkNotPast(request);

        Long newReservationId = reservationDao.save(request);
        return reservationDao.findById(newReservationId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 저장에 실패했습니다."));
    }

    private void checkDuplicateReservation(CreateReservationRequest request) {
        boolean isDuplicate = reservationDao.isExistsByDateAndTimeIdAndThemeId(
                request.date(), request.timeId(), request.themeId());
        if (isDuplicate) {
            throw new DuplicateReservationException("선택하신 날짜·시간·테마에 이미 예약이 있습니다. 다른 시간을 선택해 주세요.");
        }
    }

    private void checkNotPast(CreateReservationRequest request) {
        ReservationTime time = reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException("존재하지 않는 예약 시간입니다."));
        LocalDateTime reservationAt = LocalDateTime.of(request.date(), time.getStartAt());
        if (reservationAt.isBefore(LocalDateTime.now(clock))) {
            throw new PastReservationException("지난 날짜는 예약할 수 없습니다. 오늘 이후 날짜를 선택해주세요.");
        }
    }

    public Reservation updateReservation(Long reservationId, Long userId, UpdateReservationRequest request) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("존재하지 않는 예약입니다."));
        if (!reservation.getUser().getId().equals(userId)) {
            throw new UnauthorizedReservationException("본인의 예약만 변경할 수 있습니다.");
        }
        checkNotPastReservation(reservation);

        ReservationTime time = reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new ReservationTimeNotFoundException("존재하지 않는 예약 시간입니다."));
        LocalDateTime newReservationAt = LocalDateTime.of(request.date(), time.getStartAt());
        if (newReservationAt.isBefore(LocalDateTime.now(clock))) {
            throw new PastReservationException("지난 날짜로 변경할 수 없습니다. 오늘 이후 날짜를 선택해 주세요.");
        }

        boolean isDuplicate = reservationDao.isExistsByDateAndTimeIdAndThemeIdExcludingId(
                request.date(), request.timeId(), reservation.getTheme().getId(), reservationId);
        if (isDuplicate) {
            throw new DuplicateReservationException("선택하신 날짜·시간·테마에 이미 예약이 있습니다. 다른 시간을 선택해 주세요.");
        }

        reservationDao.updateDateAndTime(reservationId, request.date(), request.timeId());
        return reservationDao.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("존재하지 않는 예약입니다."));
    }

    private void checkNotPastReservation(Reservation reservation) {
        LocalDateTime reservationAt = LocalDateTime.of(reservation.getDate(), reservation.getTime().getStartAt());
        if (reservationAt.isBefore(LocalDateTime.now(clock))) {
            throw new PastReservationException("지난 예약은 변경하거나 취소할 수 없습니다.");
        }
    }

    public void deleteMyReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationDao.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("존재하지 않는 예약입니다."));
        if (!reservation.getUser().getId().equals(userId)) {
            throw new UnauthorizedReservationException("본인의 예약만 삭제할 수 있습니다.");
        }
        checkNotPastReservation(reservation);
        reservationDao.deleteById(reservationId);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
