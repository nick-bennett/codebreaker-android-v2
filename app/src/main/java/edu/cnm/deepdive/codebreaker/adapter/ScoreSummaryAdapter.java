package edu.cnm.deepdive.codebreaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ItemSummaryBinding;
import edu.cnm.deepdive.codebreaker.model.pojo.ScoreSummary;
import java.util.List;

public class ScoreSummaryAdapter extends ArrayAdapter<ScoreSummary> {

  private final LayoutInflater inflater;
  private final String averageGuessesFormat;

  public ScoreSummaryAdapter(@NonNull Context context, @NonNull List<ScoreSummary> objects) {
    super(context, R.layout.item_summary, objects);
    inflater = LayoutInflater.from(context);
    averageGuessesFormat = context.getString(R.string.average_guesses_format);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ItemSummaryBinding binding = (convertView != null)
        ? ItemSummaryBinding.bind(convertView)
        : ItemSummaryBinding.inflate(inflater, parent, false);
    ScoreSummary summary = getItem(position);
    //noinspection ConstantConditions
    binding.codeLength.setText(String.valueOf(summary.getCodeLength()));
    binding.averageGuesses.setText(
        String.format(averageGuessesFormat, summary.getAverageGuessCount()));
    return binding.getRoot();
  }

}
