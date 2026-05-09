package roomescape.reservation.service;

import static roomescape.reservation.domain.ReservationStatus.CANCELED;
import static roomescape.reservation.domain.ReservationStatus.RESERVED;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.closeddate.repository.ClosedDateRepository;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.response.ThemeDetailDto;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Slf4j
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ClosedDateRepository closedDateRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ClosedDateRepository closedDateRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.closedDateRepository = closedDateRepository;
        this.themeRepository = themeRepository;
    }


    @Transactional(readOnly = true)
    public List<ReservationResponse> readAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> readAllByName(String name) {
        return reservationRepository.findAllByNameOrderByDateAndTime(name).stream()
                .map(ReservationResponse::from)
                .toList();
    }

    @Transactional
    public ReservationResponse create(ReservationSaveDto dto) {
        ReservationTime reservationTime = reservationTimeRepository.findById(dto.timeId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 시간입니다."));

        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new NotFoundException("해당 테마가 존재하지 않습니다."));

        if (closedDateRepository.existsByDate(dto.date())) {
            throw new IllegalArgumentException("예약 불가능한 날짜입니다.");
        }

        validateNotAlreadyBookedByOthers(dto.date(), reservationTime.startAt(), theme);
        validateUserHasNoReservationAtSameTime(dto.name(), dto.date(), reservationTime);

        Long id = reservationRepository.save(
                Reservation.create(dto.name(), dto.date(), reservationTime.startAt(), theme));

        log.info("Reservation created: name={}, date={}", dto.name(), dto.date());

        return new ReservationResponse(
                id,
                dto.name(),
                dto.date(),
                reservationTime.startAt(),
                ThemeDetailDto.from(theme),
                RESERVED
        );
    }

    private void validateNotAlreadyBookedByOthers(LocalDate date, LocalTime time, Theme theme) {
        if (reservationRepository.existsByDateAndTimeAndThemeId(date, time, theme.id())) {
            throw new ConflictException("해당 날짜/시간/테마는 이미 예약되었습니다.");
        }
    }

    private void validateUserHasNoReservationAtSameTime(String name, LocalDate date, ReservationTime time) {
        if (reservationRepository.existsByNameAndDateAndTime(name, date, time.startAt())) {
            throw new ConflictException("동일한 날짜와 시간에 예약이 존재합니다.");
        }
    }

    @Transactional
    public ReservationResponse cancel(Long id) {
        Reservation reservation = getReservation(id);
        reservation.updateStatus(CANCELED);
        reservationRepository.updateStatus(reservation);
        log.info("Reservation canceled: id={}, name={}, date={}, time={}, theme={}", reservation.id(), reservation.name(), reservation.date(), reservation.time(), reservation.theme().name());
        return ReservationResponse.from(reservation);
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
    }

}
