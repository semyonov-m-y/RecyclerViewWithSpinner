package ru.semenovmy.learning.recyclerviewwithspinner.models;

import androidx.annotation.StringRes;

import ru.semenovmy.learning.recyclerviewwithspinner.R;

public enum DisplayMode {

    UNGROUPED(R.string.ungrouped),
    GROUP_BY_WEEK(R.string.group_by_week);

    private final int mTitleStringResourceId;

    DisplayMode(@StringRes int titleStringResourceId) {
        mTitleStringResourceId = titleStringResourceId;
    }

    public int getTitleStringResourceId() {
        return mTitleStringResourceId;
    }

}
