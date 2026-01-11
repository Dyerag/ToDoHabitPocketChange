package com.example.todohabitpocketchange;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Navigation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Navigation extends Fragment {

    public Navigation() {
        // Required empty public constructor
    }

    public static Navigation newInstance() {
        return new Navigation();
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//        setButtons();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get your buttons from the layout
        Button tasksBtn = view.findViewById(R.id.btnTasks);
        Button habitsBtn = view.findViewById(R.id.btnHabits);
        Button moneyBtn = view.findViewById(R.id.btnMoney);

        // Handle clicks
        tasksBtn.setOnClickListener(v -> openIfNotCurrent(MainActivity.class));
        habitsBtn.setOnClickListener(v -> openIfNotCurrent(HabitTracking.class));
        moneyBtn.setOnClickListener(v -> openIfNotCurrent(PocketMoney.class));
    }

    // Utility method to prevent relaunching the current activity
    private void openIfNotCurrent(Class<?> target) {
        if (!requireActivity().getClass().equals(target)) {
            startActivity(new Intent(getActivity(), target));
        }
    }
}