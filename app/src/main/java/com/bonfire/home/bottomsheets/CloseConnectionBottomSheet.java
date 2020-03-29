package com.bonfire.home.bottomsheets;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bonfire.home.R;
import com.bonfire.home.utils.Font;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CloseConnectionBottomSheet extends BottomSheetDialogFragment {
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
    View view = inflater.inflate(R.layout.bottom_sheet_close_bluetooth_connection, container, false);

    ButterKnife.bind(this, view);
    this.a();
    this.b();

    return view;
  }

  @BindView(R.id.tv_close_connection)
  TextView textViewCloseConnection;
  @BindView(R.id.tv_cancel)
  TextView textViewCancel;
  @BindView(R.id.tv_close)
  TextView textViewClose;

  @BindView(R.id.fr_cancel)
  FrameLayout frameLayoutCancel;
  @BindView(R.id.fr_close)
  FrameLayout frameLayoutClose;
  private void a() {
    Font.setGilroyBold(this.textViewCloseConnection);
    Font.setSofiaProLight(
        this.textViewCancel,
        this.textViewClose
    );
    Font.setBoldStyle(
        this.textViewCancel,
        this.textViewClose
    );
  }

  private void b() {
    this.frameLayoutCancel.setOnClickListener(view -> this.dismissDialog());
    this.frameLayoutClose.setOnClickListener(view -> {
      if (this.closeListener != null)
        this.closeListener.onResult(-1);
      this.dismissDialog();
    });
  }

  private void dismissDialog() {
    if (this.getDialog() != null && this.getDialog().isShowing())
      this.getDialog().dismiss();
  }

  public interface closeListener {
    void onResult(int result);
  }
  private closeListener closeListener;
  public void setCloseListener(closeListener closeListener) {
    this.closeListener = closeListener;
  }
}
