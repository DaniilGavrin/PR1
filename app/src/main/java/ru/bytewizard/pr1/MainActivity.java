package ru.bytewizard.pr1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView errorMessageTextView;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        errorMessageTextView = findViewById(R.id.error_message);
        loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

        // Обработка нажатия на кнопку регистрации
        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void authenticateUser() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please fill in all fields");
            return;
        }

        sendLoginRequest(username, password);
    }

    private void sendLoginRequest(String username, String password) {
        OkHttpClient client = new OkHttpClient();

        String url = "http://192.168.31.51:259/login?username=" + username + "&password=" + password;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> showErrorMessage("Network error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        if (responseBody.equals("ok")) {
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();  // Завершение текущей активности
                            });
                        } else {
                            runOnUiThread(() -> showErrorMessage("Authorization Failed"));
                        }
                    } else {
                        runOnUiThread(() -> showErrorMessage("Server error: " + response.code()));
                    }
                } catch (IOException e) {
                    runOnUiThread(() -> showErrorMessage("Response parsing error: " + e.getMessage()));
                } catch (Exception e) {
                    runOnUiThread(() -> showErrorMessage("Unexpected error: " + e.getMessage()));
                }
            }
        });
    }

    private void showErrorMessage(String message) {
        errorMessageTextView.setText(message);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }
}