package by.frostetsky.model;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class PlayerScore {
    private Point points = Point.ZERO;
    private int games = 0;
    private int sets = 0;
    private int tieBreakPoints = 0;
}
