package roomescape.reservation.service;

import java.time.LocalDate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.DuplicateException;
import roomescape.exception.InvalidRequestException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class AdminReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public AdminReservationService(ReservationRepository reservationRepository,
                                   ReservationTimeRepository reservationTimeRepository,
                                   ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Reservation forceCreateReservation(Long themeId, String name, LocalDate date, Long timeId) {
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("해당 시간을 찾을 수 없습니다."));

        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("해당 테마를 찾을 수 없습니다."));

        try {
            return reservationRepository.save(name, date, reservationTime, theme);
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("해당 날짜의 해당 시간은 이미 예약되었습니다.");
        } catch (DataIntegrityViolationException e) {
            throw new InvalidRequestException("요청이 데이터 무결성 조건을 위반했습니다.");
        }
    }

    @Transactional
    public void forceDeleteReservation(long id) {
        reservationRepository.delete(id);
    }
}
