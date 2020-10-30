package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import edu.cnm.deepdive.codebreaker.model.entity.Match;
import edu.cnm.deepdive.codebreaker.model.pojo.MatchWithGames;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface MatchDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<Long> insert(Match match);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(Match... matches);

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<List<Long>> insert(Collection<Match> matches);

  @Update
  Single<Integer> update(Match match);

  @Update
  Single<Integer> update(Match... matches);

  @Update
  Single<Integer> update(Collection<Match> matches);

  @Delete
  Single<Integer> delete(Match match);

  @Delete
  Single<Integer> delete(Match... matches);

  @Delete
  Single<Integer> delete(Collection<Match> matches);

  // TODO Verify correct date processing.
  @Query("SELECT * FROM `Match` WHERE state = 0 AND deadline > DATETIME('now', 'unixepoch')")
  LiveData<List<Match>> selectInProgress();

  @Query("SELECT * FROM `Match` WHERE state = :state ORDER BY started DESC, deadline DESC")
  LiveData<List<Match>> selectByState(Match.State state);

  @Transaction
  @Query("SELECT * FROM `Match` WHERE match_id = :id")
  LiveData<MatchWithGames> select(long id);

}
