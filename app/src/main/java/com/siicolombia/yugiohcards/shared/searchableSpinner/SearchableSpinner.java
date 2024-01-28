package com.siicolombia.yugiohcards.shared.searchableSpinner;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;
import com.siicolombia.yugiohcards.R;
import com.siicolombia.yugiohcards.entities.archetype.ArchetypeEntity;
import com.siicolombia.yugiohcards.service.ServicePackageManager;
import com.siicolombia.yugiohcards.service.onServiceResponseListener;
import com.siicolombia.yugiohcards.shared.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchableSpinner {
    private ArrayList<ArchetypeEntity> arrayList;
    private ArchetypeEntity archetypeEntity;
    private Dialog dialog;
    private final Context context;
    private ItemClick itemSelected;
    private SearchableSpinnerAdapter adapter;
    private LinearLayout loading;
    private View.OnClickListener listener;
    private boolean isFiltered = false;
    
    public SearchableSpinner(Context context) {
        this.context = context;  
    }

    public void setOnItemSelected(ItemClick itemSelected) {
        this.itemSelected = itemSelected;
    }
    public void setOnCleanFilterListener(View.OnClickListener l) {
        this.listener = l;
    }

    public void setFiltered(boolean filtered) {
        isFiltered = filtered;
    }

    public void createDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.searchable_spinner, null, false);
        alert.setView(view);
        initComponents(view);
        loadSpinnerData();
        dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initComponents(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.spinnerRecyclerView);
        TextInputEditText searchText = view.findViewById(R.id.searchText);
        arrayList = new ArrayList<>();
        adapter = new SearchableSpinnerAdapter(arrayList, itemSelected, context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        loading = view.findViewById(R.id.archetypeLoading);
        Button cancelButton = view.findViewById(R.id.cancelButton);
        Button cleanFilter = view.findViewById(R.id.cleanFilterButton);

        cleanFilter.setOnClickListener(listener);
        cancelButton.setOnClickListener(v -> hide());
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if(isFiltered) {
            cleanFilter.setVisibility(View.VISIBLE);
        }
    }

    private void loadSpinnerData() {
        showLoading(true);
        ServicePackageManager manager = new ServicePackageManager(context, Utils.BASE_URL);
        manager.sendArrayGetRequestToService("archetypes.php", new onServiceResponseListener() {
            @Override
            public void onSuccessResponse(JSONArray response) throws JSONException {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonArchetype = response.getJSONObject(i);
                    archetypeEntity = new GsonBuilder().create().fromJson(jsonArchetype.toString(), ArchetypeEntity.class);
                    arrayList.add(archetypeEntity);
                }
                adapter.setItems(arrayList);
                adapter.notifyDataSetChanged();
                showLoading(false);
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private void showLoading(boolean show) {
        if(show) {
            loading.setVisibility(View.VISIBLE);
            loading.animate().alpha(1).setDuration(300);
            return;
        }
        loading.animate().alpha(0).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animation) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animation) {

            }
        });
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.hide();
        dialog.cancel();
    }
}
