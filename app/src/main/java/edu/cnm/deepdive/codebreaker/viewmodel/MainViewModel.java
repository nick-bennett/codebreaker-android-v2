package edu.cnm.deepdive.codebreaker.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;
import androidx.preference.PreferenceManager;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.pojo.ScoreSummary;
import edu.cnm.deepdive.codebreaker.service.GameRepository;
import edu.cnm.deepdive.codebreaker.service.UserRepository;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  public static final String POOL = "ROYGBIV";

  private final UserRepository userRepository;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<Guess> guess;
  private final MutableLiveData<Boolean> solved;
  private final LiveData<List<Guess>> guesses;
  private final MutableLiveData<Throwable> throwable;
  private final Random rng;
  private final String codeLengthPrefKey;
  private final int codeLengthPrefDefault;
  private final SharedPreferences preferences;
  private final GameRepository repository;
  private final CompositeDisposable pending;

  public MainViewModel(@NonNull Application application) {
    super(application);
    userRepository = new UserRepository(application);
    repository = new GameRepository(application);
    game = new MutableLiveData<>();
    guess = new MutableLiveData<>();
    solved = new MutableLiveData<>();
    guesses = Transformations.switchMap(game, repository::getGuesses);
    throwable = new MutableLiveData<>();
    rng = new SecureRandom();
    codeLengthPrefKey = application.getString(R.string.code_length_pref_key);
    codeLengthPrefDefault =
        application.getResources().getInteger(R.integer.code_length_pref_default);
    preferences = PreferenceManager.getDefaultSharedPreferences(application);
    pending = new CompositeDisposable();
    startGame();
    userRepository.getServerUserProfile()
        .subscribe(
            (user) -> Log.d(getClass().getSimpleName(), user.getDisplayName())
        );
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<Guess> getGuess() {
    return guess;
  }

  public LiveData<List<Guess>> getGuesses() {
    return guesses;
  }

  public LiveData<Boolean> getSolved() {
    return solved;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void startGame() {
    throwable.setValue(null);
    int codeLength = preferences.getInt(codeLengthPrefKey, codeLengthPrefDefault);
    pending.add(
        repository.newGame(POOL, codeLength, rng)
            .doAfterSuccess((game) -> {
              guess.postValue(null);
              solved.postValue(false);
            })
            .subscribe(
                game::postValue,
                throwable::postValue
            )
    );
  }

  public void guess(String text) {
    Game game = this.game.getValue();
    throwable.setValue(null);
    Disposable disposable = repository.guess(game, text)
        .doAfterSuccess((guess) -> {
          //noinspection ConstantConditions
          if (guess.getCorrect() == game.getCodeLength()) {
            solved.postValue(true);
          }
        })
        .subscribe(
            guess::postValue,
            throwable::postValue
        );
    pending.add(disposable);
  }

  public LiveData<List<ScoreSummary>> getSummaries() {
    return repository.getSummaries();
  }

  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

}
