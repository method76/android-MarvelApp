package com.method76.comics.marvel.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.method76.comics.marvel.R;
import com.method76.comics.marvel.common.constant.AppConst;
import com.method76.comics.marvel.data.substr.MarvelCharacter;
import com.method76.common.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * 썸네일 그리드 어댑터
 */
public class ThumbnailAdapter extends
        RecyclerView.Adapter<ThumbnailAdapter.ListItemViewHolder> implements AppConst {

    private static Activity context;
    private List<MarvelCharacter> items;
    private SparseBooleanArray selectedItems;
    private Handler handler;


    public ThumbnailAdapter(Activity context, List<MarvelCharacter> modelData) {
        if (modelData == null) {
            throw new IllegalArgumentException("modelData must not be null");
        }
        this.context        = context;
        this.items          = modelData;
        this.selectedItems  = new SparseBooleanArray();
    }

    public void setHandler(Handler handler){
        this.handler = handler;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_thumbnail, viewGroup, false);
        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder viewHolder, int position) {
        MarvelCharacter model = items.get(position);
        model.setIndex(position);
        if(selectedItems.get(position, false)){
            // If selected item?
            viewHolder.btn_hidden.setSelected(true);
        }else{
            viewHolder.btn_hidden.setSelected(false);
        }
        viewHolder.char_nm.setText(model.getName());
        viewHolder.btn_hidden.setTag(model);
        Glide.with(context).load(
                model.getThumbnail().getPath() + "." + model.getThumbnail().getExtension())
                    .into(viewHolder.grid_item_image);
    }

    @Override
    public void onViewRecycled(ListItemViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    /**
     * Adds and item into the underlying data set
     * at the position passed into the method.
     *
     * @param newModelData The item to add to the data set.
     * @param position The index of the item to remove.
     */
    public void addData(MarvelCharacter newModelData, int position) {
        items.add(position, newModelData);
        notifyItemInserted(position);
    }

    /**
     * Removes the item that currently is at the passed in position from the
     * underlying data set.
     * @param position The index of the item to remove.
     */
    public void removeData(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public MarvelCharacter getItem(int position) {
        return items.get(position);
    }

    /**
     * 아이템 뷰 홀더
     */
    class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView grid_item_image;
        TextView char_nm;
        LinearLayout btn_hidden;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            grid_item_image = (ImageView) itemView.findViewById(R.id.grid_item_image);
            char_nm = (TextView) itemView.findViewById(R.id.char_nm);
            btn_hidden = (LinearLayout)itemView.findViewById(R.id.btn_hidden);
            btn_hidden.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            MarvelCharacter item = (MarvelCharacter)view.getTag();
            if(view.isSelected()==false) {
                if(getSelectedItemCount()<LIMIT_CHAR) {
                    view.setSelected(true);
                }else{
                    return;
                }
            }else{
                view.setSelected(false);
            }
            toggleSelection(item.getIndex());
            Message msg = ThumbnailAdapter.this.handler.obtainMessage();
            msg.what = ITEM_SELECTED;
            msg.arg1 = item.getId();

            Bundle data = new Bundle();
            String name = item.getName().indexOf("(")>-1?item.getName().substring(0, item.getName().indexOf("(")):item.getName();
            data.putString(CHARACTER_INFO, name);
            msg.setData(data);

            ThumbnailAdapter.this.handler.sendMessage(msg);
        }

    }

}
