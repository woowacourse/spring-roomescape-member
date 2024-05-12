package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import roomescape.domain.*;
import roomescape.dto.request.ReservationAdminCreateRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.repository.UserRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationTimeRepository reservationTimeRepository;
    private final ThemeRepository themeRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ReservationTimeRepository reservationTimeRepository,
                              ThemeRepository themeRepository,
                              UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationTimeRepository = reservationTimeRepository;
        this.themeRepository = themeRepository;
        this.userRepository = userRepository;
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<AvailableTimeResponse> findByDateAndThemeId(LocalDate date, Long themeId) {
        List<Long> foundReservations = reservationRepository.findByDateAndThemeId(date, themeId)
                .stream()
                .map(reservation -> reservation.getReservationTime().getId())
                .toList();
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        List<AvailableTimeResponse> availableTimeResponses = new ArrayList<>();
        for (ReservationTime reservationTime : reservationTimes) {
            availableTimeResponses.add(AvailableTimeResponse.from(
                    reservationTime.getId(),
                    reservationTime.getStartAt(),
                    foundReservations.contains(reservationTime.getId())
            ));
        }
        return availableTimeResponses;
    }

    public ReservationResponse create(ReservationCreateRequest reservationCreateRequest, User user) {
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(reservationCreateRequest.timeId());
        validateAvailableDateTime(reservationCreateRequest.date(), reservationTime.getStartAt());
        Theme theme = themeRepository.findByThemeId(reservationCreateRequest.themeId());
        Reservation reservation = new Reservation(
                user,
                reservationCreateRequest.date(),
                reservationTime,
                theme
        );
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public ReservationResponse createAdminReservation(ReservationAdminCreateRequest reservationAdminCreateRequest) {
        ReservationTime reservationTime = reservationTimeRepository.findByTimeId(reservationAdminCreateRequest.timeId());
        validateAvailableDateTime(reservationAdminCreateRequest.date(), reservationTime.getStartAt());
        Theme theme = themeRepository.findByThemeId(reservationAdminCreateRequest.themeId());
        User user = userRepository.findById(reservationAdminCreateRequest.userId());
        Reservation reservation = new Reservation(
                user,
                reservationAdminCreateRequest.date(),
                reservationTime,
                theme
        );
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateAvailableDateTime(LocalDate date, LocalTime time) {
        LocalDate nowDate = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalTime nowTime = LocalTime.now(ZoneId.of("Asia/Seoul"));

        if (date.isBefore(nowDate) || (date.isEqual(nowDate) && time.isBefore(nowTime))) {
            throw new IllegalArgumentException("예약 가능한 시간이 아닙니다.");
        }
    }

    public void delete(Long id) {
        if (reservationRepository.deleteById(id) == 0) {
            throw new IllegalArgumentException("삭제할 예약이 존재하지 않습니다");
        }
    }
}
