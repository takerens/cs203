// package csd.grp3.player;

// import java.util.List;

// import csd.grp3.match.Match;
// import csd.grp3.user.User;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.Table;
// import lombok.*;


// @Entity
// @Table(name="Players")
// @Getter
// @Setter
// @ToString
// @AllArgsConstructor
// @NoArgsConstructor

// public class Player extends User{
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private int gamePoints = 0;

//     @OneToMany(mappedBy = "player1")
//     private List<Match> matches;

//     public boolean equals(Player player2){
//         return super.equals(player2);
//     }

//     public Integer getELO() {
//         return super.getELO();
//     }
// }
