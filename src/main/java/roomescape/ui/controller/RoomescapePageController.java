package roomescape.ui.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import roomescape.availability.service.AvailabilityService;
import roomescape.holiday.controller.dto.HolidayResponseDto;
import roomescape.holiday.service.HolidayService;
import roomescape.holiday.service.dto.HolidaySaveServiceDto;
import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;
import roomescape.reservation.controller.dto.ReservationResponseDto;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.dto.ReservationSaveServiceDto;
import roomescape.reservation.service.dto.ReservationUpdateServiceDto;
import roomescape.theme.controller.dto.ThemeAvailableTimeResponseDto;
import roomescape.theme.controller.dto.ThemeResponseDto;
import roomescape.theme.service.ThemeService;
import roomescape.theme.service.dto.ThemeSaveServiceDto;
import roomescape.time.controller.dto.TimeResponseDto;
import roomescape.time.service.TimeService;

@Controller
public class RoomescapePageController {

    private static final Logger log = LoggerFactory.getLogger(RoomescapePageController.class);

    private final ReservationService reservationService;
    private final ThemeService themeService;
    private final TimeService timeService;
    private final HolidayService holidayService;
    private final AvailabilityService availabilityService;

    public RoomescapePageController(
            ReservationService reservationService,
            ThemeService themeService,
            TimeService timeService,
            HolidayService holidayService,
            AvailabilityService availabilityService
    ) {
        this.reservationService = reservationService;
        this.themeService = themeService;
        this.timeService = timeService;
        this.holidayService = holidayService;
        this.availabilityService = availabilityService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard() {
        return "dashboard/index";
    }

    @GetMapping("/dashboard/reservations")
    public String reservationsPage(
            @RequestParam(required = false) String name,
            Model model
    ) {
        List<ReservationResponseDto> reservations = (name == null || name.isBlank()
                ? reservationService.getAll()
                : reservationService.findByName(name))
                .stream()
                .map(ReservationResponseDto::from)
                .toList();
        model.addAttribute("reservations", reservations);
        model.addAttribute("searchName", name);
        model.addAttribute("themes", themeService.getAll().stream().map(ThemeResponseDto::from).toList());
        model.addAttribute("times", timeService.findAll().stream().map(TimeResponseDto::from).toList());
        return "dashboard/reservations";
    }

    @GetMapping("/dashboard/availability")
    public String availabilityPage(
            @RequestParam(required = false) Long availableThemeId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableDate,
            Model model
    ) {
        model.addAttribute("themes", themeService.getAll().stream().map(ThemeResponseDto::from).toList());
        model.addAttribute("selectedAvailableThemeId", availableThemeId);
        model.addAttribute("selectedDate", availableDate);
        model.addAttribute("availableTimes", availableTimes(availableThemeId, availableDate, model));
        return "dashboard/availability";
    }

    @GetMapping("/dashboard/themes")
    public String themesPage(Model model) {
        model.addAttribute("themes", themeService.getAll().stream().map(ThemeResponseDto::from).toList());
        model.addAttribute("bestThemes", themeService.getBestThemes().stream().map(ThemeResponseDto::from).toList());
        return "dashboard/themes";
    }

    @GetMapping("/dashboard/times")
    public String timesPage(Model model) {
        model.addAttribute("times", timeService.findAll().stream().map(TimeResponseDto::from).toList());
        return "dashboard/times";
    }

    @GetMapping("/dashboard/holidays")
    public String holidaysPage(Model model) {
        model.addAttribute("holidays", holidayService.getAll().stream().map(HolidayResponseDto::from).toList());
        return "dashboard/holidays";
    }

    @PostMapping("/dashboard/reservations")
    public String createReservation(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long themeId,
            @RequestParam Long timeId,
            RedirectAttributes redirectAttributes
    ) {
        reservationService.create(new ReservationSaveServiceDto(name, date, themeId, timeId));
        addSuccessMessage(redirectAttributes, "예약을 생성했습니다.");
        return "redirect:/dashboard/reservations";
    }

    @PostMapping("/dashboard/reservations/{id}/update")
    public String updateReservation(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam Long themeId,
            @RequestParam Long timeId,
            RedirectAttributes redirectAttributes
    ) {
        reservationService.update(new ReservationUpdateServiceDto(id, name, date, timeId, themeId));
        addSuccessMessage(redirectAttributes, "예약을 변경했습니다.");
        redirectAttributes.addAttribute("name", name);
        return "redirect:/dashboard/reservations";
    }

    @PostMapping("/dashboard/reservations/{id}/cancel")
    public String cancelReservation(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            RedirectAttributes redirectAttributes
    ) {
        if (name == null || name.isBlank()) {
            reservationService.cancel(id);
        } else {
            reservationService.cancel(id, name);
            redirectAttributes.addAttribute("name", name);
        }
        addSuccessMessage(redirectAttributes, "예약을 취소했습니다.");
        return "redirect:/dashboard/reservations";
    }

    @PostMapping("/dashboard/themes")
    public String createTheme(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String imageUrl,
            RedirectAttributes redirectAttributes
    ) {
        themeService.create(new ThemeSaveServiceDto(name, description, imageUrl));
        addSuccessMessage(redirectAttributes, "테마를 생성했습니다.");
        return "redirect:/dashboard/themes";
    }

    @PostMapping("/dashboard/themes/{id}/delete")
    public String deleteTheme(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        themeService.deleteById(id);
        addSuccessMessage(redirectAttributes, "테마를 삭제했습니다.");
        return "redirect:/dashboard/themes";
    }

    @PostMapping("/dashboard/times")
    public String createTime(
            @RequestParam String startAt,
            @RequestParam String endAt,
            RedirectAttributes redirectAttributes
    ) {
        timeService.create(startAt, endAt);
        addSuccessMessage(redirectAttributes, "시간 슬롯을 생성했습니다.");
        return "redirect:/dashboard/times";
    }

    @PostMapping("/dashboard/times/{id}/delete")
    public String deleteTime(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        timeService.deleteById(id);
        addSuccessMessage(redirectAttributes, "시간 슬롯을 삭제했습니다.");
        return "redirect:/dashboard/times";
    }

    @PostMapping("/dashboard/holidays")
    public String createHoliday(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            RedirectAttributes redirectAttributes
    ) {
        holidayService.create(new HolidaySaveServiceDto(date));
        addSuccessMessage(redirectAttributes, "휴일을 추가했습니다.");
        return "redirect:/dashboard/holidays";
    }

    @PostMapping("/dashboard/holidays/{id}/delete")
    public String deleteHoliday(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        holidayService.delete(id);
        addSuccessMessage(redirectAttributes, "휴일을 삭제했습니다.");
        return "redirect:/dashboard/holidays";
    }

    @ExceptionHandler(RoomescapeException.class)
    public String handleRoomescapeException(
            RoomescapeException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        addExpectedErrorMessage(redirectAttributes, pageErrorMessage(request, e), e);
        addSearchNameIfPresent(request, redirectAttributes);
        return redirectToDashboardPage(request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(
            IllegalArgumentException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        addExpectedErrorMessage(redirectAttributes, pageErrorMessage(request, e), e);
        addSearchNameIfPresent(request, redirectAttributes);
        return redirectToDashboardPage(request);
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(
            RuntimeException e,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        addUnexpectedErrorMessage(redirectAttributes, e);
        addSearchNameIfPresent(request, redirectAttributes);
        return redirectToDashboardPage(request);
    }

    private List<ThemeAvailableTimeResponseDto> availableTimes(Long availableThemeId, LocalDate availableDate, Model model) {
        if (availableThemeId == null || availableDate == null) {
            return List.of();
        }
        try {
            return availabilityService.getAvailableTimes(availableThemeId, availableDate)
                    .stream()
                    .map(ThemeAvailableTimeResponseDto::from)
                    .toList();
        } catch (RoomescapeException e) {
            log.info("Failed to load available times for themeId={} date={}: {}", availableThemeId, availableDate, e.getMessage());
            model.addAttribute("availableTimeErrorMessage", "예약 가능 시간을 조회하지 못했습니다. 조회 조건을 확인해 주세요.");
            return List.of();
        }
    }

    private void addSuccessMessage(RedirectAttributes redirectAttributes, String message) {
        redirectAttributes.addFlashAttribute("successMessage", message);
    }

    private void addExpectedErrorMessage(RedirectAttributes redirectAttributes, RoomescapeException e) {
        log.info("Dashboard request failed: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", e.getErrorCode().getDefaultMessage());
    }

    private void addExpectedErrorMessage(RedirectAttributes redirectAttributes, String userMessage, RuntimeException e) {
        log.info("Dashboard request failed: {}", e.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", userMessage);
    }

    private void addUnexpectedErrorMessage(RedirectAttributes redirectAttributes, RuntimeException e) {
        log.error("Dashboard request failed unexpectedly", e);
        redirectAttributes.addFlashAttribute("errorMessage", ErrorCode.INTERNAL_SERVER_ERROR.getDefaultMessage());
    }

    private String pageErrorMessage(HttpServletRequest request, RoomescapeException e) {
        String requestUri = request.getRequestURI();
        if (requestUri.matches("/dashboard/themes/\\d+/delete")) {
            return "삭제할 테마를 찾지 못했습니다.";
        }
        if (requestUri.matches("/dashboard/holidays/\\d+/delete")) {
            return "삭제할 휴일을 찾지 못했습니다.";
        }
        return e.getErrorCode().getDefaultMessage();
    }

    private String pageErrorMessage(HttpServletRequest request, IllegalArgumentException e) {
        if ("/dashboard/times".equals(request.getRequestURI())) {
            return "시간 슬롯 생성에 실패했습니다. 입력값을 다시 확인해 주세요.";
        }
        return ErrorCode.INTERNAL_SERVER_ERROR.getDefaultMessage();
    }

    private void addSearchNameIfPresent(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (!request.getRequestURI().matches("/dashboard/reservations/\\d+/update")) {
            return;
        }
        String name = request.getParameter("name");
        if (name != null && !name.isBlank()) {
            redirectAttributes.addAttribute("name", name);
        }
    }

    private String redirectToDashboardPage(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        if (requestUri.startsWith("/dashboard/themes")) {
            return "redirect:/dashboard/themes";
        }
        if (requestUri.startsWith("/dashboard/times")) {
            return "redirect:/dashboard/times";
        }
        if (requestUri.startsWith("/dashboard/holidays")) {
            return "redirect:/dashboard/holidays";
        }
        return "redirect:/dashboard/reservations";
    }
}
