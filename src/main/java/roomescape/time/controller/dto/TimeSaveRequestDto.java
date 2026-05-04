package roomescape.time.controller.dto;

public class TimeSaveRequestDto {
  private String startAt;
  private String endAt;

  public String getStartAt() {
    return startAt;
  }

  public void setStartAt(String startAt) {
    this.startAt = startAt;
  }

  public String getEndAt() {
    return endAt;
  }

  public void setEndAt(String endAt) {
    this.endAt = endAt;
  }
}

