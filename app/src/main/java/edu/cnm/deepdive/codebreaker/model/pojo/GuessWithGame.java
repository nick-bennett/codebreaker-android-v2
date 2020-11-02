package edu.cnm.deepdive.codebreaker.model.pojo;

import androidx.room.Relation;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;

public class GuessWithGame extends Guess {

  @Relation(
      entity = Game.class,
      entityColumn = "game_id",
      parentColumn = "game_id"
  )
  private Game game;

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public boolean isSolution() {
    return getCorrect() == game.getCodeLength();
  }

}
