package edu.cnm.deepdive.codebreaker.model.pojo;

import androidx.annotation.NonNull;
import androidx.room.Relation;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import java.util.List;

public class GameWithGuesses extends Game {

  @SuppressWarnings("NotNullFieldNotInitialized")
  @NonNull
  @Relation(
      entity = Guess.class,
      entityColumn = "game_id",
      parentColumn = "game_id"
  )
  private List<Guess> guesses;

  @NonNull
  public List<Guess> getGuesses() {
    return guesses;
  }

  public void setGuesses(@NonNull List<Guess> guesses) {
    this.guesses = guesses;
  }

}
