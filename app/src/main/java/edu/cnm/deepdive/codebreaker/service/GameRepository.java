package edu.cnm.deepdive.codebreaker.service;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import edu.cnm.deepdive.codebreaker.model.dao.GuessDao;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.model.dao.GameDao;
import edu.cnm.deepdive.codebreaker.model.dao.ScoreDao;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.pojo.ScoreSummary;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GameRepository {

  private static final String ILLEGAL_LENGTH_FORMAT =
      "Invalid guess length: code length is %1$d; guess length is %2$d.";
  private static final String ILLEGAL_CHARACTER_FORMAT =
      "Guess includes invalid characters: pool is \"%1$s\"; guess includes \"%2$s\".";
  private static final String VALID_CHARACTER_PATTERN_FORMAT = "[%s]";

  private final Context context;
  private final ScoreDao scoreDao;
  private final GameDao gameDao;
  private final GuessDao guessDao;

  public GameRepository(Context context) {
    this.context = context;
    CodebreakerDatabase database = CodebreakerDatabase.getInstance();
    scoreDao = database.getScoreDao();
    gameDao = database.getGameDao();
    guessDao = database.getGuessDao();
  }

  public Single<Game> newGame(String pool, int codeLength, Random rng) {
    return Single.fromCallable(() -> createGame(pool, codeLength, rng))
        .subscribeOn(Schedulers.computation())
        .flatMap((game) -> gameDao.insert(game)
            .map((id) -> {
              game.setId(id);
              return game;
            })
        )
        .subscribeOn(Schedulers.io());
  }

  public Single<Guess> guess(Game game, String text) {
    // TODO Check if game is a solitaire (local-only) game, or a game from a match.
    return Single.fromCallable(() -> {
      validateGuess(game, text);
      Map<Character, Set<Integer>> letterMap = getLetterMap(text);
      char[] work = game.getCode().toCharArray();
      Guess guess = new Guess();
      guess.setGameId(game.getId());
      guess.setText(text);
      guess.setCorrect(getCorrect(letterMap, work));
      guess.setClose(getClose(letterMap, work));
      return guess;
    })
        .subscribeOn(Schedulers.computation())
        .flatMap((guess) -> guessDao.insert(guess)
            .map((id) -> {
              guess.setId(id);
              return guess;
            })
        )
        .subscribeOn(Schedulers.io());
  }

  public LiveData<List<Guess>> getGuesses(Game game) {
    return guessDao.selectForGame(game.getId());
  }

  public LiveData<List<ScoreSummary>> getSummaries() {
    return scoreDao.selectSummaries();
  }

  private int getClose(Map<Character, Set<Integer>> letterMap, char[] work) {
    int close = 0;
    for (char letter : work) {
      if (letter != 0) {
        Set<Integer> positions = letterMap.getOrDefault(letter, Collections.emptySet());
        if (!positions.isEmpty()) {
          close++;
          Iterator<Integer> iter = positions.iterator();
          iter.next();
          iter.remove();
        }
      }
    }
    return close;
  }

  private int getCorrect(Map<Character, Set<Integer>> letterMap, char[] work) {
    int correct = 0;
    for (int i = 0; i < work.length; i++) {
      char letter = work[i];
      Set<Integer> positions = letterMap.getOrDefault(letter, Collections.emptySet());
      if (positions.contains(i)) {
        correct++;
        positions.remove(i);
        work[i] = 0;
      }
    }
    return correct;
  }

  @NonNull
  private Map<Character, Set<Integer>> getLetterMap(String text) {
    Map<Character, Set<Integer>> letterMap = new HashMap<>();
    char[] letters = text.toCharArray();
    for (int i = 0; i < letters.length; i++) {
      char letter = letters[i];
      Set<Integer> positions = letterMap.getOrDefault(letter, new HashSet<>());
      positions.add(i);
      letterMap.putIfAbsent(letter, positions);
    }
    return letterMap;
  }

  private void validateGuess(Game game, String text) {
    if (text.length() != game.getCodeLength()) {
      throw new IllegalArgumentException(
          String.format(ILLEGAL_LENGTH_FORMAT, game.getCodeLength(), text.length()));
    }
    String badCharacters =
        text.replaceAll(String.format(VALID_CHARACTER_PATTERN_FORMAT, game.getPool()), "");
    if (!badCharacters.isEmpty()) {
      throw new IllegalArgumentException(
          String.format(ILLEGAL_CHARACTER_FORMAT, game.getPool(), badCharacters));
    }
  }

  @NonNull
  private Game createGame(String pool, int codeLength, Random rng) {
    Game game = new Game();
    game.setCodeLength(codeLength);
    game.setPool(pool);
    StringBuilder builder = new StringBuilder(codeLength);
    int length = pool.length();
    for (int i = 0; i < codeLength; i++) {
      builder.append(pool.charAt(rng.nextInt(length)));
    }
    game.setCode(builder.toString());
    return game;
  }

}
