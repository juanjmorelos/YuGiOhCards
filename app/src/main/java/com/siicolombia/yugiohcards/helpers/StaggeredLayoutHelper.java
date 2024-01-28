package com.siicolombia.yugiohcards.helpers;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class StaggeredLayoutHelper {

    public StaggeredLayoutHelper() {

    }

    public boolean isLastItemDisplaying(RecyclerView recyclerView) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null && adapter.getItemCount() > 0) {
            int lastVisibleItemPosition;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

            if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] lastVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItems);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            return lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == adapter.getItemCount() - 1;
        }

        return false;
    }

    private int getLastVisibleItem(int[] lastVisibleItems) {
        int max = lastVisibleItems[0];
        for (int lastVisibleItem : lastVisibleItems) {
            if (lastVisibleItem > max) {
                max = lastVisibleItem;
            }
        }
        return max;
    }
}
