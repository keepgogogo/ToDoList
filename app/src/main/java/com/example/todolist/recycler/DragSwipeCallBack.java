package com.example.todolist.recycler;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DragSwipeCallBack extends ItemTouchHelper.Callback {

    private PlanShowRecyclerAdapter adapter;

    public DragSwipeCallBack (PlanShowRecyclerAdapter adapter)
    {
        this.adapter=adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        adapter.onItemSwap(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        switch (direction) {
            case ItemTouchHelper.END: // START->END 标记完成事件
                adapter.onItemDone(viewHolder.getAdapterPosition());
                break;
            case ItemTouchHelper.START: // END->START 删除事件
                adapter.onItemDelete(viewHolder.getAdapterPosition());
                break;
            default:
        }
    }



    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
}
