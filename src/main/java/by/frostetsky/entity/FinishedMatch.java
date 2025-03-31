package by.frostetsky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class FinishedMatch implements BaseEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "first_player_id")
    private Player firstPlayer;
    @ManyToOne
    @JoinColumn(name = "second_player_id")
    private Player secondPlayer;
    @ManyToOne
    @JoinColumn(name = "winner_player_id")
    private Player winner;
}
