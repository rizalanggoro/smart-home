package com.bonfire.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bonfire.home.R;
import com.bonfire.home.utils.Color;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ColorAdapter extends RecyclerView.Adapter {
  private String primaryColor = "500";
  private int[] primaryColors = {
      Color.red(this.primaryColor),         // 0
      Color.pink(this.primaryColor),        // 1
      Color.purple(this.primaryColor),      // 2
      Color.deepPurple(this.primaryColor),  // 3
      Color.indigo(this.primaryColor),      // 4
      Color.blue(this.primaryColor),        // 5
      Color.lightBlue(this.primaryColor),   // 6
      Color.cyan(this.primaryColor),        // 7
      Color.teal(this.primaryColor),        // 8
      Color.green(this.primaryColor),       // 9
      Color.lightGreen(this.primaryColor),  // 10
      Color.lime(this.primaryColor),        // 11
      Color.yellow(this.primaryColor),      // 12
      Color.amber(this.primaryColor),       // 13
      Color.orange(this.primaryColor),      // 14
      Color.deepOrange(this.primaryColor),  // 15
      Color.brown(this.primaryColor),       // 16
      Color.grey(this.primaryColor),        // 17
      Color.blueGrey(this.primaryColor),    // 18
  };
  private int[] primaryColorsRGB = {
      Color.red(this.primaryColor),
      Color.green(this.primaryColor),
      Color.blue(this.primaryColor),
  };
  private boolean rgbOnly;
  public interface colorChangedListener {
    void onColorChanged(String result);
  }
  private colorChangedListener colorChangedListener;
  public void setColorChangedListener(colorChangedListener listener) {
    if (listener != null)
      this.colorChangedListener = listener;
  }

  public ColorAdapter(boolean rgbOnly) {
    this.rgbOnly = rgbOnly;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_color, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    if (rgbOnly) {
      ((Holder) holder).bind(this.primaryColorsRGB[position], position);
    } else {
      ((Holder) holder).bind(this.primaryColors[position], position);
    }
    ((Holder) holder).frameLayoutColor.setOnClickListener(view -> {
      // return position --> rgb only
      // return integer color
      String data = "";
      if (rgbOnly) {
        data = String.valueOf(position);
      } else {
        // --> static;red:38,green:50,,blue:56
        int hexColor = this.primaryColors[position];
        int red = (hexColor >> 16) & 0xFF;
        int green = (hexColor >> 8) & 0xFF;
        int blue = (hexColor) & 0xFF;
        data = "red:" + red + ",green:" + green + ",,blue:" + blue;
      }
      if (this.colorChangedListener != null)
        this.colorChangedListener.onColorChanged(data);
    });
  }

  @Override
  public int getItemCount() {
    if (rgbOnly) {
      return this.primaryColorsRGB.length;
    } else {
      return this.primaryColors.length;
    }
  }

  class Holder extends RecyclerView.ViewHolder {
    @BindView(R.id.div_start)
    View divStart;
    @BindView(R.id.div_end)
    View divEnd;

    @BindView(R.id.card_color)
    CardView cardViewColor;
    @BindView(R.id.fr_color)
    FrameLayout frameLayoutColor;

    public Holder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }

    private void bind(int color, int position) {
      this.divStart.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
      if (rgbOnly) {
        this.divEnd.setVisibility(position == primaryColorsRGB.length - 1 ? View.VISIBLE : View.GONE);
      } else {
        this.divEnd.setVisibility(position == primaryColors.length - 1 ? View.VISIBLE : View.GONE);
      }
      this.cardViewColor.setCardBackgroundColor(color);
    }
  }
}
