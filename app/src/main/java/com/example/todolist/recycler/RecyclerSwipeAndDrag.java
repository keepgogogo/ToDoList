package com.example.todolist.recycler;

public interface RecyclerSwipeAndDrag {

    void onItemSwap(int fromPosition,int toPosition);

    void onItemDelete(int position);

    void onItemImportance(int position);

}
