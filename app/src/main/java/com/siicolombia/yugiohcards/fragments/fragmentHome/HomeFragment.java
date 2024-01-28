package com.siicolombia.yugiohcards.fragments.fragmentHome;

import android.animation.Animator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.siicolombia.yugiohcards.R;
import com.siicolombia.yugiohcards.entities.cards.CardEntity;
import com.siicolombia.yugiohcards.helpers.StaggeredLayoutHelper;
import com.siicolombia.yugiohcards.helpers.connection.ConnectionHelper;
import com.siicolombia.yugiohcards.helpers.connection.NetworkListener;
import com.siicolombia.yugiohcards.service.ServicePackageManager;
import com.siicolombia.yugiohcards.service.onServiceResponseListener;
import com.siicolombia.yugiohcards.shared.utils.Utils;
import com.siicolombia.yugiohcards.shared.searchableSpinner.SearchableSpinner;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements NetworkListener {

    RecyclerView recyclerCards;
    CardEntity cardEntity;
    ArrayList<CardEntity> cardEntities;
    HomeFragmentAdapter adapter;
    LinearLayout loadingCard, offline;
    Toolbar toolbar;
    boolean isloading = false;
    int page;
    StaggeredLayoutHelper layoutHelper;
    ServicePackageManager manager;
    String filter;
    private boolean isReceiverRegistered = false;
    ConnectionHelper connectionHelper;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        if(!isReceiverRegistered) {
            Utils.initNetworkReceiverListener(getActivity(), this);
            isReceiverRegistered = true;
        }

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        filter = "";
        loadingCard = view.findViewById(R.id.cardLoading);
        toolbar = view.findViewById(R.id.toolbar);
        cardEntities = new ArrayList<>();
        recyclerCards = view.findViewById(R.id.recyclerViewCards);
        adapter = new HomeFragmentAdapter(cardEntities, getActivity(), this);
        layoutHelper = new StaggeredLayoutHelper();
        manager = new ServicePackageManager(getActivity(), Utils.BASE_URL);
        page = 0;
        connectionHelper = new ConnectionHelper(this);
        offline = view.findViewById(R.id.cardOffline);

        initRecyclerView();
        initToolbar();

        if(connectionHelper.isOnline(getActivity())) {
            loadData(manager, filter);
        } else {
            loadingCard.setVisibility(View.GONE);
            offline.setVisibility(View.VISIBLE);
        }

        recyclerCards.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] lastVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
                    if (lastVisibleItems.length > 0 && layoutHelper.isLastItemDisplaying(recyclerView)) {
                        if(!isloading) {
                            isloading = true;
                            loadData(manager, filter);
                        }
                    }
                }
            }
        });

        return view;
    }

    private void loadData(ServicePackageManager manager, String filter) {
        showLoading(true);
        int pageToLoad = 10 * page;
        manager.sendGetRequestToService("cardinfo.php?num=10&offset=" + pageToLoad + filter,
                new onServiceResponseListener() {
            @Override
            public void onSuccessResponse(JSONObject response) throws JSONException {
                isloading = false;
                JSONArray dataArray = response.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject jsonCard = dataArray.getJSONObject(i);
                    cardEntity = new GsonBuilder().create().fromJson(jsonCard.toString(), CardEntity.class);

                    JSONObject banlistInfo = jsonCard.optJSONObject("banlist_info");
                    if (banlistInfo != null) {
                        String banStatusTCG = banlistInfo.optString("ban_tcg");
                        String banStatusOCG = banlistInfo.optString("ban_ocg");

                        if ("banned".equals(banStatusTCG) || "banned".equals(banStatusOCG)) {
                            continue;
                        }
                    }
                    cardEntities.add(cardEntity);
                }
                adapter.notifyDataSetChanged();
                showLoading(false);
                page++;
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse == null) return;
                if(error.networkResponse.statusCode == 400) {
                    Toast.makeText(getActivity(), "No more cards were found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.home_screen_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_action_filter) {
                if(!connectionHelper.isOnline(getActivity())) {
                    Utils.showMessageDialog(getString(R.string.offline_text_message), getActivity());
                    return true;
                }
                SearchableSpinner searchableSpinner = new SearchableSpinner(getActivity());
                searchableSpinner.setFiltered(!filter.isEmpty());
                searchableSpinner.setOnItemSelected(archetype -> {
                    setupFilter("&archetype=" + archetype.getArchetypeName());
                    loadData(manager, filter);
                    toolbar.setSubtitle("Filtered archetype: " + archetype.getArchetypeName());
                    toolbar.setSubtitleTextColor(Color.WHITE);
                    searchableSpinner.hide();
                });
                searchableSpinner.setOnCleanFilterListener(v -> {
                    setupFilter("");
                    loadData(manager, filter);
                    toolbar.setSubtitle(null);
                    searchableSpinner.hide();
                });
                searchableSpinner.createDialog();
                searchableSpinner.show();
                return true;
            }
            return false;
        });
    }

    private void setupFilter(String filter) {
        cardEntities.clear();
        adapter.notifyDataSetChanged();
        page = 0;
        this.filter = filter;
    }

    private void showLoading(boolean show) {
        if(show) {
            loadingCard.setVisibility(View.VISIBLE);
            loadingCard.animate().alpha(1).setDuration(300);
            return;
        }
        loadingCard.animate().alpha(0).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                loadingCard.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
    }

    @Override
    public void onResume() {
        if(!isReceiverRegistered) {
            Utils.initNetworkReceiverListener(getActivity(), this);
            isReceiverRegistered = true;
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Utils.unregisterNetworkReceiver(getActivity());
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (isReceiverRegistered) {
            Utils.unregisterNetworkReceiver(getActivity());
            isReceiverRegistered = false;
        }
        super.onPause();
    }

    private void initRecyclerView() {
        recyclerCards.setAdapter(adapter);
        recyclerCards.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    public void onNetworkChangeListener(Boolean isOnline) {
        if(!isOnline) {
            offline.setVisibility(View.VISIBLE);
        } else {
            if(cardEntities.isEmpty()) {
                loadData(manager, filter);
            }
            offline.setVisibility(View.GONE);
        }
    }
}