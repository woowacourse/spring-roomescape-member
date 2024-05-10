package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.global.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.SaveReservationDto;

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

    public Reservation save(SaveReservationDto dto) {
        ReservationTime time = findTime(dto.timeId());
        Theme theme = findTheme(dto.themeId());
        Reservation reservation = new Reservation(dto.name(), dto.date(), time, theme);
        validatePastReservation(LocalDate.parse(dto.date()), time);
        validateDuplication(dto.date(), dto.timeId(), dto.themeId());

        return reservationRepository.save(reservation);
    }

    private ReservationTime findTime(Long timeId) {
        if (timeId == null) {
            throw new RoomescapeException("시간 ID는 null일 수 없습니다.");
        }
        try {
            return reservationTimeRepository.findById(timeId);
        } catch (EmptyResultDataAccessException e) {
            throw new RoomescapeException("입력한 시간 ID에 해당하는 데이터가 존재하지 않습니다.");
        }
    }

    private Theme findTheme(Long themeId) {
        if (themeId == null) {
            throw new RoomescapeException("테마 ID는 null일 수 없습니다.");
        }
        try {
            return themeRepository.findById(themeId);
        } catch (EmptyResultDataAccessException e) {
            throw new RoomescapeException("입력한 테마 ID에 해당하는 데이터가 존재하지 않습니다.");
        }
    }

    private void validatePastReservation(LocalDate date, ReservationTime time) {
        if (date.isBefore(LocalDate.now())) {
            throw new RoomescapeException("과거 예약을 추가할 수 없습니다.");
        }
        if (date.isEqual(LocalDate.now()) && time.isBeforeNow()) {
            throw new RoomescapeException("과거 예약을 추가할 수 없습니다.");
        }
    }

    private void validateDuplication(String rawDate, Long timeId, Long themeId) {
        if (reservationRepository.isDuplicated(rawDate, timeId, themeId)) {
            throw new RoomescapeException("해당 시간에 예약이 이미 존재합니다.");
        }
    }

    public int delete(Long id) {
        return reservationRepository.deleteById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
}
