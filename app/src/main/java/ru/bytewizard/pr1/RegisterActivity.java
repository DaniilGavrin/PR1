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

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private TextView errorMessageTextView;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameEditText = findViewById(R.id.first_name);
        lastNameEditText = findViewById(R.id.last_name);
        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        errorMessageTextView = findViewById(R.id.error_message);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorMessage("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showErrorMessage("Passwords do not match");
            return;
        }

        sendRegisterRequest(firstName, lastName, username, email, password);
    }

    private void sendRegisterRequest(String firstName, String lastName, String username, String email, String password) {
        OkHttpClient client = new OkHttpClient();

        String url = "http://192.168.31.51:259/register?firstName=" + firstName
                + "&lastName=" + lastName
                + "&username=" + username
                + "&email=" + email
                + "&password=" + password;

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
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();  // Завершить текущую активность
                            });
                        } else {
                            runOnUiThread(() -> showErrorMessage("Registration Failed"));
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
