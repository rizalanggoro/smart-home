package com.bonfire.home.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bonfire.home.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IntervalAdapter extends RecyclerView.Adapter {
  private int[] a = {
      R.string.interval_1,
      R.string.interval_5,
      R.string.interval_10,
      R.string.interval_25,
      R.string.interval_50,
      R.string.interval_100,
      R.string.interval_1000,
  };
  private int[] b = {
      1,
      5,
      10,
      25,
      50,
      100,
      1000,
  };
  private int selectedInterval = 10; // default selected interval ==> 10 ms
  private Activity activity;

  public IntervalAdapter(Activity activity, int selectedInterval) {
    this.activity = activity;
    if (this.selectedInterval != -1)
      this.selectedInterval = selectedInterval;
  }

  public interface onIntervalChangedListener {
    void onIntervalChanged(int interval);
  }
  private onIntervalChangedListener onIntervalChangedListener;
  public void setOnIntervalChangedListener(onIntervalChangedListener onIntervalChangedListener) {
    this.onIntervalChangedListener = onIntervalChangedListener;
    if (this.onIntervalChangedListener != null)
      this.onIntervalChangedListener.onIntervalChanged(this.b[2]);
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_interval, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ((Holder) holder).bind(position);
    ((Holder) holder).linearLayout.setOnClickListener(view -> {
      if (this.selectedInterval != b[position]) {
        this.selectedInterval = b[position];
        activity.runOnUiThread(this::notifyDataSetChanged);
        if (this.onIntervalChangedListener != null)
          this.onIntervalChangedListener.onIntervalChanged(this.selectedInterval);
      }
    });
  }

  @Override
  public int getItemCount() {
    return a.length;
  }

  class Holder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_title)
    TextView textViewTitle;

    @BindView(R.id.img_icon)
    ImageView imageViewIcon;

    @BindView(R.id.ln)
    LinearLayout linearLayout;

    public Holder(@NonNull View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
    }

    private void bind(int position) {
      this.textViewTitle.setText(a[position]);
      this.imageViewIcon.setColorFilter(
          activity.getResources().getColor(b[position] == selectedInterval ?
              R.color.colorPrimary : R.color.blue_grey_200));
    }
  }
}
