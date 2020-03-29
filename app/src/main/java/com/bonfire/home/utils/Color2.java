package com.bonfire.home.utils;

public class Color2 {
  public static int getChildColor(int parentColor, String color) {
    switch (parentColor) {
      case 0:
        return (Color.red(color));

      case 1:
        return (Color.pink(color));


      case 2:
        return (Color.purple(color));


      case 3:
        return (Color.deepPurple(color));


      case 4:
        return (Color.indigo(color));


      case 5:
        return (Color.blue(color));


      case 6:
        return (Color.lightBlue(color));


      case 7:
        return (Color.cyan(color));


      case 8:
        return (Color.teal(color));


      case 9:
        return (Color.green(color));


      case 10:
        return (Color.lightGreen(color));


      case 11:
        return (Color.lime(color));


      case 12:
        return (Color.yellow(color));


      case 13:
        return (Color.amber(color));


      case 14:
        return (Color.orange(color));


      case 15:
        return (Color.deepOrange(color));


      case 16:
        return (Color.brown(color));


      case 17:
        return (Color.grey(color));


      case 18:
        return (Color.blueGrey(color));


      default:
        return 0;
    }
  }
}
