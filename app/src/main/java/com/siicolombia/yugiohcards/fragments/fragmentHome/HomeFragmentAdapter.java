package com.siicolombia.yugiohcards.fragments.fragmentHome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siicolombia.yugiohcards.R;
import com.siicolombia.yugiohcards.entities.cards.CardEntity;
import com.siicolombia.yugiohcards.fragments.detailFragment.DetailFragment;
import com.siicolombia.yugiohcards.shared.utils.Utils;

import java.util.ArrayList;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {
    ArrayList<CardEntity> cardItems;
    Context context;
    HomeFragment homeFragment;

    public HomeFragmentAdapter(ArrayList<CardEntity> cardItems, Context context, HomeFragment homeFragment) {
        this.cardItems = cardItems;
        this.context = context;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public HomeFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context parentContext = parent.getContext();
        View view = LayoutInflater.from(parentContext).inflate(R.layout.home_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragmentAdapter.ViewHolder holder, int position) {
        CardEntity item = cardItems.get(position);
        holder.cardName.setText(item.getName());
        holder.cardType.setText(item.getType());
        Utils.loadUrlImage(context, item.getCardImages()[0].getImage_url_cropped(), holder.cardImage);
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setCardEntity(item);
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                Utils.goToFragment(fragmentManager, homeFragment, detailFragment );
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        TextView cardName, cardType;
        ConstraintLayout item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardImage = itemView.findViewById(R.id.imageCard);
            cardName = itemView.findViewById(R.id.cardName);
            cardType = itemView.findViewById(R.id.cardType);
            item = itemView.findViewById(R.id.item);
        }
    }
}
