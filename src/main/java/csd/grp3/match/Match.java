package csd.grp3.match;

import csd.grp3.round.Round;
import csd.grp3.user.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="Matches")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "round_id", nullable = false)
    private Round round;

    private User white;
    private User black;
    private boolean isBYE = false;

    @Getter (AccessLevel.NONE)
    private Integer result = 0;

    /**
     * Get result of match. Returns 
     *  -1 (black wins),
     *  1 (white wins),
     *  0.5 (draw),
     *  0 (unplayed/ongoing)
     * @return Integer value
     */
    public Integer getResult() {
        return result;
    }
}
