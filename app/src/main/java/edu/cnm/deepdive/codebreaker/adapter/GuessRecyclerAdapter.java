package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.adapter.GuessRecyclerAdapter.Holder;
import edu.cnm.deepdive.codebreaker.databinding.ItemGuessBinding;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuessRecyclerAdapter extends RecyclerView.Adapter<Holder> {

  private final Context context;
  private final Map<Character, Integer> colorValueMap;
  private final Map<Character, String> colorLabelMap;
  private final List<Guess> guesses;
  private final NumberFormat formatter;
  private final LayoutInflater inflater;

  public GuessRecyclerAdapter(@NonNull Context context,
      @NonNull Map<Character, Integer> colorValueMap,
      @NonNull Map<Character, String> colorLabelMap) {
    this.context = context;
    this.colorValueMap = colorValueMap;
    this.colorLabelMap = colorLabelMap;
    guesses = new ArrayList<>();
    formatter = NumberFormat.getInstance();
    inflater = LayoutInflater.from(context);
  }

  public List<Guess> getGuesses() {
    return guesses;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemGuessBinding binding = ItemGuessBinding.inflate(inflater, parent, false);
    return new Holder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.bind(position);
  }

  @Override
  public int getItemCount() {
    return guesses.size();
  }

  class Holder extends RecyclerView.ViewHolder {

    private final ItemGuessBinding binding;

    private Holder(@NonNull ItemGuessBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    private void bind(int position) {
      Guess guess = guesses.get(position);
      binding.guessNumber.setText(formatter.format(position + 1));
      binding.correct.setText(formatter.format(guess.getCorrect()));
      binding.close.setText(formatter.format(guess.getClose()));
      binding.guessContainer.removeAllViews();
      for (char c : guess.getText().toCharArray()) {
        ImageView swatch =
            (ImageView) inflater.inflate(R.layout.item_swatch, binding.guessContainer, false);
        swatch.setBackgroundColor(colorValueMap.get(c));
        swatch.setContentDescription(colorLabelMap.get(c));
        swatch.setTooltipText(colorLabelMap.get(c));
        binding.guessContainer.addView(swatch);
      }

    }

  }

}
