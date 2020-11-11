package edu.cnm.deepdive.codebreaker.adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.codebreaker.adapter.GuessRecyclerAdapter.Holder;
import edu.cnm.deepdive.codebreaker.model.entity.Guess;

public class GuessRecyclerAdapter extends RecyclerView.Adapter<Holder> {

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  class Holder extends RecyclerView.ViewHolder {

    private Holder(@NonNull View itemView) {
      super(itemView);
    }

    private void bind(Guess guess) {

    }

  }

}
