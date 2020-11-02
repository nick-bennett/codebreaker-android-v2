package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.codebreaker.model.view.Score;
import edu.cnm.deepdive.codebreaker.model.pojo.ScoreSummary;
import io.reactivex.Single;
import java.util.Collection;
import java.util.List;

@Dao
public interface ScoreDao {

  @Query("SELECT * FROM Score ORDER BY code_length DESC, guess_count ASC")
  LiveData<List<Score>> selectAll();

  @Query("SELECT * FROM Score WHERE code_length = :codeLength ORDER BY guess_count ASC LIMIT :recordCount")
  LiveData<List<Score>> selectBest(int codeLength, int recordCount);

  @Query("SELECT code_length, AVG(guess_count) AS average_guess_count FROM Score GROUP BY code_length ORDER BY code_length DESC")
  LiveData<List<ScoreSummary>> selectSummaries();

}





