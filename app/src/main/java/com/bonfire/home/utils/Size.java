package com.bonfire.home.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class Size {
  public static float dpToPx(float dp, Context context) {
    if (context != null) {
      return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    } else {
      return 0;
    }
  }

  public static float pxToDp(float px, Context context) {
    if (context != null) {
      return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    } else {
      return 0;
    }
  }

  public static int getWidthPixels(Context context) {
    return context != null ? context.getResources().getDisplayMetrics().widthPixels : 0;
  }
}
