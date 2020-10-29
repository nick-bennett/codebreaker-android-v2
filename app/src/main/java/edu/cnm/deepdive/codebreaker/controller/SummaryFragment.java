package edu.cnm.deepdive.codebreaker.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import edu.cnm.deepdive.codebreaker.adapter.ScoreSummaryAdapter;
import edu.cnm.deepdive.codebreaker.databinding.FragmentSummaryBinding;
import edu.cnm.deepdive.codebreaker.viewmodel.MainViewModel;

public class SummaryFragment extends Fragment {

  private FragmentSummaryBinding binding;

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentSummaryBinding.inflate(inflater);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    FragmentActivity activity = getActivity();
    //noinspection ConstantConditions
    MainViewModel viewModel = new ViewModelProvider(activity).get(MainViewModel.class);
    viewModel.getSummaries().observe(getViewLifecycleOwner(), (summaries) -> {
      ScoreSummaryAdapter adapter = new ScoreSummaryAdapter(activity, summaries);
      binding.summaryList.setAdapter(adapter);
    });
  }

}