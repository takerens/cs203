// package csd.grp3.player;

// import java.util.List;

// import csd.grp3.match.Match;
// import csd.grp3.user.User;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.OneToOne;
// import jakarta.persistence.Table;
// import lombok.*;

// import jakarta.persistence.*;


// @Entity
// @Table(name="Players")
// @Getter
// @Setter
// @ToString
// @AllArgsConstructor
// @NoArgsConstructor

// public class Player{
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private double gamePoints = 0;

//     @OneToMany(mappedBy = "player1")
//     private List<Match> matches;

//     @OneToOne(cascade = CascadeType.ALL)
//     @JoinColumn(name = "user_id", referencedColumnName = "id")
//     private User user;  

//     public Player(User user) {
//         super(user.getUsername(), null); 
//         this.user = user;
//     }
//     public boolean equals(Player player2){
//         return super.equals(player2);
//     }
// }
