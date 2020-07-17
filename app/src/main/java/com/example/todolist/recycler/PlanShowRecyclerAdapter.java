package com.example.todolist.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.PlanElements;
import com.example.todolist.R;

public class PlanShowRecyclerAdapter extends RecyclerView.Adapter<PlanShowRecyclerAdapter
        .PlanShowRecyclerViewHolder> {

    private PlanElements[] mDataSet;

    public PlanShowRecyclerAdapter(PlanElements[] mDataSet)
    {
        this.mDataSet=mDataSet;
    }

    @Override
    public PlanShowRecyclerAdapter.PlanShowRecyclerViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View itemView=(View) LayoutInflater.from(parent.getContext()).inflate(R.layout.show_all_plan_recycler_view,
                parent,false);
        return new PlanShowRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlanShowRecyclerViewHolder holder,int position)
    {
        String timeOutput=getItemDataFromDataBase(position);
        holder.timeTextView.setText(timeOutput);

        String importanceOutput=
    }

    public String getItemDataFromDataBase(int position,int statement)
    {
        final int GET_TIME=0;
        final int GET_IMPORTANCE=1;
        final int GET_PLAN_DETAIL=2;
        StringBuilder stringBuilder=new StringBuilder();
        PlanElements elements=mDataSet[position];
        switch (statement)
        {
            case GET_TIME:
                stringBuilder.append(elements.getYear());
                stringBuilder.append("\\");
                stringBuilder.append(elements.getMonth());
                stringBuilder.append("\\");
                stringBuilder.append(elements.getDate_days());
                stringBuilder.append("  ");
                stringBuilder.append(elements.getHour());
                stringBuilder.append(":");
                stringBuilder.append(elements.getMinute());
                break;
            case GET_IMPORTANCE:
                if (1==elements.getImportance())stringBuilder.append("重要事项");
                else stringBuilder.append("非重要事项");
                break;
            case GET_PLAN_DETAIL:
                break;
            default:
                break;
        }
        return stringBuilder.toString();
    }

    public static class PlanShowRecyclerViewHolder extends RecyclerView.ViewHolder
    {
        public TextView importanceTextView;
        public TextView timeTextView;
        public CardView cardView;

        public PlanShowRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.show_all_plan_recycler_card_view);
            timeTextView=itemView.findViewById(R.id.TextViewForRecyclerIn_ShowAllActivity);
            importanceTextView=itemView.findViewById(R.id.TextViewForRecyclerImportanceIn_ShowAllActivity);
        }
    }
}
