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

import com.laci.gazora.model.AddressItem;
import com.laci.gazora.R;
import com.laci.gazora.ShowAddressesActivity;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> implements Filterable {
        private ArrayList<AddressItem> mAddressItemsData;
        private ArrayList<AddressItem> mAddressItemsDataAll;
        private Context mContext;
        private int lastPosition = -1;


    public AddressAdapter(Context context, ArrayList<AddressItem> itemsData) {
        this.mAddressItemsData = itemsData;
        this.mAddressItemsDataAll = itemsData;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_addresses, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        AddressItem currentItem = mAddressItemsData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mAddressItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return addressFilter;
    }

    private Filter addressFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<AddressItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mAddressItemsDataAll.size();
                results.values = mAddressItemsDataAll;
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (AddressItem item : mAddressItemsDataAll) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
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
            mAddressItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mAddressText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mAddressText = itemView.findViewById(R.id.address);

            itemView.findViewById(R.id.openAddress).setOnClickListener(view -> {

            });
        }

        public void bindTo(AddressItem currentItem) {
            mAddressText.setText(currentItem.getName());

            itemView.findViewById(R.id.deleteAddress).setOnClickListener(view -> ((ShowAddressesActivity)mContext).deleteAddress(currentItem));
            itemView.findViewById(R.id.openAddress).setOnClickListener(view -> ((ShowAddressesActivity)mContext).openAddress(currentItem));
        }
    }
}
