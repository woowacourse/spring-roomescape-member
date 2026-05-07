package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation create(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new DomainException(ErrorCode.RESERVATION_TIME_NOT_FOUND));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new DomainException(ErrorCode.THEME_NOT_FOUND));

        Reservation reservation = new Reservation(name, date, time, theme);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
