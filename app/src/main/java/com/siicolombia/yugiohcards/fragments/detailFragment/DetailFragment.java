package com.siicolombia.yugiohcards.fragments.detailFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.siicolombia.yugiohcards.R;
import com.siicolombia.yugiohcards.entities.cards.CardEntity;
import com.siicolombia.yugiohcards.helpers.connection.ConnectionHelper;
import com.siicolombia.yugiohcards.helpers.connection.NetworkListener;
import com.siicolombia.yugiohcards.shared.utils.Utils;

public class DetailFragment extends Fragment implements NetworkListener {

    CardEntity cardEntity;
    TextView name, description, type, frameType, race, atribute, level, atack, defend;
    ImageView imageCard;
    LinearLayout moreInfo, aditional;
    ImageButton back;
    ConnectionHelper connectionHelper;

    public void setCardEntity(CardEntity cardEntity) {
        this.cardEntity = cardEntity;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        name = view.findViewById(R.id.textTitle);
        description = view.findViewById(R.id.textDescription);
        type = view.findViewById(R.id.textType);
        frameType = view.findViewById(R.id.textFrameType);
        race = view.findViewById(R.id.textRace);
        imageCard = view.findViewById(R.id.imageCardDetail);
        back = view.findViewById(R.id.backArrow);
        moreInfo = view.findViewById(R.id.btnMoreInfo);
        atribute = view.findViewById(R.id.textAtribute);
        level = view.findViewById(R.id.textLevel);
        atack = view.findViewById(R.id.textAtack);
        defend = view.findViewById(R.id.textDef);
        aditional = view.findViewById(R.id.aditional);
        connectionHelper = new ConnectionHelper(this);


        Utils.loadUrlImage(getActivity(), cardEntity.getCardImages()[0].getImage_url_cropped(), imageCard);
        name.setText(cardEntity.getName());
        description.setText(cardEntity.getDesc());

        if (cardEntity.getType() != null) {
            type.setText("Type: " + cardEntity.getType());
        } else {
            type.setVisibility(View.GONE);
        }

        if (cardEntity.getFrameType() != null) {
            frameType.setText("Frame type: " + cardEntity.getFrameType());
        } else {
            frameType.setVisibility(View.GONE);
        }

        if (cardEntity.getRace() != null) {
            race.setText("Race: " + cardEntity.getRace());
        } else {
            race.setVisibility(View.GONE);
        }

        if (cardEntity.getAttribute() != null) {
            atribute.setText("Attribute: " + cardEntity.getAttribute());
        } else {
            atribute.setVisibility(View.GONE);
        }

        if (cardEntity.getLevel() != null) {
            level.setText("Level: " + cardEntity.getLevel());
        } else {
            level.setVisibility(View.GONE);
        }

        if (cardEntity.getAtk() != null) {
            atack.setText("Attack: " + cardEntity.getAtk());
        } else {
            atack.setVisibility(View.GONE);
        }

        if (cardEntity.getDef() != null) {
            defend.setText("Defend: " + cardEntity.getDef());
        } else {
            defend.setVisibility(View.GONE); // Ocultar la vista si el dato es nulo
        }

        if(cardEntity.getYgoprodeckUrl() == null) {
            moreInfo.setVisibility(View.GONE);
        }

        if(cardEntity.getLevel() == null & cardEntity.getDef() == null && cardEntity.getAtk() == null) {
            aditional.setVisibility(View.GONE);
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!connectionHelper.isOnline(getActivity())) {
                    Utils.showMessageDialog(getString(R.string.offline_text_message), getActivity());
                    return;
                }
                String url = cardEntity.getYgoprodeckUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        return view;
    }

    @Override
    public void onNetworkChangeListener(Boolean isOnline) {

    }
}