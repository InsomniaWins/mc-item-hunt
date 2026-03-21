package wins.insomnia.mcitemhunt.model.dto;

public record RunSummary(
        Long runId,
        String playerId,
        String worldSeed,
        Long startTime,
        RunVerificationStatus verificationStatus
) {}
