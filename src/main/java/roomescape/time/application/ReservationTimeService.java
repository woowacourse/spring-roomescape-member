package roomescape.time.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;
import roomescape.time.application.dto.AvailableReservationTimeFindCommand;
import roomescape.time.application.dto.AvailableReservationTimeInfo;
import roomescape.time.application.dto.ReservationTimeCommand;
import roomescape.time.application.exception.DuplicateReservationTimeException;
import roomescape.time.domain.ReservationTime;
import roomescape.time.application.exception.ReservationTimeInUseException;
import roomescape.time.application.exception.ReservationTimeNotFoundException;
import roomescape.time.domain.ReservationTimeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationTimeService {

    private static final int DELETE_ROW_COUNTS = 0;

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;

    @Transactional(readOnly = true)
    public List<ReservationTime> getReservationTimes() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime addReservationTime(ReservationTimeCommand time) {
        if (reservationTimeRepository.existsByStartAt(time.startAt())) {
            throw new DuplicateReservationTimeException("이미 존재하는 시간입니다.");
        }
        return reservationTimeRepository.save(time.toEntity());
    }

    public void deleteReservationTime(Long id) {
        if (reservationRepository.existsByReservationTime(id)) {
            throw new ReservationTimeInUseException("해당 시간에 예약이 존재합니다.");
        }
        if (reservationTimeRepository.deleteById(id) == DELETE_ROW_COUNTS) {
            throw new ReservationTimeNotFoundException("존재하지 않는 시간ID 입니다.");
        }
    }

    @Transactional(readOnly = true)
    public AvailableReservationTimeInfo getAvailableReservationTime(AvailableReservationTimeFindCommand command) {
        Theme theme = themeRepository.getById(command.themeId());
        List<Reservation> reservations = reservationRepository.findByThemeAndDate(
                command.themeId(), command.date());
        List<ReservationTime> allTimes = reservationTimeRepository.findAll();
        Set<ReservationTime> reservedTimes = reservations.stream()
                .map(Reservation::getTime)
                .collect(Collectors.toSet());
        List<ReservationTime> availableTime = allTimes.stream()
                .filter(time -> !reservedTimes.contains(time))
                .toList();
        return AvailableReservationTimeInfo.from(theme, availableTime);
    }
}
