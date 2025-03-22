package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CurrentMatch {
    private UUID id;
    private Player player1;
    private Player player2;

    private int ScopePlayer1 = 0;
    private int ScopePlayer2 = 0;

    private boolean finished = false;

    public CurrentMatch(Player player1, Player player2) {
        this.id = UUID.randomUUID();
        this.player1 = player1;
        this.player2 = player2;
    }
}
