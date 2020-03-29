package com.bonfire.home.utils;

import android.graphics.Typeface;
import android.widget.TextView;

public class Font {
  public static void setGilroyBold(TextView textView) {
    a(textView, "gilroy_extra_bold.otf");
  }

  public static void setGilroyLight(TextView textView) {
    a(textView, "gilroy_light.otf");
  }

  public static void setSofiaProLight(TextView textView) {
    a(textView, "sofiapro_light.otf");
  }

  public static void setSofiaProLight(TextView... textViews) {
    for (TextView a : textViews) {
      if (a != null) {
        a(a, "sofiapro_light.otf");
      }
    }
  }

  public static void setGilroyBold(TextView... textViews) {
    for (TextView a : textViews) {
      if (a != null) {
        a(a, "gilroy_extra_bold.otf");
      }
    }
  }

  private static void a(TextView a, String b) {
    if (a != null && a.getContext() != null) {
      Typeface c = Typeface.createFromAsset(a.getContext().getAssets(), "fonts/" + b);
      if (c != null)
        a.setTypeface(c);
    }
  }

  public static void setBoldStyle(TextView textView) {
    if (textView != null) {
      textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
    }
  }

  public static void setBoldStyle(TextView... textViews) {
    for (TextView a : textViews) {
      if (a != null) {
        a.setTypeface(a.getTypeface(), Typeface.BOLD);
      }
    }
  }
}
