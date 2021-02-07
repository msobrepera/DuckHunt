package com.ymest.s17_duckhunt.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.ymest.s17_duckhunt.R;
import com.ymest.s17_duckhunt.models.User;

import java.util.ArrayList;
import java.util.List;


public class UserRankingFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    List<User> userList;
    MyUserRecyclerViewAdapter adapter;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    Integer duracion = 0;
    Bundle data;


    public UserRankingFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UserRankingFragment newInstance(int columnCount) {

        UserRankingFragment fragment = new UserRankingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();

        data = this.getArguments();
        if(data != null) duracion = data.getInt("duracion");
        else duracion = 0;



       if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_user_ranking_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            db.collection("users")
                    .whereEqualTo("tipoPartida", duracion)
                    .orderBy("ducks", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            userList = new ArrayList<>();
                            for(DocumentSnapshot document:task.getResult()){
                                User userItem = document.toObject(User.class);
                                userList.add(userItem);
                                adapter = new MyUserRecyclerViewAdapter(userList);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });

        }
        return view;
    }
}