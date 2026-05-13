package roomescape.domain.reservation.service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.global.exception.BadRequestException;
import roomescape.domain.global.exception.ConflictException;
import roomescape.domain.global.exception.ErrorCode;
import roomescape.domain.global.exception.ErrorDetail;
import roomescape.domain.global.exception.ForbiddenException;
import roomescape.domain.global.exception.NotFoundException;
import roomescape.domain.global.exception.UnprocessableEntityException;
import roomescape.domain.reservation.dto.request.ReservationCreateRequestDto;
import roomescape.domain.reservation.dto.request.ReservationUpdateRequestDto;
import roomescape.domain.reservation.dto.response.ReservationCreateResponseDto;
import roomescape.domain.reservation.dto.response.ReservationResponseDto;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.validator.ReservationValidator;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

@Service
public class ReservationService {

    private final Clock clock;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(Clock clock, ReservationRepository reservationRepository,
        TimeRepository timeRepository,
        ThemeRepository themeRepository) {
        this.clock = clock;
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservations();
        return convertReservationsToDto(reservations);
    }

    public List<ReservationResponseDto> getReservationsByName(String name) {
        ReservationValidator.validate(name);
        List<Reservation> reservations = reservationRepository.findReservationsByName(name);
        return convertReservationsToDto(reservations);
    }

    private List<ReservationResponseDto> convertReservationsToDto(List<Reservation> reservations) {
        return reservations.stream()
            .map(ReservationResponseDto::from)
            .toList();
    }

    public ReservationCreateResponseDto saveReservation(ReservationCreateRequestDto requestDto) {
        Reservation reservation = createReservation(requestDto);
        validateDuplicates(requestDto.date(), requestDto.timeId(), requestDto.themeId());
        return ReservationCreateResponseDto.from(reservationRepository.save(reservation));
    }

    private void validateDuplicates(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateTimeAndThemeId(date, timeId, themeId)) {
            throw new ConflictException(ErrorCode.RESERVATION_DUPLICATE);
        }
    }

    private Reservation createReservation(ReservationCreateRequestDto requestDto) {
        ReservationValidator.validate(requestDto.name());

        Time time = timeRepository.findTimeById(requestDto.timeId())
            .orElseThrow(() -> new BadRequestException(ErrorCode.TIME_NOT_FOUND, List.of()));
        Theme theme = themeRepository.findThemeById(requestDto.themeId())
            .orElseThrow(() -> new BadRequestException(ErrorCode.THEME_NOT_FOUND, List.of()));
        return Reservation.create(requestDto.name(), requestDto.date(), time, theme, clock);
    }

    public void updateReservation(String name, Long id, ReservationUpdateRequestDto requestDto) {
        Reservation reservation = getReservationById(id);
        validateOwner(name, reservation);
        validateTimeExists(requestDto.timeId());
        validateDuplicatesExceptMe(id, requestDto.date(), requestDto.timeId(),
            reservation.getTheme().getId());
        validateDateUpdatable(reservation);
        validateDateTimeChangeable(requestDto.date(), requestDto.timeId());

        reservationRepository.updateReservationById(id, requestDto.date(), requestDto.timeId());
    }

    private Reservation getReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findReservationById(id);
        if (reservation.isEmpty()) {
            throw new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        return reservation.get();
    }

    private void validateTimeExists(Long timeId) {
        if (!timeRepository.existsById(timeId)) {
            throw new BadRequestException(ErrorCode.COMMON_INVALID_REQUEST_BODY,
                List.of(ErrorDetail.of("timeId", timeId, "요청한 시간 id가 존재하지 않습니다.")));
        }
    }

    private void validateDateUpdatable(Reservation reservation) {
        if (reservation.isPast(clock)) {
            throw new UnprocessableEntityException(ErrorCode.RESERVATION_ALREADY_PASSED);
        }
    }

    private void validateDuplicatesExceptMe(Long id, LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateTimeAndThemeIdExceptId(id, date, timeId, themeId)) {
            throw new ConflictException(ErrorCode.RESERVATION_DUPLICATE);
        }
    }

    private void validateDateTimeChangeable(LocalDate date, Long timeId) {
        LocalDate nowDate = LocalDate.now(clock);
        Time time = timeRepository.findTimeById(timeId).get();
        if (date.isBefore(LocalDate.now(clock)) || (date.isEqual(nowDate) && time.isPast(clock))) {
            throw new UnprocessableEntityException(ErrorCode.RESERVATION_TIME_ALREADY_PASSED);
        }
    }

    public void deleteReservationById(Long id) {
        if (reservationRepository.deleteReservationById(id) == 0) {
            throw new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    public void deleteMemberReservationById(String name, Long id) {
        Reservation reservation = getReservationById(id);
        validateOwner(name, reservation);
        reservationRepository.deleteReservationById(id);
    }

    private void validateOwner(String name, Reservation reservation) {
        if (!reservation.isOwner(name)) {
            throw new ForbiddenException(ErrorCode.RESERVATION_FORBIDDEN);
        }
    }
}
