package com.bonfire.home.bottomsheets;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bonfire.home.R;
import com.bonfire.home.utils.Font;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplyingBottomSheet extends BottomSheetDialogFragment {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    this.setCancelable(false);
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
    View view = inflater.inflate(R.layout.bottom_sheet_applying, container, false);

    ButterKnife.bind(this, view);
    this.a();

    return view;
  }

  @BindView(R.id.tv_applying)
  TextView textViewApplying;
  private void a() {
    Font.setGilroyBold(this.textViewApplying);
  }
}
