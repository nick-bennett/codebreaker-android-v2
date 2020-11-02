package edu.cnm.deepdive.codebreaker.model.view;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@SuppressWarnings("NotNullFieldNotInitialized")
@DatabaseView("SELECT "
    + "gm.game_id, gm.pool, gm.code, gm.code_length, gm.started, s.submitted, gs.guess_count "
    + "FROM Game AS gm "
    + "INNER JOIN Guess AS s ON s.game_id = gm.game_id AND s.correct = gm.code_length "
    + "INNER JOIN (SELECT game_id, COUNT(*) AS guess_count FROM Guess GROUP BY game_id) AS gs ON gs.game_id = gm.game_id")
public class Score {

  @ColumnInfo(name = "game_id")
  private long id;

  @NonNull
  private String pool;

  @NonNull
  private String code;

  @ColumnInfo(name = "code_length")
  private int codeLength;

  @NonNull
  private Date started;

  @NonNull
  private Date submitted;

  @ColumnInfo(name = "guess_count")
  private int guessCount;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public String getPool() {
    return pool;
  }

  public void setPool(@NonNull String pool) {
    this.pool = pool;
  }

  @NonNull
  public String getCode() {
    return code;
  }

  public void setCode(@NonNull String code) {
    this.code = code;
  }

  public int getCodeLength() {
    return codeLength;
  }

  public void setCodeLength(int codeLength) {
    this.codeLength = codeLength;
  }

  @NonNull
  public Date getStarted() {
    return started;
  }

  public void setStarted(@NonNull Date started) {
    this.started = started;
  }

  @NonNull
  public Date getSubmitted() {
    return submitted;
  }

  public void setSubmitted(@NonNull Date submitted) {
    this.submitted = submitted;
  }

  public int getGuessCount() {
    return guessCount;
  }

  public void setGuessCount(int guessCount) {
    this.guessCount = guessCount;
  }

}
