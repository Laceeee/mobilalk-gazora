package com.laci.gazora.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laci.gazora.model.GasItem;
import com.laci.gazora.R;
import com.laci.gazora.ShowGasActivity;

import java.util.ArrayList;

public class GasAdapter extends RecyclerView.Adapter<GasAdapter.ViewHolder> implements Filterable {
    private ArrayList<GasItem> mGasItemsData;
    private ArrayList<GasItem> mGasItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    public GasAdapter(Context context, ArrayList<GasItem> itemsData) {
        this.mGasItemsData = itemsData;
        this.mGasItemsDataAll = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_gas_amounts, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GasAdapter.ViewHolder holder, int position) {
        GasItem currentItem = mGasItemsData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_from_bottom);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mGasItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return gasFilter;
    }

    private Filter gasFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<GasItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mGasItemsDataAll.size();
                results.values = mGasItemsDataAll;
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (GasItem item : mGasItemsDataAll) {
                    if (item.getDate().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mGasItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };



    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mGasTextView;
        private TextView mDateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mGasTextView = itemView.findViewById(R.id.gas);
            mDateTextView = itemView.findViewById(R.id.date);


        }

        public void bindTo(GasItem currentItem) {
            mGasTextView.setText(currentItem.getAmount());
            mDateTextView.setText(currentItem.getDate());

            itemView.findViewById(R.id.deleteGas).setOnClickListener(view -> ((ShowGasActivity)mContext).deleteGas(currentItem));
        }
    }
}
