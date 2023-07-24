package app;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {
   public static double round(double value, int places) {
      BigDecimal bd = BigDecimal.valueOf(value);
      bd = bd.setScale(places, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }
   
   public static double[] round(double[] arr, int places) {
      for (int i = 0; i < arr.length; i ++) {
         BigDecimal bd = BigDecimal.valueOf(arr[i]);
         bd = bd.setScale(places, RoundingMode.HALF_UP);
         arr[i] = bd.doubleValue();
      }
      return arr;
   }
}