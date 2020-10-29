package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import java.util.Date;
import java.util.UUID;

@Entity(
    indices = {
        @Index(value = {"guess_key"}, unique = true),
        @Index({"game_id", "submitted"})
    }
)
public class Guess {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "guess_id")
  private long id;

  @ColumnInfo(name = "game_id", index = true)
  private long gameId;

  @ColumnInfo(name = "guess_key")
  private UUID guessKey;

  @NonNull
  private Date submitted = new Date();

  @NonNull
  private String text = "";

  private int correct;

  private int close;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getGameId() {
    return gameId;
  }

  public void setGameId(long gameId) {
    this.gameId = gameId;
  }

  public UUID getGuessKey() {
    return guessKey;
  }

  public void setGuessKey(UUID guessKey) {
    this.guessKey = guessKey;
  }

  @NonNull
  public Date getSubmitted() {
    return submitted;
  }

  public void setSubmitted(@NonNull Date submitted) {
    this.submitted = submitted;
  }

  @NonNull
  public String getText() {
    return text;
  }

  public void setText(@NonNull String text) {
    this.text = text;
  }

  public int getCorrect() {
    return correct;
  }

  public void setCorrect(int correct) {
    this.correct = correct;
  }

  public int getClose() {
    return close;
  }

  public void setClose(int close) {
    this.close = close;
  }

}
