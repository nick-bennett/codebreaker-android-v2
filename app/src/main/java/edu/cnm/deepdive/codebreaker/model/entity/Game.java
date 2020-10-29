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
        @Index(value = {"game_key"}, unique = true)
    }
)
public class Game {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "game_id")
  private long id;

  private String code;

  @ColumnInfo(name = "code_length", index = true)
  private int codeLength;

  @ColumnInfo(name = "match_key", index = true)
  private UUID matchKey;

  @ColumnInfo(name = "game_key")
  private UUID gameKey;

  @NonNull
  @ColumnInfo(index = true)
  private Date started = new Date();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getCodeLength() {
    return codeLength;
  }

  public void setCodeLength(int codeLength) {
    this.codeLength = codeLength;
  }

  public UUID getMatchKey() {
    return matchKey;
  }

  public void setMatchKey(UUID matchKey) {
    this.matchKey = matchKey;
  }

  public UUID getGameKey() {
    return gameKey;
  }

  public void setGameKey(UUID gameKey) {
    this.gameKey = gameKey;
  }

  @NonNull
  public Date getStarted() {
    return started;
  }

  public void setStarted(@NonNull Date started) {
    this.started = started;
  }

}
