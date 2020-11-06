package edu.cnm.deepdive.codebreaker.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface UserDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  Single<Long> insert(User user);

  @Update
  Single<Integer> update(User user);

  @Delete
  Single<Integer> delete(User...users);

  @Query("SELECT * FROM user_profile WHERE user_id = :userId")
  LiveData<User> selectById(long userId);

  @Query("SELECT * FROM user_profile WHERE oauth_key = :oauthKey")
  Single<User> selectByOauthKey(String oauthKey);

}
