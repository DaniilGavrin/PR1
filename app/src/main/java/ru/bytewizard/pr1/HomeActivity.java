package ru.bytewizard.pr1;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private StoryAdapter adapter;
    private List<Story> storyList = new ArrayList<>();

    private final String SERVER_URL = "http://192.168.31.51:259";  // Адрес сервера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Инициализация RecyclerView и EmptyView
        recyclerView = findViewById(R.id.recycler_view);
        emptyView = findViewById(R.id.empty_view);

        int numberOfColumns = 3; // Устанавливаем количество столбцов
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        // Загрузка данных
        loadStories();
    }

    private void loadStories() {
        OkHttpClient client = new OkHttpClient();

        // Запрос к серверу
        Request request = new Request.Builder()
                .url(SERVER_URL + "/stories")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(HomeActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                    showEmptyView();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    // Парсинг JSON-ответа
                    Gson gson = new Gson();
                    Type storyListType = new TypeToken<ArrayList<Story>>() {}.getType();
                    storyList = gson.fromJson(responseBody, storyListType);

                    runOnUiThread(() -> {
                        if (storyList.isEmpty()) {
                            showEmptyView();
                        } else {
                            showStories();
                        }
                    });
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(HomeActivity.this, "Ошибка сервера", Toast.LENGTH_SHORT).show();
                        showEmptyView();
                    });
                }
            }
        });
    }

    private void showEmptyView() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    private void showStories() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);

        adapter = new StoryAdapter(this, storyList, story -> {
            // Открытие истории при клике
            Toast.makeText(HomeActivity.this, "Открытие истории: " + story.getTitle(), Toast.LENGTH_SHORT).show();
            // Здесь можно добавить Intent для перехода на другой экран с подробной информацией
        });

        recyclerView.setAdapter(adapter);
    }
}
