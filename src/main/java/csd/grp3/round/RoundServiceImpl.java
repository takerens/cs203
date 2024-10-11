package csd.grp3.round;

public class RoundServiceImpl implements RoundService {
    private RoundRepository rounds;

    public RoundServiceImpl (RoundRepository rounds) {
        this.rounds = rounds;
    }

    @Override
    public Round getRound(long id) {
        return rounds.findById(id)
            .orElseThrow(() -> new RoundNotFoundException());
    }
}
