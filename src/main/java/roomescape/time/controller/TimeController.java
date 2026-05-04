package roomescape.time.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.time.controller.dto.TimeResponseDto;
import roomescape.time.controller.dto.TimeSaveRequestDto;
import roomescape.time.service.TimeService;

@RestController
public class TimeController {
  private final TimeService timeService;

  public TimeController(TimeService timeService) {
    this.timeService = timeService;
  }

  @PostMapping("/times")
  public TimeResponseDto create(@RequestBody TimeSaveRequestDto request) {
    return TimeResponseDto.from(timeService.create(request.getStartAt()));
  }

  @GetMapping("/times")
  public List<TimeResponseDto> findAll() {
    return timeService.findAll()
        .stream()
        .map(TimeResponseDto::from)
        .collect(Collectors.toList());
  }

  @DeleteMapping("/times/{id}")
  public void deleteById(@PathVariable long id) {
    timeService.deleteById(id);
  }
}