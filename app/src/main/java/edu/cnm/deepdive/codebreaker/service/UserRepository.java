package edu.cnm.deepdive.codebreaker.service;

import android.content.Context;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import edu.cnm.deepdive.codebreaker.model.dao.UserDao;
import edu.cnm.deepdive.codebreaker.model.entity.User;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

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

  public Single<User> getCurrentUser() {
    return Single.fromCallable(signInService::getAccount)
        .flatMap((account) -> 
            webService.getProfile(getBearerToken(account.getIdToken()))
                .flatMap((user) -> 
                    userDao.selectByOauthKey(account.getId())
                        .switchIfEmpty( 
                            userDao.insert(user)
                                .map((id) -> {
                                  user.setId(id);
                                  return user;
                                })
                        )
                )
                .flatMap((user) -> 
                    userDao.update(user)
                        .map((numRecords) -> user)
                )
        )
        .subscribeOn(Schedulers.io());
  }

  private String getBearerToken(String idToken) {
    return String.format("Bearer %s", idToken);  
  }
  
}
