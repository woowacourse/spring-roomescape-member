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
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DeletionNotAllowedException;
import roomescape.exception.exception.DuplicateReservationException;
import roomescape.exception.exception.PastReservationTimeException;
import roomescape.repository.RoomescapeRepository;
import roomescape.repository.RoomescapeThemeRepository;
import roomescape.repository.RoomescapeTimeRepository;

@Service
public class RoomescapeService {

    private static final int POPULAR_RESERVATION_DAYS_CRITERIA = 7;

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
        List<ReservationTheme> popularReservationThemes = roomescapeThemeRepository
                .findTopThemeOrderByCountWithinDaysDesc(POPULAR_RESERVATION_DAYS_CRITERIA);
        return popularReservationThemes.stream().map(ReservationThemeResponse::of).toList();
    }

    public ReservationResponse addReservation(final ReservationRequest request) {
        Reservation reservation = toReservation(request);

        validateFutureDateTime(reservation);
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
        if (!roomescapeRepository.deleteById(id)) {
            throw new DataNotFoundException(String.format("[ERROR] 예약번호 %d번에 해당하는 예약이 없습니다.", id));
        }
    }

    public void removeReservationTime(final long timeId) {
        if (roomescapeRepository.existsByTimeId(timeId)) {
            throw new DeletionNotAllowedException("[ERROR] 예약이 연결된 시간은 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.");
        }
        if (!roomescapeTimeRepository.deleteById(timeId)) {
            throw new DataNotFoundException(String.format("[ERROR] 예약 시간 %d번에 해당하는 시간이 없습니다.", timeId));
        }
    }

    public void removeReservationTheme(final long themeId) {
        if (roomescapeRepository.existsByThemeId(themeId)) {
            throw new DeletionNotAllowedException("[ERROR] 예약이 연결된 테마는 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.");
        }
        if (!roomescapeThemeRepository.deleteById(themeId)) {
            throw new DataNotFoundException(String.format("[ERROR] 예약 테마 %d번애 해당하는 테마가 없습니다.", themeId));
        }
    }

    private Reservation toReservation(final ReservationRequest request) {
        ReservationTime time = findTimeById(request.timeId());
        ReservationTheme theme = findThemeById(request.themeId());
        return new Reservation(request.name(), request.date(), time, theme);
    }

    private void validateFutureDateTime(final Reservation reservation) {
        LocalDateTime requestDateTime = reservation.toDateTime();
        if (!requestDateTime.isAfter(LocalDateTime.now())) {
            throw new PastReservationTimeException("[ERROR] 현재 시각 이후로 예약해 주세요.");
        }
    }

    private void validateUniqueReservation(final Reservation reservation) {
        if (existsSameReservation(reservation)) {
            throw new DuplicateReservationException("[ERROR] 이미 존재하는 예약입니다. 다른 시간을 선택해 주세요.");
        }
    }

    private ReservationTheme findThemeById(final long themeId) {
        return roomescapeThemeRepository.findById(themeId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("[ERROR] 예약 테마 %d번애 해당하는 테마가 없습니다.", themeId)));
    }

    private ReservationTime findTimeById(final long timeId) {
        return roomescapeTimeRepository.findById(timeId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("[ERROR] 예약 시간 %d번에 해당하는 시간이 없습니다.", timeId)));
    }

    private boolean existsSameReservation(final Reservation reservation) {
        return roomescapeRepository.existsByDateAndTime(reservation.getDate(), reservation.getTime());
    }
}
