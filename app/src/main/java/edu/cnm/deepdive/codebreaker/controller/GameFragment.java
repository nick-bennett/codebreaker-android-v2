package edu.cnm.deepdive.codebreaker.controller;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.CodeCharacterAdapter;
import edu.cnm.deepdive.codebreaker.adapter.GuessAdapter;
import edu.cnm.deepdive.codebreaker.adapter.GuessRecyclerAdapter;
import edu.cnm.deepdive.codebreaker.databinding.FragmentGameBinding;
import edu.cnm.deepdive.codebreaker.model.entity.Game;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import edu.cnm.deepdive.codebreaker.viewmodel.MainViewModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameFragment extends Fragment {

  private Map<Character, Integer> colorValueMap;
  private Map<Character, String> colorLabelMap;
  private Character[] codeCharacters;
  private MainViewModel viewModel;
  private GuessRecyclerAdapter adapter;
  private int codeLength;
  private FragmentGameBinding binding;
  private Spinner[] spinners;

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentGameBinding.inflate(getLayoutInflater());
    setupMaps();
    setupViews();
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    setupViewModel();
  }

  private void setupMaps() {
    char[] colorCodes = getString(R.string.color_codes).toCharArray();
    codeCharacters = getString(R.string.color_codes).chars()
        .mapToObj((value) -> (char) value)
        .toArray(Character[]::new);
    Resources resources = getResources();
    int[] colorValues = resources.getIntArray(R.array.color_values);
    String[] colorLabels = resources.getStringArray(R.array.color_labels);
    colorValueMap = buildColorMap(colorCodes, colorValues);
    colorLabelMap = buildLabelMap(colorCodes, colorLabels);
  }

  private void setupViews() {
    binding.submit.setOnClickListener((view) -> recordGuess());
    int maxCodeLength = getResources().getInteger(R.integer.code_length_pref_max);
    spinners = new Spinner[maxCodeLength];
    LayoutInflater inflater = LayoutInflater.from(getContext());
    for (int i = 0; i < maxCodeLength; i++) {
      Spinner spinner =
          (Spinner) inflater.inflate(R.layout.swatch_spinner, binding.guessControls, false);
      CodeCharacterAdapter adapter = new CodeCharacterAdapter(
          getContext(), colorValueMap, colorLabelMap, codeCharacters);
      spinner.setAdapter(adapter);
      spinners[i] = spinner;
      binding.spinners.addView(spinner);
    }
  }

  private void setupViewModel() {
    FragmentActivity activity = getActivity();
    //noinspection ConstantConditions
    adapter = new GuessRecyclerAdapter(activity, colorValueMap, colorLabelMap);
    binding.guessList.setAdapter(adapter);
    viewModel = new ViewModelProvider(activity).get(MainViewModel.class);
    getLifecycle().addObserver(viewModel);
    LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
    viewModel.getGame().observe(lifecycleOwner, this::updateGameDisplay);
    viewModel.getSolved().observe(lifecycleOwner, solved ->
        binding.guessControls.setVisibility(solved ? View.GONE : View.VISIBLE));
    viewModel.getGuesses().observe(lifecycleOwner, this::updateGuessList);
    viewModel.getGuess().observe(lifecycleOwner, (guess) -> {
      if (guess == null) {
        for (Spinner spinner : spinners) {
          spinner.setSelection(0);
        }
      } else {
        char[] characters = guess.getText().toCharArray();
        for (int i = 0; i < characters.length; i++) {
          char c = characters[i];
          for (int position = 0; position < codeCharacters.length; position++) {
            if (codeCharacters[position].equals(c)) {
              spinners[i].setSelection(position);
              break;
            }
          }
        }
      }
    });
  }

  private void updateGameDisplay(Game game) {
    codeLength = game.getCodeLength();
    for (int i = 0; i < spinners.length; i++) {
      spinners[i].setVisibility((i < codeLength) ? View.VISIBLE : View.GONE);
    }
  }

  private void updateGuessList(List<Guess> guesses) {
    adapter.getGuesses().clear();
    adapter.getGuesses().addAll(guesses);
    adapter.notifyItemInserted(guesses.size() - 1);
    //noinspection ConstantConditions
    binding.guessList.getLayoutManager().scrollToPosition(guesses.size() - 1);
  }

  private void recordGuess() {
    StringBuilder builder = new StringBuilder(codeLength);
    for (int i = 0; i < codeLength; i++) {
      builder.append(codeCharacters[spinners[i].getSelectedItemPosition()]);
    }
    viewModel.guess(builder.toString());
  }

  private void startGame() {
    viewModel.startGame();
  }

  private static Map<Character, Integer> buildColorMap(char[] chars, int[] values) {
    Map<Character, Integer> colorMap = new HashMap<>();
    for (int i = 0; i < chars.length; i++) {
      colorMap.put(chars[i], values[i]);
    }
    return colorMap;
  }

  private static Map<Character, String> buildLabelMap(char[] chars, String[] labels) {
    Map<Character, String> labelMap = new HashMap<>();
    for (int i = 0; i < chars.length; i++) {
      labelMap.put(chars[i], labels[i]);
    }
    return labelMap;
  }

}
