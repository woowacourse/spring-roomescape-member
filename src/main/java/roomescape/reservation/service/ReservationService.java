package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.ConflictException;
import roomescape.global.exception.InvalidRequestException;
import roomescape.global.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation create(String name, LocalDate date, Long timeId, Long themeId) {
        ReservationTime time = findTime(timeId);
        Theme theme = findTheme(themeId);

        Reservation reservation = new Reservation(name, date, time, theme);
        if (reservation.isPast(LocalDateTime.now())) {
            throw new InvalidRequestException("현재 시각 이후의 날짜와 시간을 선택해주세요.");
        }
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        )) {
            throw new ConflictException("선택한 날짜와 시간에는 이미 해당 테마의 예약이 있습니다. 다른 시간을 선택해주세요.");
        }

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation updateDateTime(Long id, String name, LocalDate date, Long timeId) {
        Reservation reservation = findReservationByIdAndName(id, name);
        if (reservation.isPast(LocalDateTime.now())) {
            throw new InvalidRequestException("이미 지난 예약은 변경할 수 없습니다.");
        }

        ReservationTime time = findTime(timeId);
        Reservation changedReservation = reservation.changeDateTime(date, time);

        if (changedReservation.isPast(LocalDateTime.now())) {
            throw new InvalidRequestException("현재 시각 이후의 날짜와 시간을 선택해주세요.");
        }
        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                changedReservation.getDate(),
                changedReservation.getTime().getId(),
                changedReservation.getTheme().getId()
        )) {
            throw new ConflictException("선택한 날짜와 시간에는 이미 해당 테마의 예약이 있습니다. 다른 시간을 선택해주세요.");
        }

        return reservationRepository.update(changedReservation)
                .orElseThrow(() -> new NotFoundException("변경할 예약이 존재하지 않습니다. 예약 목록을 확인해주세요."));
    }

    @Transactional(readOnly = true)
    public List<Reservation> findByName(String name) {
        return reservationRepository.findByName(name);
    }

    @Transactional
    public void cancel(Long id, String name) {
        Reservation reservation = findByIdAndName(id, name);
        if (reservation.isPast(LocalDateTime.now())) {
            throw new InvalidRequestException("이미 지난 예약은 취소할 수 없습니다.");
        }
        if (!reservationRepository.deleteById(id)) {
            throw new NotFoundException("삭제할 예약이 존재하지 않습니다. 예약 목록을 확인해주세요.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!reservationRepository.deleteById(id)) {
            throw new NotFoundException("삭제할 예약이 존재하지 않습니다. 예약 목록을 확인해주세요.");
        }
    }

    private ReservationTime findTime(Long timeId) {
        return reservationTimeRepository.findById(timeId)
                .orElseThrow(() -> new NotFoundException("선택한 예약 시간이 존재하지 않습니다. 다른 시간을 선택해주세요."));
    }

    private Theme findTheme(Long themeId){
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("선택한 테마가 존재하지 않습니다. 다른 테마를 선택해주세요."));
    }

    private Reservation findByIdAndName(Long id, String name){
        return reservationRepository.findByIdAndName(id, name)
                .orElseThrow(() -> new NotFoundException("해당 이름으로 예약을 찾을 수 없습니다. 예약 정보를 확인해주세요."));
    }

    private Reservation findReservationByIdAndName(Long id, String name) {
        return reservationRepository.findByIdAndName(id, name)
                .orElseThrow(() -> new NotFoundException("해당 이름으로 예약을 찾을 수 없습니다. 예약 정보를 확인해주세요."));
    }
}
