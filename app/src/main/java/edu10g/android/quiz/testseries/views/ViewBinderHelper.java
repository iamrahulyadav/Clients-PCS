

package edu10g.android.quiz.testseries.views;

import android.os.Bundle;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu10g.android.quiz.testseries.models.TestCategory;


public class ViewBinderHelper {
    private static final String BUNDLE_MAP_KEY = "ViewBinderHelper_Bundle_Map_Key";

    private Map<TestCategory, Integer> mapStates = Collections.synchronizedMap(new HashMap<TestCategory, Integer>());
    private Map<TestCategory, SwipableLayout> mapLayouts = Collections.synchronizedMap(new HashMap<TestCategory, SwipableLayout>());
    private Set<TestCategory> lockedSwipeSet = Collections.synchronizedSet(new HashSet<TestCategory>());

    private volatile boolean openOnlyOne = false;
    private final Object stateChangeLock = new Object();


    public void bind(final SwipableLayout swipeLayout, final TestCategory id) {
        if (swipeLayout.shouldRequestLayout()) {
            swipeLayout.requestLayout();
        }

        mapLayouts.values().remove(swipeLayout);
        mapLayouts.put(id, swipeLayout);

        swipeLayout.abort();
        swipeLayout.setDragStateChangeListener(new SwipableLayout.DragStateChangeListener() {
            @Override
            public void onDragStateChanged(int state) {
                mapStates.put(id, state);

                if (openOnlyOne) {
                    closeOthers(id, swipeLayout);
                }
            }
        });

        // first time binding.
        if (!mapStates.containsKey(id)) {
            mapStates.put(id, SwipableLayout.STATE_CLOSE);
            swipeLayout.close(false);
        }

        // not the first time, then close or open depends on the current state.
        else {
            int state = mapStates.get(id);

            if (state == SwipableLayout.STATE_CLOSE || state == SwipableLayout.STATE_CLOSING ||
                    state == SwipableLayout.STATE_DRAGGING) {
                swipeLayout.close(false);
            } else {
                swipeLayout.open(false);
            }
        }

        // set lock swipe
        swipeLayout.setLockDrag(lockedSwipeSet.contains(id));
    }

    public void saveStates(Bundle outState) {
        if (outState == null)
            return;

        Bundle statesBundle = new Bundle();
        /*for (Map.Entry<Bottle, Integer> entry : mapStates.entrySet()) {
            statesBundle.putB(entry.getKey(), entry.getValue());
        }*/
        statesBundle.putAll(outState);

        outState.putBundle(BUNDLE_MAP_KEY, statesBundle);
    }



    public void restoreStates(Bundle inState) {
        if (inState == null)
            return;

        if (inState.containsKey(BUNDLE_MAP_KEY)) {
            HashMap<TestCategory, Integer> restoredMap = new HashMap<>();

            Bundle statesBundle = inState.getBundle(BUNDLE_MAP_KEY);
            Set<String> keySet = statesBundle.keySet();

           /* if (keySet != null) {
                for (Bottle key : keySet) {
                    restoredMap.put(key, statesBundle.getInt(key));
                }
            }*/

            mapStates = restoredMap;
        }
    }


    private void closeOthers(TestCategory id, SwipableLayout swipeLayout) {
        synchronized (stateChangeLock) {
            // close other rows if openOnlyOne is true.
            if (getOpenCount() > 1) {
                for (Map.Entry<TestCategory, Integer> entry : mapStates.entrySet()) {
                    if (!entry.getKey().equals(id)) {
                        entry.setValue(SwipableLayout.STATE_CLOSE);
                    }
                }

                for (SwipableLayout layout : mapLayouts.values()) {
                    if (layout != swipeLayout) {
                        layout.close(true);
                    }
                }
            }
        }
    }


    private int getOpenCount() {
        int total = 0;

        for (int state : mapStates.values()) {
            if (state == SwipableLayout.STATE_OPEN || state == SwipableLayout.STATE_OPENING) {
                total++;
            }
        }

        return total;
    }
}
