package roomescape.time.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<TimeResponseDto> create(@RequestBody TimeSaveRequestDto request) {
    TimeResponseDto body = TimeResponseDto.from(timeService.create(request.getStartAt(), request.getEndAt()));
    return ResponseEntity.status(HttpStatus.CREATED).body(body);
  }

  @GetMapping("/times")
  public ResponseEntity<List<TimeResponseDto>> findAll() {
    List<TimeResponseDto> body = timeService.findAll()
        .stream()
        .map(TimeResponseDto::from)
        .collect(Collectors.toList());
    return ResponseEntity.ok(body);
  }

  @DeleteMapping("/times/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    timeService.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}