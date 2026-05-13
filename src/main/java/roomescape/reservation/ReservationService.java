package roomescape.reservation;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

        try {
            return reservationRepository.save(new Reservation(name, date, time, theme));
        } catch (DuplicateKeyException e) {
            throw new DuplicateException("해당 날짜와 시간은 이미 예약되어 있습니다.");
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
}
