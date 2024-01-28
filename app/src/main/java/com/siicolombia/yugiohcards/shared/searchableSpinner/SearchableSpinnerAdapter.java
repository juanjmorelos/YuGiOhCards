package com.siicolombia.yugiohcards.shared.searchableSpinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.siicolombia.yugiohcards.R;
import com.siicolombia.yugiohcards.entities.archetype.ArchetypeEntity;
import com.siicolombia.yugiohcards.helpers.connection.ConnectionHelper;
import com.siicolombia.yugiohcards.helpers.connection.NetworkListener;
import com.siicolombia.yugiohcards.shared.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SearchableSpinnerAdapter extends RecyclerView.Adapter<SearchableSpinnerAdapter.ViewHolder> implements NetworkListener {
    ArrayList<ArchetypeEntity> list = new ArrayList<>();
    ArrayList<ArchetypeEntity> listCompleta;
    ItemClick itemClickListener;
    Context context;
    ConnectionHelper connectionHelper;

    public SearchableSpinnerAdapter(ArrayList<ArchetypeEntity> archetypeList, ItemClick itemClickListener, Context context) {
        this.listCompleta = archetypeList;
        this.itemClickListener = itemClickListener;
        this.context = context;
        connectionHelper = new ConnectionHelper(this);
    }

    public void setItems(List<ArchetypeEntity> arrayList){
        this.list.clear();
        this.list.addAll(arrayList);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        list.clear();
        if(text.equals("")){
            list.addAll(listCompleta);
            notifyDataSetChanged();
            return;
        }
        for (ArchetypeEntity item : listCompleta) {
            if (item.getArchetypeName().toLowerCase().contains(text.toLowerCase())) {
                list.add(item);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchableSpinnerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();
        View view = LayoutInflater.from(parentContext).inflate(R.layout.search_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchableSpinnerAdapter.ViewHolder holder, int position) {
        ArchetypeEntity item = list.get(position);
        holder.archetypeName.setText(item.getArchetypeName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!connectionHelper.isOnline(context)) {
                    Utils.showMessageDialog(context.getString(R.string.offline_text_message), context);
                    return;
                }
                itemClickListener.onItemClickListener(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onNetworkChangeListener(Boolean isOnline) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView archetypeName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            archetypeName = itemView.findViewById(R.id.arquetypeName);
        }
    }
}
