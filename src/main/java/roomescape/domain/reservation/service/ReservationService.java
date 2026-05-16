package roomescape.domain.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.global.exception.custom.BusinessException;
import roomescape.domain.global.exception.error.ErrorCode;
import roomescape.domain.global.exception.error.ErrorDetail;
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

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
        TimeRepository timeRepository,
        ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllReservations();
        return convertReservationsToDto(reservations);
    }

    @Transactional
    public List<ReservationResponseDto> getReservationsByName(String name) {
        List<Reservation> reservations = reservationRepository.findReservationsByName(name);
        return convertReservationsToDto(reservations);
    }

    private List<ReservationResponseDto> convertReservationsToDto(List<Reservation> reservations) {
        return reservations.stream()
            .map(ReservationResponseDto::from)
            .toList();
    }

    @Transactional
    public ReservationCreateResponseDto saveReservation(ReservationCreateRequestDto requestDto, LocalDateTime now) {
        Reservation reservation = createReservation(requestDto, now);
        validateDuplicates(requestDto.date(), requestDto.timeId(), requestDto.themeId());
        return ReservationCreateResponseDto.from(reservationRepository.save(reservation));
    }

    private void validateDuplicates(LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateTimeAndThemeId(date, timeId, themeId)) {
            throw new BusinessException(ErrorCode.RESERVATION_DUPLICATE);
        }
    }

    private Reservation createReservation(ReservationCreateRequestDto requestDto,
        LocalDateTime now) {
        Time time = timeRepository.findTimeById(requestDto.timeId())
            .orElseThrow(() -> new BusinessException(ErrorCode.TIME_NOT_FOUND, List.of()));
        Theme theme = themeRepository.findThemeById(requestDto.themeId())
            .orElseThrow(() -> new BusinessException(ErrorCode.THEME_NOT_FOUND, List.of()));
        return Reservation.create(requestDto.name(), requestDto.date(), time, theme, now);
    }

    @Transactional
    public void updateReservation(String name, Long id, ReservationUpdateRequestDto requestDto,
        LocalDateTime now) {
        Reservation reservation = getReservationById(id);
        ReservationValidator.validateOwner(name, reservation);
        Time time = getTimeById(requestDto.timeId());
        validateDuplicatesExceptMe(id, requestDto.date(), requestDto.timeId(),
            reservation.getTheme().getId());
        ReservationValidator.validateDateAccessable(reservation, now);
        ReservationValidator.validateDateTimeChangeable(requestDto.date(), time, now);

        reservationRepository.updateReservationById(id, requestDto.date(), requestDto.timeId());
    }

    private Reservation getReservationById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findReservationById(id);
        if (reservation.isEmpty()) {
            throw new BusinessException(ErrorCode.RESERVATION_NOT_FOUND);
        }
        return reservation.get();
    }

    private Time getTimeById(Long timeId) {
        return timeRepository.findTimeById(timeId)
            .orElseThrow(() -> new BusinessException(ErrorCode.COMMON_INVALID_REQUEST_BODY,
                List.of(ErrorDetail.of("timeId", timeId, "요청한 시간 id가 존재하지 않습니다."))));
    }

    private void validateDuplicatesExceptMe(Long id, LocalDate date, Long timeId, Long themeId) {
        if (reservationRepository.existsByDateTimeAndThemeIdExceptId(id, date, timeId, themeId)) {
            throw new BusinessException(ErrorCode.RESERVATION_DUPLICATE);
        }
    }

    @Transactional
    public void deleteReservationById(Long id) {
        if (reservationRepository.deleteReservationById(id) == 0) {
            throw new BusinessException(ErrorCode.RESERVATION_NOT_FOUND);
        }
    }

    @Transactional
    public void deleteMemberReservationById(String name, Long id, LocalDateTime now) {
        Reservation reservation = getReservationById(id);
        ReservationValidator.validateOwner(name, reservation);
        ReservationValidator.validateDateAccessable(reservation, now);
        reservationRepository.deleteReservationById(id);
    }
}
