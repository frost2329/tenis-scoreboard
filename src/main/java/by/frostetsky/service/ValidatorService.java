package by.frostetsky.service;

import by.frostetsky.exception.BadRequestException;

import java.util.UUID;

public class ValidatorService {
    public void validateUUID(String uuid) {
        if (uuid == null) {
            throw new BadRequestException("UUID is empty");
        }
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid UUID " + uuid, e);
        }
    }

    public void validatePlayerNames(String playerName1, String playerName2) {
        validatePlayerName(playerName1, 1);
        validatePlayerName(playerName2, 2);

        if(playerName1.equals(playerName2)) {
            throw new BadRequestException("Players names should be unique");
        }
    }

    public void validatePlayerName(String playerName, int playerNumber) {
        if (playerName == null || playerName.isEmpty()) {
            throw new BadRequestException("Player %d is empty".formatted(playerNumber));
        }
        if (!playerName.matches("^[а-яА-ЯёЁa-zA-Z]+$")) {
            throw new BadRequestException(("Player %d name contains forbidden characters," +
                    "use only letters").formatted(playerNumber));
        }
        if (playerName.length() > 20) {
            throw new BadRequestException("Player %d name is too long".formatted(playerNumber));
        }
        if (playerName.length() < 3) {
            throw new BadRequestException("Player %d name is too short".formatted(playerNumber));
        }
    }
}
