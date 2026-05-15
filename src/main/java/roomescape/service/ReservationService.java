package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.dto.request.ReservationPatchDto;
import roomescape.dto.request.ReservationRequestDto;
import roomescape.dto.response.PageResponse;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public PageResponse<Reservation> findAll(int page, int size) {
        int offset = page * size;
        List<Reservation> content = reservationDao.findAll(size, offset);
        long totalElements = reservationDao.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        return new PageResponse<>(content, totalElements, totalPages, page, size);
    }

    public Reservation findById(Long id) {
        return reservationDao.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
    }

    public Reservation findActiveById(Long id) {
        Reservation reservation = findById(id);
        if (!reservation.isActive()) {
            throw new NotFoundException("존재하지 않는 예약입니다.");
        }
        return reservation;
    }

    @Transactional
    public Reservation create(ReservationRequestDto reservationRequest) {
        Reservation reservation = buildReservation(reservationRequest);
        reservation.validateCreate(LocalDateTime.now());
        return reservationDao.insert(reservation);
    }

    @Transactional
    public Reservation createByAdmin(ReservationRequestDto reservationRequest) {
        Reservation reservation = buildReservation(reservationRequest);
        return reservationDao.insert(reservation);
    }

    private Reservation buildReservation(ReservationRequestDto reservationRequest) {
        Time timeById = timeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));
        Theme themeById = themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));

        if (reservationDao.existsByThemeIdAndTimeIdAndDate(reservationRequest.themeId(), reservationRequest.timeId(),
                reservationRequest.date())) {
            throw new ConflictException("이미 존재하는 예약이 있습니다.");
        }
        return new Reservation(reservationRequest.name(), reservationRequest.date(), timeById, themeById);
    }

    @Transactional
    public Reservation update(Long id, ReservationPatchDto reservationPatchDto) {
        Reservation reservation = findById(id);
        Time time = timeDao.findById(reservationPatchDto.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시간입니다."));

        reservation.update(reservationPatchDto.date(), time);
        reservationDao.update(reservation);

        return reservation;
    }

    @Transactional
    public void cancel(Long id) {
        Reservation reservation = findById(id);
        reservation.validateCancel(LocalDateTime.now());
        reservationDao.update(reservation);
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationDao.existsById(id)) {
            throw new NotFoundException("존재하지 않는 예약입니다.");
        }

        reservationDao.delete(id);
    }
}
