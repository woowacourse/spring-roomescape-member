package roomescape.reservation.service;

import java.time.LocalTime;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnauthorizedActionException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 예약을 찾을 수 없습니다. id: " + id));
    }

    @Transactional
    public Reservation save(String name, LocalDate date, long timeId, long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다."));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("해당 테마를 찾을 수 없습니다."));

        validateReservationDateTme(date, time.startAt());
        validateDuplicateReservation(date, timeId, themeId);

        try {
            return reservationRepository.save(new Reservation(name, date, time, theme));
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("해당 날짜와 시간은 이미 예약되어 있습니다.");
        }
    }

    private void validateReservationDateTme(LocalDate date, LocalTime time) {
        LocalDate today = LocalDate.now();

        if (date.isBefore(today)) {
            throw new BadRequestException("예약 날짜는 오늘 이후여야 합니다.");
        }
        if (date.equals(today) && time.isBefore(LocalTime.now())) {
            throw new BadRequestException("예약 시간은 현재 시간 이후여야 합니다.");
        }
    }

    private void validateDuplicateReservation(LocalDate date, long timeId, long themeId) {
        boolean isDuplicate = reservationRepository.existsByDateTimeAndTheme(date, timeId, themeId);

        if (isDuplicate) {
            throw new DuplicateException("해당 날짜와 시간, 테마는 이미 예약이 완료되었습니다.");
        }
    }

    @Transactional
    public void deleteByAdmin(long id) {
        Reservation reservation = findById(id);
        reservationRepository.delete(reservation.getId());
    }

    @Transactional
    public void deleteByUser(long id, String userName) {
        Reservation reservation = findById(id);
        if (!reservation.getName().equals(userName)) {
            throw new UnauthorizedActionException("예약자 이름이 일치하지 않아 삭제할 수 없습니다.");
        }
        reservationRepository.delete(reservation.getId());
    }

    public List<Reservation> findAllByName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("조회할 예약자 이름은 필수입니다.");
        }
        return reservationRepository.findAllByName(name);
    }
}
