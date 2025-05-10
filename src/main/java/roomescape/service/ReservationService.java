package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.domain.User;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationThemeRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationThemeRepository reservationThemeRepository;
    private final UserRepository userRepository;

    public ReservationResponse create(Long userId, LocalDate date, Long timeId, Long themeId) {
        User user = userRepository.getById(userId);
        ReservationTime reservationTime = reservationTimeRepository.getById(timeId);
        validateReservationTime(date, timeId, themeId, reservationTime);
        ReservationTheme reservationTheme = reservationThemeRepository.getById(themeId);
        Reservation reservation = new Reservation(user, date, reservationTime, reservationTheme);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateReservationTime(LocalDate date, Long timeId, Long themeId, ReservationTime reservationTime) {
        LocalDateTime requestedDateTime = LocalDateTime.of(date, reservationTime.startAt());
        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("이미 지나간 시간으로 예약할 수 없습니다.");
        }
        if (reservationRepository.existDuplicatedDateTime(date, timeId, themeId)) {
            throw new IllegalArgumentException("이미 예약된 시간입니다.");
        }
    }

    public List<ReservationResponse> getAll() {
        List<Reservation> reservations = reservationRepository.getAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void delete(Long id) {
        reservationRepository.findById(id)
                .ifPresent(reservationRepository::remove);
    }

    public List<ReservationResponse> search(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
        ReservationTheme theme = getNullableTheme(themeId);
        User user = getNullableMember(memberId);
        return reservationRepository.findAllByThemeAndUserInDateRange(theme, user, dateFrom, dateTo).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private ReservationTheme getNullableTheme(Long themeId) {
        if (themeId == null) {
            return null;
        }
        return reservationThemeRepository.getById(themeId);
    }

    private User getNullableMember(Long userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.getById(userId);
    }
}
