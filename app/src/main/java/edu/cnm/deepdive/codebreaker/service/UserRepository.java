package edu.cnm.deepdive.codebreaker.service;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.codebreaker.model.dao.UserDao;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;

public class UserRepository {

  private final Context context;
  private final UserDao userDao;
  private final CodebreakerWebService webService;
  private final GoogleSignInService signInService;

  public UserRepository(Context context) {
    this.context = context;
    userDao = CodebreakerDatabase.getInstance().getUserDao();
    webService = CodebreakerWebService.getInstance();
    signInService = GoogleSignInService.getInstance();
  }

  @SuppressWarnings("ConstantConditions")
  public Single<User> createUser(@NonNull GoogleSignInAccount account) {
    return Single.fromCallable(() -> {
      User user = new User();
      user.setDisplayName(account.getDisplayName());
      user.setCreated(new Date());
      user.setOauthKey(account.getId());
      return user;
    })
        .flatMap((user) ->
            userDao.insert(user)
                .map((id) -> {
                  if (id > 0) {
                    user.setId(id);
                  }
                  return user;
                })
        )
        .subscribeOn(Schedulers.io());
  }

  public Single<User> getServerUserProfile() {
    return signInService.refresh()
        .observeOn(Schedulers.io())
        .flatMap((account) -> webService.getProfile(getBearerToken(account.getIdToken()))
            .flatMap((user) -> userDao.selectByOauthKey(account.getId())
                .flatMap((localUser) -> {
                  localUser.setDisplayName(user.getDisplayName());
                  return userDao.update(localUser)
                      .map((count) -> localUser);
                })
            )
        );
  }

  private String getBearerToken(String idToken) {
    return String.format("Bearer %s", idToken);
  }

}
