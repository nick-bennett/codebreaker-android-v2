package edu.cnm.deepdive.codebreaker.controller;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.cnm.deepdive.codebreaker.R;
import edu.cnm.deepdive.codebreaker.databinding.ActivityLoginBinding;
import edu.cnm.deepdive.codebreaker.service.GoogleSignInService;

public class LoginActivity extends AppCompatActivity {

  private static final int LOGIN_REQUEST_CODE = 1000;

  private GoogleSignInService service;
  private ActivityLoginBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    service = GoogleSignInService.getInstance();
    service.refresh()
        .addOnSuccessListener((account) -> {/* TODO Switch to MainActivity. */})
        .addOnFailureListener((throwable) -> {
          binding = ActivityLoginBinding.inflate(getLayoutInflater());
          binding.signIn.setOnClickListener((v) -> {/* TODO Start login process. */});
          setContentView(R.layout.activity_login);
        });
  }
}