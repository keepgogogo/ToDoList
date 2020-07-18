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

        holder.timeTextView.setText(getItemDataFromDataBase(position,0));
        holder.importanceTextView.setText(getItemDataFromDataBase(position,1));
        holder.planDetailTextView.setText(getItemDataFromDataBase(position,2));

    }

    @Override
    public int getItemCount()
    {
        return mDataSet.length;
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
                stringBuilder.delete(0,stringBuilder.length()+1);
                stringBuilder.append(elements.year);
                stringBuilder.append("\\");
                stringBuilder.append(elements.month);
                stringBuilder.append("\\");
                stringBuilder.append(elements.date_days);
                stringBuilder.append("  ");
                stringBuilder.append(elements.hour);
                stringBuilder.append(":");
                if(elements.minute<10)stringBuilder.append("0");
                stringBuilder.append(elements.minute);
                break;
            case GET_IMPORTANCE:
                stringBuilder.delete(0,stringBuilder.length()+1);
                if (1==elements.importance)stringBuilder.append("重要事项");
                else stringBuilder.append("非重要事项");
                break;
            case GET_PLAN_DETAIL:
                stringBuilder.delete(0,stringBuilder.length()+1);
                stringBuilder.append(elements.plan);
                break;
            default:
                break;
        }
        return stringBuilder.toString();
    }

    public static class PlanShowRecyclerViewHolder extends RecyclerView.ViewHolder
    {
        public TextView planDetailTextView;
        public TextView importanceTextView;
        public TextView timeTextView;
        public CardView cardView;

        public PlanShowRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.show_all_plan_recycler_card_view);
            timeTextView=itemView.findViewById(R.id.TextViewForRecyclerIn_ShowAllActivity);
            importanceTextView=itemView.findViewById(R.id.TextViewForRecyclerImportanceIn_ShowAllActivity);
            planDetailTextView=itemView.findViewById(R.id.TextViewForRecyclerPlanDetailIn_ShowAllActivity);
        }
    }
}
