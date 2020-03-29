package com.bonfire.home.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bonfire.home.R;
import com.bonfire.home.datas.LightMode;
import com.bonfire.home.utils.Size;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LightModeAdapter extends RecyclerView.Adapter {
  private int[] a = {
      R.string.mode_off,
      R.string.mode_static,
      R.string.mode_breathing,
      R.string.mode_color_cycle,
      R.string.mode_strobing,
  };
  private int[] b = {
      R.drawable.ic_power,
      R.drawable.ic_sun,
      R.drawable.ic_breathing,
      R.drawable.ic_color_cycle,
      R.drawable.ic_strobing,
  };
  private Activity activity;
  private int cardSize = 0;
  private int iconSize = 0;
  private int selectedMode = 0;
  private int[] cardBackground = {
      R.color.blue_grey_50,
      R.color.colorPrimary,
      R.color.white,
  };
  private String[] lightModes = {
      LightMode.OFF,
      LightMode.STATIC,
      LightMode.BREATHING,
      LightMode.COLOR_CYCLE,
      LightMode.STROBING,
  };

  public LightModeAdapter(Activity activity) {
    this.activity = activity;

    this.a();
  }

  private void a() {
    int screenWidthPx = Size.getWidthPixels(this.activity);
    this.cardSize = screenWidthPx / 3;
    this.iconSize = cardSize / 4;
  }

  public interface modeChangedListener {
    void onLightModeChanged(String mode);
  }
  private modeChangedListener modeChangedListener;
  public void setModeChangedListener(modeChangedListener modeChangedListener) {
    this.modeChangedListener = modeChangedListener;
    if (this.modeChangedListener != null)
      this.modeChangedListener.onLightModeChanged(this.lightModes[0]);
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_light_mode, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ((Holder) holder).bind(position);
    ((Holder) holder).frameLayout.setOnClickListener(view -> {
      if (this.selectedMode != position) {
        this.selectedMode = position;
        this.notifyDataSetChanged();
        if (this.modeChangedListener != null)
          this.modeChangedListener.onLightModeChanged(this.lightModes[this.selectedMode]);
      }
    });
  }

  @Override
  public int getItemCount() {
    return this.a.length;
  }

  class Holder extends RecyclerView.ViewHolder {
    @BindView(R.id.img_icon)
    ImageView imageViewIcon;
    @BindView(R.id.tv_mode)
    TextView textViewMode;

    @BindView(R.id.card_light_mode)
    CardView cardViewLightMode;
    @BindView(R.id.fr)
    FrameLayout frameLayout;

    @BindView(R.id.div_end)
    View divEnd;
    @BindView(R.id.div_start)
    View divStart;

    public Holder(@NonNull View itemView) {
      super(itemView);

      ButterKnife.bind(this, itemView);
      this.a();
    }

    private void a() {
      this.cardViewLightMode.setLayoutParams(new LinearLayout.LayoutParams(cardSize, cardSize));
      this.imageViewIcon.setLayoutParams(new FrameLayout.LayoutParams(iconSize, iconSize));
    }

    private void bind(int position) {
      this.imageViewIcon.setImageResource(b[position]);
      this.textViewMode.setText(a[position]);

      this.divStart.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
      this.divEnd.setVisibility(position == a.length - 1 ? View.VISIBLE : View.GONE);
      this.cardViewLightMode.setCardBackgroundColor(
          Color.parseColor(activity.getString(position == selectedMode ? cardBackground[1] : cardBackground[0])));
      this.imageViewIcon.setColorFilter(
          Color.parseColor(activity.getString(selectedMode == position ? cardBackground[2] : cardBackground[1])));
    }
  }
}
