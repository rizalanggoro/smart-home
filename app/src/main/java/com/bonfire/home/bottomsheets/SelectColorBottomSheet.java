package com.bonfire.home.bottomsheets;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bonfire.home.MainActivity;
import com.bonfire.home.R;
import com.bonfire.home.adapters.ColorAdapter;
import com.bonfire.home.datas.LightMode;
import com.bonfire.home.utils.Font;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectColorBottomSheet extends BottomSheetDialogFragment {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (this.getDialog() != null && this.getDialog().getWindow() != null) {
      Window window = this.getDialog().getWindow();
      if (window != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          window.getDecorView().setSystemUiVisibility(
              window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
          );
        }
      }
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.bottom_sheet_select_color, container, false);

    ButterKnife.bind(this, view);
    this.a();
    this.b();

    return view;
  }

  @BindView(R.id.tv_led_color)
  TextView textViewLedColor;

  @BindView(R.id.rv_color)
  RecyclerView recyclerViewColor;

  public interface colorSelectedListener {
    void onColorSelected(String result);
  }
  private colorSelectedListener colorSelectedListener;
  public void setColorSelectedListener(colorSelectedListener colorSelectedListener) {
    if (colorSelectedListener != null)
      this.colorSelectedListener = colorSelectedListener;
  }

  private ColorAdapter colorAdapter;
  private String result = "";
  private void a() {
    this.colorAdapter = new ColorAdapter(!MainActivity.selectedLedMode.equals(LightMode.STATIC));
    this.colorAdapter.setColorChangedListener(this::c);

    Font.setGilroyBold(this.textViewLedColor);
  }

  private void b() {
    if (this.colorAdapter != null) {
      this.recyclerViewColor.setAdapter(this.colorAdapter);
      this.recyclerViewColor.setLayoutManager(
          new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
      this.colorAdapter.notifyDataSetChanged();
    }
  }

  private void c(String result) {
    this.result = result;
    if (this.colorSelectedListener != null)
      this.colorSelectedListener.onColorSelected(this.result);
    if (this.getDialog() != null && this.getDialog().isShowing())
      this.getDialog().dismiss();
  }
}
