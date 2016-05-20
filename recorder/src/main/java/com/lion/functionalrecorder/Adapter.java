package com.lion.functionalrecorder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by lion on 5/16/16.
 */
public class Adapter extends RecyclerView.Adapter<RecordHolder> {

    private ArrayList<Item> items;
    private RecordHolder.ItemClicked itemClicked;

    public Adapter(RecordHolder.ItemClicked itemClicked) {
        this.items = new ArrayList<>();
        this.itemClicked = itemClicked;
    }

    @Override
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false);
        return new RecordHolder(v, itemClicked);
    }

    @Override
    public void onBindViewHolder(RecordHolder holder, int position) {
        Item aux = items.get(position);
        holder.bind(aux);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(ArrayList<Item> items) {
        this.items.clear();
        this.items.addAll(items);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void updateItem(Item item) {
        int indexOf = items.indexOf(item);

        if (indexOf != -1) {
            items.set(indexOf, item);
            notifyItemChanged(indexOf);
        }
    }

    public Item getItem(int position) {
        if ((position > -1) && (position < items.size())) {
            return items.get(position);
        }

        return  null;
    }

//    @Override
//    public void onViewDetachedFromWindow(RecordHolder holder) {
//        super.onViewDetachedFromWindow(holder);
//        itemClicked.removeIfPlaying(holder.getItem());
//        // TODO: 5/16/16  make things happen;
//    }
}
