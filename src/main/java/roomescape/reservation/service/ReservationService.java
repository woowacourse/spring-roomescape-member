package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.ConflictException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;
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
                .orElseThrow(() -> new NotFoundException("선택한 예약 시간이 존재하지 않습니다. 다른 시간을 선택해주세요."));
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("선택한 테마가 존재하지 않습니다. 다른 테마를 선택해주세요."));

        validateNotDuplicated(date, time, theme);

        Reservation reservation = new Reservation(name, date, time, theme);
        return reservationRepository.save(reservation);
    }

    private void validateNotDuplicated(LocalDate date, ReservationTime time, Theme theme) {
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(date, time.getId(), theme.getId())) {
            throw new ConflictException("선택한 날짜와 시간에는 이미 해당 테마의 예약이 있습니다. 다른 시간을 선택해주세요.");
        }
    }

    @Transactional
    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
