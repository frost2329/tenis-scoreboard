package by.frostetsky.dto;


import java.util.List;

public record FinishedMatchesResponse(
        List<FinishedMatchDto> matches,
        Long totalCount) {
}
