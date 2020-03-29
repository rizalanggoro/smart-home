package com.bonfire.home.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class AlphaAnimation {
  private View view;
  private int animationDuration = 250;

  public AlphaAnimation(int duration) {
    this.animationDuration = duration;
  }

  public AlphaAnimation() {

  }

  public void animateAlpha(View view, boolean isVisible) {
    if (view != null) {
      this.view = view;
      if (isVisible) {
        if (this.view.getVisibility() == View.GONE) {
          this.view.setVisibility(View.VISIBLE);
          if (this.view.getAlpha() != 0.0f) view.setAlpha(0.0f);
          view.animate().alpha(1.0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              AlphaAnimation.this.view.setVisibility(View.VISIBLE);
            }
          });
        }
      } else {
        if (this.view.getVisibility() == View.VISIBLE) {
          this.view.setVisibility(View.VISIBLE);
          if (this.view.getAlpha() != 1.0f) view.setAlpha(1.0f);
          view.animate().alpha(0.0f).setDuration(animationDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
              AlphaAnimation.this.view.setVisibility(View.GONE);
            }
          });
        }
      }
    }
  }
}
