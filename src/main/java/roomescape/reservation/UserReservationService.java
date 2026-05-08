package roomescape.reservation;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ApiException;
import roomescape.exception.DuplicateException;
import roomescape.exception.NotFoundException;
import roomescape.exception.UnauthorizedActionException;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

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

    @Transactional
    public void deleteReservation(long id, String name) {
        Reservation reservation = reservationRepository.findById(id);
        if (reservation.getName().equals(name)) {
            reservationRepository.delete(id);
            return;
        }
        throw new UnauthorizedActionException("예약자 이름이 일치하지 않습니다.");
    }
}
