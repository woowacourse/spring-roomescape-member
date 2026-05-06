package roomescape.reservation.service;

import static roomescape.reservation.domain.ReservationStatus.CANCELED;
import static roomescape.reservation.domain.ReservationStatus.RESERVED;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.ConflictException;
import roomescape.common.exception.NotFoundException;
import roomescape.date.domain.ReservationDate;
import roomescape.date.repository.ReservationDateRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.request.ReservationSaveDto;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.response.ThemeDetailDto;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationDateRepository reservationDateRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ReservationDateRepository reservationDateRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationDateRepository = reservationDateRepository;
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

        ReservationDate reservationDate = reservationDateRepository.findById(dto.dateId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약 날짜입니다."));

        Theme theme = themeRepository.findById(dto.themeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));

        validateNotAlreadyBookedByOthers(reservationDate.date(), reservationTime.startAt(), theme);
        validateUserHasNoReservationAtSameTime(dto.name(), reservationDate, reservationTime);
        Long id = reservationRepository.save(
                Reservation.create(dto.name(), reservationDate.date(), reservationTime.startAt(), theme));

        return new ReservationResponse(
                id,
                dto.name(),
                reservationDate.date(),
                reservationTime.startAt(),
                ThemeDetailDto.from(theme),
                RESERVED // TODO: save 반환값 Reservation으로 수정
        );
    }

    private void validateNotAlreadyBookedByOthers(LocalDate date, LocalTime time, Theme theme) {
        if (reservationRepository.existsByDateAndTimeAndThemeId(date, time, theme.id())) {
            throw new ConflictException("해당 날짜/시간/테마는 이미 예약되었습니다.");
        }
    }

    private void validateUserHasNoReservationAtSameTime(String name, ReservationDate date, ReservationTime time) {
        if (reservationRepository.existsByNameAndDateAndTime(name, date.date(), time.startAt())) {
            throw new ConflictException("동일한 날짜와 시간에 예약이 존재합니다.");
        }
    }

    @Transactional
    public ReservationResponse delete(Long id) {
        Reservation reservation = getReservation(id);
        reservationRepository.delete(id);
        return ReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse cancel(Long id) {
        Reservation reservation = getReservation(id);
        reservation.updateStatus(CANCELED);
        reservationRepository.updateStatus(reservation);
        return ReservationResponse.from(reservation);
    }

    private Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 예약입니다."));
    }

}
