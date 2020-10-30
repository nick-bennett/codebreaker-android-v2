package edu.cnm.deepdive.codebreaker.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import java.util.Date;
import java.util.UUID;

@SuppressWarnings("NotNullFieldNotInitialized")
@Entity(
    indices = {
        @Index(value = {"match_key"}, unique = true)
    }
)
public class Match {

  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "match_id")
  private long id;

  @NonNull
  @ColumnInfo(name = "match_key", typeAffinity = ColumnInfo.BLOB)
  private UUID matchKey;

  @NonNull
  @ColumnInfo(index = true)
  private Date started;

  @NonNull
  @ColumnInfo(index = true)
  private Date deadline;

  @NonNull
  @ColumnInfo(index = true)
  private State state;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public UUID getMatchKey() {
    return matchKey;
  }

  public void setMatchKey(@NonNull UUID matchKey) {
    this.matchKey = matchKey;
  }

  @NonNull
  public Date getStarted() {
    return started;
  }

  public void setStarted(@NonNull Date started) {
    this.started = started;
  }

  @NonNull
  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(@NonNull Date deadline) {
    this.deadline = deadline;
  }

  @NonNull
  public State getState() {
    return state;
  }

  public void setState(@NonNull State state) {
    this.state = state;
  }

  @TypeConverter
  public static Integer stateToInteger(State value) {
    return (value != null) ? value.ordinal() : null;
  }

  @TypeConverter
  public static State integerToState(Integer value) {
    return (value != null) ? State.values()[value] : null;
  }

  public enum State {
    IN_PROGRESS, WON, LOST, FORFEITED;
  }

}
