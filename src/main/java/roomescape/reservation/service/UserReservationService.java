package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateException;
import roomescape.exception.InvalidInputException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class UserReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public UserReservationService(ReservationRepository reservationRepository,
                                  ReservationTimeRepository reservationTimeRepository,
                                  ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation createReservation(String name, LocalDate date, long timeId, long themeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다."));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("해당 테마를 찾을 수 없습니다."));

        reservationTime.validateFutureDate(date);

        try {
            return reservationRepository.save(name, date, reservationTime, theme);
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("해당 날짜의 해당 시간은 이미 예약되었습니다.");
        } catch (DataIntegrityViolationException e) {
            throw new InvalidInputException("요청이 데이터 무결성 조건을 위반했습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> getMyReservations(String name) {
        return reservationRepository.findByName(name);
    }

    @Transactional
    public void deleteReservation(long id, String name) {
        reservationRepository.findById(id).ifPresent(reservation -> {
            validateOwnerAndActive(reservation, name);
            reservationRepository.delete(id);
        });
    }

    @Transactional
    public Reservation updateReservation(long id, String name, LocalDate newDate, long newTimeId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 예약을 찾을 수 없습니다."));
        validateOwnerAndActive(reservation, name);

        ReservationTime newTime = reservationTimeRepository.findById(newTimeId)
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다."));

        newTime.validateFutureDate(newDate);

        List<Long> takenTimeIds = reservationRepository.findByDateAndTheme(newDate, reservation.getTheme().id());
        if (!reservation.isSameSlot(newDate, newTimeId) && takenTimeIds.contains(newTimeId)) {
            throw new DuplicateException("해당 날짜의 해당 시간은 이미 예약되었습니다.");
        }

        try {
            reservationRepository.update(id, newDate, newTimeId);
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("해당 날짜의 해당 시간은 이미 예약되었습니다.");
        }
        return new Reservation(id, reservation.getName(), newDate, newTime, reservation.getTheme());
    }

    private void validateOwnerAndActive(Reservation reservation, String name) {
        reservation.validateOwner(name);
        reservation.validateIsActive();
    }
}