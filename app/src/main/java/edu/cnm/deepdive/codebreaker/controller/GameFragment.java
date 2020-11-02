package edu.cnm.deepdive.codebreaker.controller;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.CodeCharacterAdapter;
import edu.cnm.deepdive.codebreaker.adapter.GuessAdapter;
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
  private GuessAdapter adapter;
  private int codeLength;
  private FragmentGameBinding binding;
  private Spinner[] spinners;
  private NavController navController;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

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
    setupNavigation(view);
    setupViewModel();
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.game_options, menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    boolean handled = true;
    switch (item.getItemId()) {
      case R.id.new_game:
        startGame();
        break;
      case R.id.settings:
        navController.navigate(R.id.action_navigation_game_to_navigation_settings);
        break;
      default:
        handled = super.onOptionsItemSelected(item);
    }
    return handled;
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
    binding.summary.setOnClickListener((view) ->
        navController.navigate(R.id.action_navigation_game_to_navigation_summary));
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

  private void setupNavigation(View root) {
    navController = Navigation.findNavController(root);
  }

  private void setupViewModel() {
    FragmentActivity activity = getActivity();
    //noinspection ConstantConditions
    adapter = new GuessAdapter(activity, colorValueMap, colorLabelMap);
    viewModel = new ViewModelProvider(activity).get(MainViewModel.class);
    getLifecycle().addObserver(viewModel);
    LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
    viewModel.getGame().observe(lifecycleOwner, this::updateGameDisplay);
    viewModel.getSolved().observe(lifecycleOwner, solved ->
        binding.guessControls.setVisibility(solved ? View.INVISIBLE : View.VISIBLE));
    viewModel.getGuesses().observe(lifecycleOwner, this::updateGuessList);
  }

  private void updateGameDisplay(Game game) {
    codeLength = game.getCodeLength();
    for (int i = 0; i < spinners.length; i++) {
      spinners[i].setVisibility((i < codeLength) ? View.VISIBLE : View.GONE);
    }
  }

  private void updateGuessList(List<Guess> guesses) {
    adapter.clear();
    adapter.addAll(guesses);
    binding.guessList.setAdapter(adapter);
    binding.guessList.setSelection(adapter.getCount() - 1);
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
