package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
import roomescape.global.exception.InvalidRequestException;
import roomescape.global.exception.NotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
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

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation create(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 테마입니다."));

        validateNotDuplicated(date, timeId, themeId);

        Reservation reservation = new Reservation(name, date, time, theme);
        return reservationRepository.save(reservation);
    }

    private void validateNotDuplicated(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new InvalidRequestException("이미 예약된 시간입니다.");
        }
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
