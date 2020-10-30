package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.util.TableInfo.Column;
import java.util.Date;
import java.util.UUID;

@Entity(
    indices = {
        @Index(value = {"game_key"}, unique = true)
    },
    foreignKeys = {
        @ForeignKey(
            entity = Match.class,
            parentColumns = {"match_id"}, childColumns = {"match_id"},
            onDelete = ForeignKey.CASCADE
        )
    }
)
public class Game {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "game_id")
  private long id;

  @ColumnInfo(name = "game_key", typeAffinity = ColumnInfo.BLOB)
  private UUID gameKey;

  @ColumnInfo(name = "match_id", index = true)
  private Long matchId;

  private String code;

  @ColumnInfo(name = "code_length", index = true)
  private int codeLength;

  @NonNull
  @ColumnInfo(index = true)
  private Date started = new Date();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public UUID getGameKey() {
    return gameKey;
  }

  public void setGameKey(UUID gameKey) {
    this.gameKey = gameKey;
  }

  public Long getMatchId() {
    return matchId;
  }

  public void setMatchId(Long matchId) {
    this.matchId = matchId;
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

  @NonNull
  public Date getStarted() {
    return started;
  }

  public void setStarted(@NonNull Date started) {
    this.started = started;
  }
}
