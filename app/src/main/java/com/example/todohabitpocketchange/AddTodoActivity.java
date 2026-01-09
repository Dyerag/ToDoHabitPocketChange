package com.example.todohabitpocketchange;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddTodoActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_REWARD = "extra_reward";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_todo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editTitle = findViewById(R.id.editTitle);
        EditText editReward = findViewById(R.id.editReward);
        Button saveBtn = findViewById(R.id.btnSaveTodo);

        saveBtn.setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }

            String reward = editReward.getText().toString().trim();
            if (reward.isEmpty()){
                Toast.makeText(this,"Please enter title", Toast.LENGTH_SHORT).show();
            }

            Intent result = new Intent();
            result.putExtra(EXTRA_TITLE, title);
            result.putExtra(EXTRA_REWARD, reward);
            setResult(RESULT_OK, result);
            finish();
        });

    }
}