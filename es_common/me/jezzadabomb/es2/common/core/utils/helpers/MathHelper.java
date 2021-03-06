package me.jezzadabomb.es2.common.core.utils.helpers;

public class MathHelper {

    public static float expandAwayFrom(float value, float secondaryValue) {
        if (value < 0) {
            return value - secondaryValue;
        } else if (value > 0) {
            return value + secondaryValue;
        }
        return value;
    }

    public static boolean withinValues(float value, float lowerBound, float upperBound) {
        return lowerBound < value && value < upperBound;
    }

    public static double pythagoras(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    public static double pythagoras(float a, float b) {
        return Math.sqrt(a * a + b * b);
    }

    public static int pythagoras(int a, int b) {
        return net.minecraft.util.MathHelper.truncateDoubleToInt((Math.sqrt(a * a + b * b)));
    }

    public static boolean withinRange(double value, double target, double tolerance) {
        return value >= (target - tolerance) && value <= (target + tolerance);
    }

    public static boolean withinRange(double value, double tolerance) {
        return value >= (0.0D - tolerance) && value <= (0.0D + tolerance);
    }

    public static boolean withinRange(float value, float target, float tolerance) {
        return value >= (target - tolerance) && value <= (target + tolerance);
    }

    public static boolean withinRange(int value, int target, int tolerance) {
        return value >= (target - tolerance) && value <= (target + tolerance);
    }

    public static double clipDouble(double value, double min, double max) {
        if (value > max)
            value = max;
        if (value < min)
            value = min;
        return value;
    }

    public static float clipFloat(float value, float min, float max) {
        if (value > max)
            value = max;
        if (value < min)
            value = min;
        return value;
    }

    public static int clipInt(int value, int min, int max) {
        if (value > max)
            value = max;
        if (value < min)
            value = min;
        return value;
    }

    public static int clipInt(int value, int max) {
        if (value > max)
            value = max;
        if (value < 0)
            value = 0;
        return value;
    }

    public static double interpolate(double a, double b, double d) {
        return a + (b - a) * d;
    }

    public static float interpolate(float a, float b, float d) {
        return a + (b - a) * d;
    }
}
