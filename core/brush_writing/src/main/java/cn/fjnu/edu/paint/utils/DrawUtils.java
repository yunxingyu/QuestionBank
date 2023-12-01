package cn.fjnu.edu.paint.utils;

import android.graphics.Path;

/* loaded from: classes.dex */
public class DrawUtils {
    private DrawUtils() {
    }

    public static void a(int i2, Path path, float f2, float f3, float f4, float f5) {
        float min = Math.min(f2, f4);
        float max = Math.max(f2, f4);
        float min2 = Math.min(f3, f5);
        float max2 = Math.max(f3, f5);
        path.reset();
        float f6 = max2 - min2;
        float f7 = max - min;
        if (i2 == 28) {
            double d2 = (double) max;
            float f8 = 0.25f * f6;
            double d3 = (double) f8;
            float sqrt = (float) (d2 - (Math.sqrt(3.0d) * d3));
            float f9 = f8 + min2;
            path.moveTo(min, f9);
            path.lineTo(sqrt, f9);
            path.lineTo(sqrt, min2);
            float f10 = (0.5f * f6) + min2;
            path.lineTo(max, f10);
            float sqrt2 = (float) (d2 - (d3 * Math.sqrt(3.0d)));
            float f11 = min2 + (f6 * 0.75f);
            path.moveTo(min, f11);
            path.lineTo(sqrt2, f11);
            path.lineTo(sqrt2, max2);
            path.lineTo(max, f10);
            path.moveTo(min, f9);
            path.lineTo(min, f11);
        } else if (i2 == 29) {
            double d4 = (double) min;
            float f12 = 0.25f * f6;
            double d5 = (double) f12;
            float sqrt3 = (float) (d4 + (Math.sqrt(3.0d) * d5));
            float f13 = f12 + min2;
            path.moveTo(max, f13);
            path.lineTo(sqrt3, f13);
            path.lineTo(sqrt3, min2);
            float f14 = (f6 * 0.5f) + min2;
            path.lineTo(min, f14);
            float sqrt4 = (float) (d4 + (d5 * Math.sqrt(3.0d)));
            float f15 = min2 + (f6 * 0.75f);
            path.moveTo(max, f15);
            path.lineTo(sqrt4, f15);
            path.lineTo(sqrt4, max2);
            path.lineTo(min, f14);
            path.moveTo(max, f13);
            path.lineTo(max, f15);
        } else if (i2 == 30) {
            float f16 = 0.25f * f7;
            float f17 = min + f16;
            double d6 = (double) max2;
            double d7 = (double) f16;
            float sqrt5 = (float) (d6 - (Math.sqrt(3.0d) * d7));
            path.moveTo(f17, min2);
            path.lineTo(f17, sqrt5);
            path.lineTo(min, sqrt5);
            float f18 = (0.5f * f7) + min;
            path.lineTo(f18, max2);
            float f19 = min + (f7 * 0.75f);
            float sqrt6 = (float) (d6 - (d7 * Math.sqrt(3.0d)));
            path.moveTo(f19, min2);
            path.lineTo(f19, sqrt6);
            path.lineTo(max, sqrt6);
            path.lineTo(f18, max2);
            path.moveTo(f17, min2);
            path.lineTo(f19, min2);
        } else if (i2 == 31) {
            float f20 = 0.25f * f7;
            float f21 = min + f20;
            double d8 = (double) min2;
            double d9 = (double) f20;
            float sqrt7 = (float) ((Math.sqrt(3.0d) * d9) + d8);
            path.moveTo(f21, max2);
            path.lineTo(f21, sqrt7);
            path.lineTo(min, sqrt7);
            float f22 = (0.5f * f7) + min;
            path.lineTo(f22, min2);
            float f23 = min + (f7 * 0.75f);
            float sqrt8 = (float) (d8 + (d9 * Math.sqrt(3.0d)));
            path.moveTo(f23, max2);
            path.lineTo(f23, sqrt8);
            path.lineTo(max, sqrt8);
            path.lineTo(f22, min2);
            path.moveTo(f21, max2);
            path.lineTo(f23, max2);
        }
    }
}
