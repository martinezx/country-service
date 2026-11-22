package org.xmdf.countryservice.adapter.in.web.controller;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.xmdf.countryservice.adapter.in.web.openapi.ApiTags;
import org.xmdf.countryservice.application.model.RefreshExecution;
import org.xmdf.countryservice.application.port.in.RefreshDataUseCase;

@RestController
@Tag(name = ApiTags.DATA_REFRESH)
@RequestMapping(value = "/jobs/data-refresh", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataRefreshController {

  private final RefreshDataUseCase refreshData;

  public DataRefreshController(RefreshDataUseCase refreshData) {
    this.refreshData = refreshData;
  }

  @Operation(
      operationId = "startDataRefresh",
      summary = "Start a data refresh",
      description =
          "Starts the refresh asynchronously and returns immediately. Only one refresh can run at a"
              + " time.")
  @PostMapping
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ApiResponse(responseCode = "202", description = "The refresh has been started.")
  @ApiResponse(
      responseCode = "409",
      description = "A data refresh is already running.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
              schema = @Schema(implementation = ProblemDetail.class)))
  public DataRefreshResponse trigger() {
    RefreshExecution execution = refreshData.refresh();
    return new DataRefreshResponse(execution.id(), execution.status());
  }

  /**
   * Response body of {@code POST /jobs/data-refresh}.
   *
   * @param jobExecutionId the identifier of the refresh that was started
   * @param status the status of the refresh at the moment it was started, for example {@code
   *     STARTING}
   */
  public record DataRefreshResponse(
      @Schema(description = "Identifier of the refresh that was started", requiredMode = REQUIRED)
          Long jobExecutionId,
      @Schema(
              description = "Status of the refresh at the moment it was started",
              example = "STARTING",
              requiredMode = REQUIRED)
          String status) {}
}
