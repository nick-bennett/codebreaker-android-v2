package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.pojo.GuessWithGame;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface GuessDao {

  @Insert
  Single<Long> insert(Guess guess);

  @Insert
  Single<List<Long>> insert(Guess... guesses);

  @Insert
  Single<List<Long>> insert(Collection<Guess> guesses);

  @Delete
  Single<Integer> delete(Guess guess);

  @Delete
  Single<Integer> delete(Guess... guesses);

  @Delete
  Single<Integer> delete(Collection<Guess> guesses);

  @Transaction
  @Query("SELECT * FROM Guess WHERE guess_id = :id")
  LiveData<GuessWithGame> select(long id);

  @Query("SELECT * FROM Guess WHERE game_id = :id ORDER BY submitted ASC")
  LiveData<List<Guess>> selectForGame(long id);

}
