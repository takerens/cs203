package csd.grp3.tournament;

import jakarta.persistence.Entity;

import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Tournament {
    private String title;
}
