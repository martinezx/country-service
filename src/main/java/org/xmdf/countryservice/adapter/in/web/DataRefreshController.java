package org.xmdf.countryservice.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xmdf.countryservice.application.model.RefreshExecution;
import org.xmdf.countryservice.application.port.in.RefreshDataUseCase;

@RestController
@RequestMapping("/jobs/data-refresh")
public class DataRefreshController {

  private final RefreshDataUseCase refreshData;

  public DataRefreshController(RefreshDataUseCase refreshData) {
    this.refreshData = refreshData;
  }

  @PostMapping
  public ResponseEntity<DataRefreshResponse> trigger() {
    RefreshExecution execution = refreshData.refresh();
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(new DataRefreshResponse(execution.id(), execution.status()));
  }

  /**
   * Response body of {@code POST /jobs/data-refresh}.
   *
   * @param jobExecutionId the identifier of the refresh that was started
   * @param status the status of the refresh at the moment it was started, for example {@code
   *     STARTING}
   */
  public record DataRefreshResponse(Long jobExecutionId, String status) {}
}
