package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.dto.ReservationThemeRequest;
import roomescape.dto.ReservationThemeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.repository.RoomescapeRepository;
import roomescape.repository.RoomescapeThemeRepository;
import roomescape.repository.RoomescapeTimeRepository;

@Service
public class RoomescapeService {

    public static final int DELETE_FAILED_COUNT = 0;

    private final RoomescapeRepository roomescapeRepository;
    private final RoomescapeTimeRepository roomescapeTimeRepository;
    private final RoomescapeThemeRepository roomescapeThemeRepository;

    public RoomescapeService(final RoomescapeRepository roomescapeRepository,
                             final RoomescapeTimeRepository roomescapeTimeRepository,
                             final RoomescapeThemeRepository roomescapeThemeRepository) {
        this.roomescapeRepository = roomescapeRepository;
        this.roomescapeTimeRepository = roomescapeTimeRepository;
        this.roomescapeThemeRepository = roomescapeThemeRepository;
    }

    public List<ReservationResponse> findReservations() {
        List<Reservation> reservations = roomescapeRepository.findAll();
        return reservations.stream().map(ReservationResponse::of).toList();
    }

    public List<ReservationTimeResponse> findReservationTimes() {
        List<ReservationTime> reservationTimes = roomescapeTimeRepository.findAll();
        return reservationTimes.stream().map(ReservationTimeResponse::of).toList();
    }

    public List<ReservationThemeResponse> findReservationThemes() {
        List<ReservationTheme> reservationThemes = roomescapeThemeRepository.findAll();
        return reservationThemes.stream().map(ReservationThemeResponse::of).toList();
    }

    public List<ReservationThemeResponse> findPopularReservations() {
        List<ReservationTheme> popularReservationThemes = roomescapeThemeRepository.findPopularThemes();
        return popularReservationThemes.stream().map(ReservationThemeResponse::of).toList();
    }

    public ReservationResponse addReservation(final ReservationRequest request) {
        LocalDateTime now = LocalDateTime.now();
        long timeId = request.timeId();
        final long themeId = request.themeId();

        ReservationTime time = roomescapeTimeRepository.findById(timeId);
        ReservationTheme theme = roomescapeThemeRepository.findById(themeId);
        Reservation reservation = new Reservation(request.name(), request.date(), time, theme);
        LocalDateTime requestDateTime = LocalDateTime.of(request.date(), time.getStartAt());

        validateFutureDateTime(requestDateTime, now);
        validateUniqueReservation(reservation);

        Reservation saved = roomescapeRepository.save(reservation);
        return ReservationResponse.of(saved);
    }

    public ReservationTimeResponse addReservationTime(final ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(request.startAt());
        ReservationTime saved = roomescapeTimeRepository.save(reservationTime);
        return ReservationTimeResponse.of(saved);
    }

    public ReservationThemeResponse addReservationTheme(final ReservationThemeRequest request) {
        ReservationTheme reservationTheme = new ReservationTheme(request.name(), request.description(),
                request.thumbnail());
        ReservationTheme saved = roomescapeThemeRepository.save(reservationTheme);
        return ReservationThemeResponse.of(saved);
    }

    public void removeReservation(final long id) {
        int deleteCounts = roomescapeRepository.deleteById(id);
        if (deleteCounts == DELETE_FAILED_COUNT) {
            throw new IllegalArgumentException(String.format("[ERROR] 예약번호 %d번은 존재하지 않습니다.", id));
        }
    }

    public void removeReservationTime(final long id) {
        int deleteCounts = roomescapeTimeRepository.deleteById(id);
        if (deleteCounts == DELETE_FAILED_COUNT) {
            throw new IllegalArgumentException(String.format("[ERROR] 예약시간 %d번은 존재하지 않습니다.", id));
        }
    }

    public void removeReservationTheme(final long id) {
        int deleteCounts = roomescapeThemeRepository.deleteById(id);
        if (deleteCounts == DELETE_FAILED_COUNT) {
            throw new IllegalArgumentException(String.format("[ERROR] 예약테마 %d번은 존재하지 않습니다.", id));
        }
    }

    private void validateFutureDateTime(final LocalDateTime requestDateTime, final LocalDateTime now) {
        if (requestDateTime.isBefore(now) || requestDateTime.isEqual(now)) {
            throw new IllegalArgumentException("[ERROR] 이전 시각으로 예약할 수 없습니다.");
        }
    }

    private void validateUniqueReservation(final Reservation reservation) {
        if (existsSameReservation(reservation)) {
            throw new IllegalArgumentException("[ERROR] 이미 존재하는 예약시간입니다.");
        }
    }

    private boolean existsSameReservation(final Reservation reservation) {
        // 방법1
        // return roomescapeRepository.existsByDateAndTime(reservation.getDate(), reservation.getTime());
        // 방법2
        List<Reservation> reservations = roomescapeRepository.findByDate(reservation.getDate());
        return reservations.stream()
                .anyMatch(candidate -> candidate.isDuplicateReservation(reservation));
    }
}
