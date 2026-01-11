package com.example.todohabitpocketchange;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PocketMoney extends AppCompatActivity {

    private TextView balanceText;
    private EditText expenseName, expenseCost;
    private LinearLayout expenseListContainer;
    private FrameLayout overlayContainer;
    private ExtendedFloatingActionButton fabShowExpense;

    private boolean overlayVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pocket_money);

        balanceText = findViewById(R.id.balanceText);
        expenseName = findViewById(R.id.expenseName);
        expenseCost = findViewById(R.id.expenseCost);
        expenseListContainer = findViewById(R.id.expenseListContainer);
        overlayContainer = findViewById(R.id.overlayContainer);
        fabShowExpense = findViewById(R.id.fabShowExpense);

        updateBalance();
        displayExpenses();

        // FAB toggles overlay
        fabShowExpense.setOnClickListener(v -> toggleOverlay());

        // Add expense button
        findViewById(R.id.addExpenseBtn).setOnClickListener(v -> addExpense());
    }

    private void toggleOverlay() {
        overlayVisible = !overlayVisible;

        if (overlayVisible) {
            overlayContainer.setVisibility(View.VISIBLE);
            fabShowExpense.setText("Close");

            // Move FAB above overlay (just place it at top of overlay input)
            // Calculate height of expenseInputContainer after layout
            overlayContainer.post(() -> {
                int overlayHeight = findViewById(R.id.expenseInputContainer).getHeight();
                fabShowExpense.setTranslationY((float) (-overlayHeight/1.5)); // 16dp margin
            });

        } else {
            overlayContainer.setVisibility(View.GONE);
            fabShowExpense.setText("Add Expense");
            fabShowExpense.setTranslationY(0);
        }
    }


    private void addExpense() {
        String name = expenseName.getText().toString().trim();
        String costStr = expenseCost.getText().toString().trim();

        if (name.isEmpty() || costStr.isEmpty()) {
            Toast.makeText(this, "Enter both name and cost", Toast.LENGTH_SHORT).show();
            return;
        }

        float cost;
        try {
            cost = Float.parseFloat(costStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid cost", Toast.LENGTH_SHORT).show();
            return;
        }

        // Subtract expense from balance
        MoneyStorage.save(this, -cost);

        // Save expense record
        Expense expense = new Expense(name, cost);
        MoneyStorage.saveExpense(this, expense);

        // Update UI
        updateBalance();
        displayExpenses();

        // Clear fields
        expenseName.setText("");
        expenseCost.setText("");

        // Close overlay
        overlayVisible = false;
        overlayContainer.setVisibility(View.GONE);
        fabShowExpense.setText("Add Expense");
        fabShowExpense.setTranslationY(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBalance();
        displayExpenses();
    }

    private void updateBalance() {
        float balance = MoneyStorage.load(this);
        balanceText.setText("Balance: " + balance + " kr.");
    }

    private void displayExpenses() {
        expenseListContainer.removeAllViews();
        ArrayList<Expense> expenses = MoneyStorage.loadExpenses(this);

        for (Expense e : expenses) {
            TextView expenseItem = new TextView(this);
            expenseItem.setText(e.getName() + ": " + e.getCost() + " kr");
            expenseListContainer.addView(expenseItem);
        }
    }
}
