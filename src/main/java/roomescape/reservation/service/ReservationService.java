package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.AdminReservationCreateRequest;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.response.ReservationsWithTotalPageResponse;
import roomescape.reservation.dto.response.ReservationsWithTotalPageResponse.BriefReservationElement;
import roomescape.reservation.dto.response.ReservationsWithTotalPageResponse.BriefReservationElement.BriefMemberElement;
import roomescape.reservation.dto.response.ReservationsWithTotalPageResponse.BriefReservationElement.BriefThemeElement;
import roomescape.reservation.dto.response.ReservationsWithTotalPageResponse.BriefReservationElement.BriefTimeElement;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.user.domain.User;
import roomescape.user.domain.UserPrinciple;
import roomescape.user.repository.UserDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final UserDao userDao;

    public ReservationService(ReservationDao reservationDao,
                              ReservationTimeDao reservationTimeDao,
                              ThemeDao themeDao, UserDao userDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.userDao = userDao;
    }

    public ReservationResponse addReservation(ReservationRequest reservationRequest, UserPrinciple userPrinciple) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 시간입니다."));
        Theme theme = themeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 테마입니다."));
        User user = userPrinciple.toEntity();
        Reservation reservation = reservationRequest.toEntityWithReservationTime(reservationTime, theme, user);
        LocalDate reservationDate = reservation.getDate();
        if (!reservationDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("하루 전 까지 예약 가능합니다.");
        }
        if (reservationDao.isExistByThemeIdAndTimeIdAndDate(
                reservationRequest.themeId(),
                reservationRequest.timeId(),
                reservationRequest.date())
        ) {
            throw new BadRequestException("이미 해당 시간에 예약이 존재합니다.");
        }
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    public ReservationResponse addReservation(AdminReservationCreateRequest adminReservationCreateRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(adminReservationCreateRequest.timeId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 시간입니다."));
        Theme theme = themeDao.findById(adminReservationCreateRequest.themeId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 테마입니다."));
        User user = userDao.findById(adminReservationCreateRequest.memberId())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다."));
        LocalDate reservationDate = adminReservationCreateRequest.date();
        if (!reservationDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("하루 전 까지 예약 가능합니다.");
        }
        if (reservationDao.isExistByThemeIdAndTimeIdAndDate(
                adminReservationCreateRequest.themeId(),
                adminReservationCreateRequest.timeId(),
                adminReservationCreateRequest.date())
        ) {
            throw new BadRequestException("이미 해당 시간에 예약이 존재합니다.");
        }
        Reservation reservation = new Reservation(
                null,
                user,
                adminReservationCreateRequest.date(),
                reservationTime,
                theme
        );
        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.fromEntity(savedReservation);
    }

    public void deleteReservation(Long id) {
        boolean isDeleted = reservationDao.deleteById(id);
        if (!isDeleted) {
            throw new ResourceNotFoundException("해당하는 id가 없습니다");
        }
    }

    public ReservationsWithTotalPageResponse getReservationsByPage(int page) {
        int totalReservations = reservationDao.countTotalReservation();
        int totalPage = totalReservations % 10 == 0 ?
                totalReservations / 10 : (totalReservations / 10) + 1;
        if (page < 1 || page > totalPage) {
            throw new ResourceNotFoundException("해당하는 페이지가 없습니다");
        }
        int start = (page - 1) * 10 + 1;
        int end = start + 10 - 1;
        List<BriefReservationElement> briefReservations = reservationDao.findReservationsWithPage(start, end)
                .stream()
                .map(reservation -> new BriefReservationElement(
                        reservation.getId(),
                        new BriefMemberElement(
                                reservation.getUser().getId(),
                                reservation.getUser().getName()
                        ),
                        new BriefThemeElement(
                                reservation.getTheme().getId(),
                                reservation.getTheme().getName()
                        ),
                        new BriefTimeElement(
                                reservation.getReservationTime().getId(),
                                reservation.getReservationTime().getStartAt()
                        ),
                        reservation.getDate()
                ))
                .toList();

        return new ReservationsWithTotalPageResponse(totalPage, briefReservations);
    }
}
