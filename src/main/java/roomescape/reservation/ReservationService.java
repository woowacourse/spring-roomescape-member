package roomescape.reservation;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.AlreadyInUseException;
import roomescape.exception.ForbiddenException;
import roomescape.exception.InvalidStateException;
import roomescape.exception.NotFoundException;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              Clock clock) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.clock = clock;
    }

    @Transactional
    public ReservationResponse create(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = getReservationTime(reservationRequest);
        Theme theme = getTheme(reservationRequest);

        Reservation reservation = new Reservation(
                reservationRequest.userName(),
                theme,
                reservationRequest.date(),
                reservationTime
        );

        validateNotPast(reservation);
        validateDuplicate(reservation);

        Reservation saved = reservationRepository.save(reservation);
        return ReservationResponse.from(saved);
    }

    public ReservationsResponse read(int page, int size) {
        List<ReservationResponse> reservationsResponse = reservationRepository.findAll(page, size).stream()
                .map(ReservationResponse::from)
                .toList();

        return ReservationsResponse.from(reservationsResponse);
    }

    public ReservationsResponse readByUserName(String userName) {
        List<ReservationResponse> reservationsResponse = reservationRepository.findByUserName(userName).stream()
                .map(ReservationResponse::from)
                .toList();

        return ReservationsResponse.from(reservationsResponse);
    }

    @Transactional
    public ReservationResponse update(long id, ReservationRequest reservationRequest, String userName) {
        Reservation reservation = getReservation(id);

        validateOwner(userName, reservation);
        validateNotPast(reservation);

        ReservationTime reservationTime = getReservationTime(reservationRequest);
        Theme theme = getTheme(reservationRequest);

        Reservation updateReservation = new Reservation(
                id,
                reservation.getUserName(),
                theme,
                reservationRequest.date(),
                reservationTime
        );

        validateNotPast(updateReservation);
        validateDuplicate(updateReservation);

        reservationRepository.update(updateReservation);
        return ReservationResponse.from(updateReservation);
    }

    @Transactional
    public void deleteByAdmin(Long id) {
        Reservation reservation = getReservation(id);

        validateNotPast(reservation);

        reservationRepository.deleteById(id);
    }

    @Transactional
    public void delete(Long id, String userName) {
        Reservation reservation = getReservation(id);

        validateOwner(userName, reservation);
        validateNotPast(reservation);

        reservationRepository.deleteById(id);
    }

    private Reservation getReservation(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다."));
    }

    private Theme getTheme(ReservationRequest reservationRequest) {
        return themeRepository.findById(reservationRequest.themeId())
                .orElseThrow(() -> new NotFoundException("테마를 찾을 수 없습니다."));
    }

    private ReservationTime getReservationTime(ReservationRequest reservationRequest) {
        return reservationTimeRepository.findById(reservationRequest.timeId())
                .orElseThrow(() -> new NotFoundException("예약 시간을 찾을 수 없습니다."));
    }

    private void validateNotPast(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now(clock);

        if (reservation.isPast(now)) {
            throw new InvalidStateException("이미 지난 날짜와 시간입니다.");
        }
    }

    private void validateOwner(String userName, Reservation reservation) {
        if (!reservation.isOwner(userName)) {
            throw new ForbiddenException("본인의 예약만 변경•삭제할 수 있습니다.");
        }
    }

    private void validateDuplicate(Reservation reservation) {
        if (reservationRepository.existsByThemeIdAndDateAndTimeId(
                reservation.getTheme().getId(),
                reservation.getDate(),
                reservation.getTime().getId())
        ) {
            throw new AlreadyInUseException("이미 예약된 테마•날짜•시간입니다.");
        }
    }
}
