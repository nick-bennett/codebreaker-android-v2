package edu.cnm.deepdive.codebreaker.service;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import edu.cnm.deepdive.codebreaker.model.dao.GameDao;
import edu.cnm.deepdive.codebreaker.model.dao.GuessDao;
import edu.cnm.deepdive.codebreaker.model.dao.MatchDao;
import edu.cnm.deepdive.codebreaker.model.dao.ScoreDao;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.entity.Match;
import edu.cnm.deepdive.codebreaker.model.view.Score;
import edu.cnm.deepdive.codebreaker.service.CodebreakerDatabase.Converters;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

@Database(
    entities = {Match.class, Game.class, Guess.class},
    views = {Score.class},
    version = 1,
    exportSchema = true
)
@TypeConverters({Converters.class, Match.State.class})
public abstract class CodebreakerDatabase extends RoomDatabase {

  private static final String DB_NAME = "codebreaker_db";

  private static Application context;

  public static void setContext(Application context) {
    CodebreakerDatabase.context = context;
  }

  public static CodebreakerDatabase getInstance() {
    return InstanceHolder.INSTANCE;
  }

  public abstract ScoreDao getScoreDao();

  public abstract MatchDao getMatchDao();

  public abstract GameDao getGameDao();

  public abstract GuessDao getGuessDao();

  private static class InstanceHolder {

    private static final CodebreakerDatabase INSTANCE =
        Room.databaseBuilder(context, CodebreakerDatabase.class, DB_NAME)
            .build();

  }

  public static class Converters {

    @TypeConverter
    public static Long dateToLong(Date value) {
      return (value != null) ? value.getTime() : null;
    }

    @TypeConverter
    public static Date longToDate(Long value) {
      return (value != null) ? new Date(value) : null;
    }

    @TypeConverter
    public static byte[] uuidToBytes(UUID value) {
      byte[] bytes = null;
      if (value != null) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(value.getMostSignificantBits())
            .putLong(value.getLeastSignificantBits());
        bytes = buffer.array();
      }
      return bytes;
    }

    @TypeConverter
    public static UUID bytesToUUID(byte[] value) {
      UUID uuid = null;
      if (value != null) {
        ByteBuffer buffer = ByteBuffer.wrap(value);
        uuid = new UUID(buffer.getLong(), buffer.getLong());
      }
      return uuid;
    }

  }

}
