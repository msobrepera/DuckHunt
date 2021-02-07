package com.ymest.s17_duckhunt.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ymest.s17_duckhunt.R;
import com.ymest.s17_duckhunt.models.User;

import java.util.List;


public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;

    public MyUserRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {




        holder.mItem = mValues.get(position);
        int pos = position + 1;
        holder.textviewPosition.setText(pos + "º");
        holder.textviewDucks.setText(String.valueOf(mValues.get(position).getDucks()));
        holder.textviewNick.setText(mValues.get(position).getNick());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textviewPosition;
        public final TextView textviewDucks;
        public final TextView textviewNick;

        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textviewPosition = view.findViewById(R.id.textViewPosition);
            textviewDucks =  view.findViewById(R.id.textViewDucks);
            textviewNick = view.findViewById(R.id.textViewNick);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textviewNick.getText() + "'";
        }
    }
}