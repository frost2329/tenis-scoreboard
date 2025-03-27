package by.frostetsky.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PlayerScore {
    private int sets = 0;
    private int games = 0;
    private int points = 0;
}
