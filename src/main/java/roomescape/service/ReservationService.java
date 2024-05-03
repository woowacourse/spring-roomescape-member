package roomescape.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.exception.DuplicatedModelException;
import roomescape.exception.PastReservationException;
import roomescape.service.dto.ReservationAppRequest;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        ReservationTimeRepository reservationTimeRepository,
        ThemeRepository themeRepository) {

        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    public Reservation save(ReservationAppRequest request) {
        ReservationDate date = new ReservationDate(request.date());
        ReservationTime time = findTime(request.timeId());
        Theme theme = findTheme(request.themeId());
        Reservation reservation = new Reservation(request.name(), date, time, theme);
        validatePastReservation(date, time);
        validateDuplication(date, request.timeId(), request.themeId());

        return reservationRepository.save(reservation);
    }

    private ReservationTime findTime(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("시간 id는 null이 입력될 수 없습니다.");
        }

        return reservationTimeRepository.findById(timeId)
            .orElseThrow(() -> new NoSuchElementException("예약에 대한 예약시간이 존재하지 않습니다."));
    }

    private Theme findTheme(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마 id는 null이 입력될 수 없습니다.");
        }

        return themeRepository.findById(themeId)
            .orElseThrow(() -> new NoSuchElementException("예약에 대한 테마가 존재하지 않습니다."));
    }

    private void validatePastReservation(ReservationDate date, ReservationTime time) {
        if (date.isBeforeNow() || date.isToday() && time.isBeforeNow()) {
            throw new PastReservationException();
        }
    }

    private void validateDuplication(ReservationDate date, Long timeId, Long themeId) {
        if (reservationRepository.isDuplicated(date.getDate(), timeId, themeId)) {
            throw new DuplicatedModelException(Reservation.class);
        }
    }

    public int delete(Long id) {
        return reservationRepository.deleteById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
}
