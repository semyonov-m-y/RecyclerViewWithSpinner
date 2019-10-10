package ru.semenovmy.learning.recyclerviewwithspinner.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import ru.semenovmy.learning.recyclerviewwithspinner.models.DisplayMode;
import ru.semenovmy.learning.recyclerviewwithspinner.R;
import ru.semenovmy.learning.recyclerviewwithspinner.models.Lecture;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.BaseViewHolder> {

    private static final int ITEM_VIEW_TYPE_LECTURE = 0;
    private static final int ITEM_VIEW_TYPE_WEEK = 1;

    private final Resources mResources;
    private List<Object> mAdapterItems;
    private List<Lecture> mLectures;
    private DisplayMode mDisplayMode = DisplayMode.UNGROUPED;

    public DataAdapter(Resources resources) {
        mResources = resources;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mAdapterItems.get(position);
        if (item instanceof Lecture) {
            return ITEM_VIEW_TYPE_LECTURE;
        } else if (item instanceof String) {
            return ITEM_VIEW_TYPE_WEEK;
        } else {
            throw new RuntimeException("This item is not supported by adapter: " + item);
        }
    }

    @Override
    public DataAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_VIEW_TYPE_LECTURE: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_lecture, parent, false);
                return new LectureHolder(view);
            }
            case ITEM_VIEW_TYPE_WEEK: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_week, parent, false);
                return new WeekHolder(view);
            }
            default:
                throw new IllegalArgumentException("ViewType " + viewType + " is not supported");
        }
    }

    @Override
    public void onBindViewHolder(DataAdapter.BaseViewHolder holder, int position) {
        Object item = mAdapterItems.get(position);
        switch (getItemViewType(position)) {
            case ITEM_VIEW_TYPE_LECTURE:
                ((LectureHolder) holder).bindView((Lecture) item);
                break;
            case ITEM_VIEW_TYPE_WEEK:
                ((WeekHolder) holder).bindView((String) item);
                break;
            default:
                throw new RuntimeException("This item is not supported by adapter: " + item);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapterItems == null ? 0 : mAdapterItems.size();
    }

    public void setLectures(List<Lecture> lectures) {
        if (lectures == null) {
            mLectures = new ArrayList<>();
            mAdapterItems = new ArrayList<>();
        } else {
            mLectures = new ArrayList<>(lectures);
            switch (mDisplayMode) {
                case GROUP_BY_WEEK:
                    groupLecturesByWeek(lectures);
                    break;
                case UNGROUPED:
                default:
                    mAdapterItems = new ArrayList<Object>(lectures);
            }
        }
        notifyDataSetChanged();
    }

    public void setDisplayMode(DisplayMode displayMode) {
        mDisplayMode = displayMode;
        setLectures(mLectures);
    }

    public int getPositionOf(Lecture lecture) {
        return mAdapterItems.indexOf(lecture);
    }

    private void groupLecturesByWeek(List<Lecture> lectures) {
        mAdapterItems = new ArrayList<>();
        int weekIndex = -1;
        for (Lecture lecture : lectures) {
            if (lecture.getWeekIndex() > weekIndex) {
                weekIndex = lecture.getWeekIndex();
                mAdapterItems.add(mResources.getString(R.string.week_number, weekIndex + 1));
            }
            mAdapterItems.add(lecture);
        }
    }

    static abstract class BaseViewHolder extends RecyclerView.ViewHolder {

        BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static class LectureHolder extends BaseViewHolder {

        private final TextView mNumber;
        private final TextView mDate;
        private final TextView mTheme;
        private final TextView mLector;

        private LectureHolder(View itemView) {
            super(itemView);
            mNumber = itemView.findViewById(R.id.number);
            mDate = itemView.findViewById(R.id.date);
            mTheme = itemView.findViewById(R.id.theme);
            mLector = itemView.findViewById(R.id.lector);
        }

        private void bindView(Lecture lecture) {
            mNumber.setText(String.valueOf(lecture.getNumber()));
            mDate.setText(lecture.getDate());
            mTheme.setText(lecture.getTheme());
            mLector.setText(lecture.getLector());
        }
    }

    private static class WeekHolder extends BaseViewHolder {

        private final TextView mWeek;

        private WeekHolder(View itemView) {
            super(itemView);
            mWeek = itemView.findViewById(R.id.week);
        }

        private void bindView(String week) {
            mWeek.setText(week);
        }
    }
}
