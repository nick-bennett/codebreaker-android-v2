package edu.cnm.deepdive.codebreaker.service;

import android.content.Context;
import edu.cnm.deepdive.codebreaker.model.dao.MatchDao;
import edu.cnm.deepdive.codebreaker.model.entity.Match;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MatchRepository {

  private final Context context;
  private final CodebreakerWebService webService;
  private final MatchDao matchDao;
  private final GoogleSignInService signInService;


  public MatchRepository(Context context) {
    this.context = context;
    webService = CodebreakerWebService.getInstance();
    signInService = GoogleSignInService.getInstance();
    matchDao = CodebreakerDatabase.getInstance().getMatchDao();
  }

  public Single<Match> add(Match match) {
    return signInService.refresh()
        .observeOn(Schedulers.io())
        .flatMap((account) -> webService.startMatch(getBearerToken(account.getIdToken()), match))
        .flatMap((responseMatch) -> matchDao.insert(responseMatch)
            .map((id) -> {
              responseMatch.setId(id);
              return responseMatch;
            })
        );
  }

  private String getBearerToken(String idToken) {
    return String.format("Bearer %s", idToken);
  }

}
