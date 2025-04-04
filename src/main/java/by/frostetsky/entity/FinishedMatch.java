package by.frostetsky.entity;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "matches")
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
