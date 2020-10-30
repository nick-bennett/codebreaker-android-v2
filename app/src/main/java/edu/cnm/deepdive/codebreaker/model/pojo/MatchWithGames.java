package edu.cnm.deepdive.codebreaker.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Match;
import java.util.List;

public class MatchWithGames extends Match {

  @Relation(
      entity = Game.class,
      parentColumn = "match_id",
      entityColumn = "match_id"
  )
  private List<Game> games;

  public List<Game> getGames() {
    return games;
  }

  public void setGames(List<Game> games) {
    this.games = games;
  }

}
