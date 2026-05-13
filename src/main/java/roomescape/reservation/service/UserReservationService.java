package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ApiException;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnauthorizedActionException;
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
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다"));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("해당 테마를 찾을 수 없습니다."));

        if (LocalDateTime.of(date, reservationTime.startAt()).isBefore(LocalDateTime.now())) {
            throw new ApiException("지나간 날짜·시간에는 예약할 수 없습니다.");
        }

        try {
            return reservationRepository.save(name, date, reservationTime, theme);
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("해당 날짜의 해당 시간은 이미 예약되었습니다");
        } catch (DataIntegrityViolationException e) {
            throw new ApiException("요청이 데이터 무결성 조건을 위반했습니다");
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
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 예약을 찾을 수 없습니다."));

        if (!reservation.getName().equals(name)) {
            throw new UnauthorizedActionException("예약자 이름이 일치하지 않습니다.");
        }

        if (LocalDateTime.of(reservation.getDate(), reservation.getTime().startAt()).isBefore(LocalDateTime.now())) {
            throw new ApiException("이미 지난 예약은 취소할 수 없습니다.");
        }

        reservationRepository.delete(id);
    }

    @Transactional
    public Reservation updateReservation(long id, String name, LocalDate newDate, long newTimeId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 예약을 찾을 수 없습니다."));

        if (!reservation.getName().equals(name)) {
            throw new UnauthorizedActionException("예약자 이름이 일치하지 않습니다.");
        }

        if (LocalDateTime.of(reservation.getDate(), reservation.getTime().startAt()).isBefore(LocalDateTime.now())) {
            throw new ApiException("이미 지난 예약은 변경할 수 없습니다.");
        }

        ReservationTime newTime = reservationTimeRepository.findById(newTimeId)
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다"));

        if (LocalDateTime.of(newDate, newTime.startAt()).isBefore(LocalDateTime.now())) {
            throw new ApiException("지나간 날짜·시간에는 예약할 수 없습니다.");
        }

        List<Long> takenTimeIds = reservationRepository.findByDateAndTheme(newDate, reservation.getTheme().id());
        if (takenTimeIds.stream().anyMatch(tid -> tid == newTimeId)) {
            throw new DuplicateException("해당 날짜의 해당 시간은 이미 예약되었습니다");
        }

        reservationRepository.update(id, newDate, newTimeId);
        return new Reservation(id, name, newDate, newTime, reservation.getTheme());
    }
}
