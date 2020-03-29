package com.bonfire.home.bottomsheets;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bonfire.home.MainActivity;
import com.bonfire.home.R;
import com.bonfire.home.adapters.IntervalAdapter;
import com.bonfire.home.utils.Font;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectIntervalBottomSheet extends BottomSheetDialogFragment {

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
    View view = inflater.inflate(R.layout.bottom_sheet_select_interval, container, false);

    ButterKnife.bind(this, view);
    this.a();
    this.b();
    this.c();

    return view;
  }

  @BindView(R.id.rv_interval)
  RecyclerView recyclerViewInterval;

  @BindView(R.id.tv_cancel)
  TextView textViewCancel;
  @BindView(R.id.tv_done)
  TextView textViewDone;
  @BindView(R.id.tv_interval)
  TextView textViewInterval;

  @BindView(R.id.fr_cancel)
  FrameLayout frameLayoutCancel;
  @BindView(R.id.fr_done)
  FrameLayout frameLayoutDone;

  public interface selectIntervalListener {
    void onIntervalSelected(int interval);
  }
  private selectIntervalListener selectIntervalListener;
  public SelectIntervalBottomSheet setSelectIntervalListener(selectIntervalListener selectIntervalListener) {
    this.selectIntervalListener = selectIntervalListener;
    return this;
  }

  private IntervalAdapter intervalAdapter;
  private int selectedInterval = 10; // default;
  private void a() {
    this.intervalAdapter = new IntervalAdapter(getActivity(), this.selectedInterval);
    this.intervalAdapter.setOnIntervalChangedListener(this::d);
  }

  private void b() {
    this.recyclerViewInterval.setAdapter(this.intervalAdapter);
    this.recyclerViewInterval.setLayoutManager(new LinearLayoutManager(getActivity()));
    this.intervalAdapter.notifyDataSetChanged();

    this.frameLayoutDone.setOnClickListener(view -> {
      if (this.selectIntervalListener != null)
        this.selectIntervalListener.onIntervalSelected(this.selectedInterval);
      this.dismissDialog();
    });

    this.frameLayoutCancel.setOnClickListener(view -> this.dismissDialog());
  }

  private void c() {
    Font.setGilroyBold(
        this.textViewInterval
    );
    Font.setSofiaProLight(
        this.textViewCancel,
        this.textViewDone
    );
    Font.setBoldStyle(
        this.textViewCancel,
        this.textViewDone
    );
  }

  private void d(int interval) {
    this.selectedInterval = interval;
  }

  private void dismissDialog() {
    if (this.getDialog() != null && this.getDialog().isShowing())
      this.getDialog().dismiss();
  }
}
