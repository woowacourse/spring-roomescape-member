package roomescape.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.dto.ReservationCreateRequest;
import roomescape.domain.reservation.dto.ReservationResponse;
import roomescape.domain.reservation.dto.ReservationUpdateRequest;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.exception.CustomException;
import roomescape.exception.CustomExceptionCode;
import roomescape.repository.ReservationQueryingDao;
import roomescape.repository.ReservationTimeQueryingDao;
import roomescape.repository.ReservationUpdatingDao;
import roomescape.repository.ThemeQueryingDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ReservationService {

    private final ReservationQueryingDao reservationQueryingDao;
    private final ReservationUpdatingDao reservationUpdatingDao;
    private final ReservationTimeQueryingDao reservationTimeQueryingDao;
    private final ThemeQueryingDao themeQueryingDao;

    public ReservationService(ReservationQueryingDao reservationQueryingDao, ReservationUpdatingDao reservationUpdatingDao, ReservationTimeQueryingDao reservationTimeQueryingDao, ThemeQueryingDao themeQueryingDao) {
        this.reservationQueryingDao = reservationQueryingDao;
        this.reservationUpdatingDao = reservationUpdatingDao;
        this.reservationTimeQueryingDao = reservationTimeQueryingDao;
        this.themeQueryingDao = themeQueryingDao;
    }

    @Transactional
    public ReservationResponse create(ReservationCreateRequest reservationReq) {
        ReservationTime findReservationTime = reservationTimeQueryingDao.findReservationTimeById(reservationReq.getTimeId())
                .orElseThrow(() -> new CustomException(CustomExceptionCode.RESERVATION_TIME_NOT_FOUND));
        Theme findTheme = themeQueryingDao.findThemeById(reservationReq.getThemeId())
                .orElseThrow(() -> new CustomException(CustomExceptionCode.THEME_NOT_FOUND));

        if (reservationReq.getDate().isBefore(LocalDate.now())) {
            throw new CustomException(CustomExceptionCode.PAST_RESERVATION_DATE);
        }

        Optional<Reservation> savedReservation = reservationQueryingDao.findReservationByThemeAndDateAndTime(findTheme.getId(), reservationReq.getDate(), findReservationTime.getId());
        if (savedReservation.isPresent()) {
            throw new CustomException(CustomExceptionCode.RESERVATION_ALREADY_EXISTS);
        }

        Long generatedId;
        try {
            generatedId = reservationUpdatingDao.save(reservationReq);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(CustomExceptionCode.RESERVATION_ALREADY_EXISTS);
        }

        Reservation findReservation = reservationQueryingDao.findReservationById(generatedId)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.RESERVATION_NOT_FOUND));

        LocalDateTime standard = LocalDateTime.of(
                findReservation.getDate(),
                findReservationTime.getStartAt()
        );

        if (!standard.isAfter(findReservation.getCreatedAt())) {
            throw new CustomException(CustomExceptionCode.PAST_RESERVATION_DATE);
        }

        return ReservationResponse.from(findReservation);
    }

    public ReservationResponse read(Long id) {
        Reservation reservationById = reservationQueryingDao.findReservationById(id)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.RESERVATION_NOT_FOUND));
        return ReservationResponse.from(reservationById);
    }

    public List<ReservationResponse> readAll() {
        List<Reservation> reservations = reservationQueryingDao.findAllReservations();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> readMyReservations(String name) {
        List<Reservation> reservations = reservationQueryingDao.findMyReservations(name);
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse update(Long id, ReservationUpdateRequest newReservationReq) {
        if (!reservationQueryingDao.existsById(id)) {
            throw new CustomException(CustomExceptionCode.RESERVATION_NOT_FOUND);
        }
        reservationUpdatingDao.update(id, newReservationReq);

        Reservation findReservation = reservationQueryingDao.findReservationById(id)
                .orElseThrow(() -> new CustomException(CustomExceptionCode.RESERVATION_NOT_FOUND));
        return ReservationResponse.from(findReservation);
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationQueryingDao.existsById(id)) {
            throw new CustomException(CustomExceptionCode.RESERVATION_NOT_FOUND);
        }

        reservationUpdatingDao.delete(id);
    }
}
