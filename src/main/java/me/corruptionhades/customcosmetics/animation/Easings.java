package me.corruptionhades.customcosmetics.animation;

import static java.lang.Math.*;

/**
 * @author sixfalls#0001 (github.com/6ixfalls) for the original, SIMULATAN (github.com/SIMULATAN) for the updated version with enums and the advanced bouncy easings
 */
public class Easings {

	public static float linear(float x) {
		return x;
	}

	public static float easeInSine(float x) {
		return (float) (1 - cos((x * PI) / 2));
	}

	public static float easeOutSine(float x) {
		return (float) (sin((x * PI) / 2));
	}

	public static float easeInOutSine(float x) {
		return (float) (-(cos(PI * x) - 1) / 2);
	}

	public static float easeInCubic(float x) {
		return (float) (x * x * x);
	}

	public static float easeOutCubic(float x) {
		return (float) (1 - pow(1 - x, 3));
	}

	public static float easeInOutCubic(float x) {
		return (float) (x < 0.5 ? 4 * x * x * x : 1 - pow(-2 * x + 2, 3) / 2);
	}

	public static float easeInQuint(float x) {
		return (float) (x * x * x * x * x);
	}

	public static float easeOutQuint(float x) {
		return (float) (1 - pow(1 - x, 5));
	}

	public static float easeInOutQuint(float x) {
		return (float) (x < 0.5 ? 16 * x * x * x * x * x : 1 - pow(-2 * x + 2, 5) / 2);
	}

	public static float easeInCirc(float x) {
		return (float) (1 - sqrt(1 - pow(x, 2)));
	}

	public static float easeOutCirc(float x) {
		return (float) (sqrt(1 - pow(x - 1, 2)));
	}

	public static float easeInOutCirc(float x) {
		return (float) (x < 0.5 ? (1 - sqrt(1 - pow(2 * x, 2))) / 2 : (sqrt(1 - pow(-2 * x + 2, 2)) + 1) / 2);
	}
	
	public static float easeInElastic(float x) {
		if (x <= 0) return 0;
		if (x >= 1) return 1;
		return (float) (-pow(2,  10 * x - 10) * sin((x * 10 - 10.75) * ((2 * PI) / 3)));
	}
	
	public static float easeOutElastic(float x) {
		if (x <= 0) return 0;
		if (x >= 1) return 1;
		return (float) (pow(2, -10 * x) * sin((x * 10 - 0.75) * ((2 * PI) / 3)) + 1);
	}
	
	public static float easeInOutElastic(float x) {
		if (x <= 0) return 0;
		if (x >= 1) return 1;
		double sinCalc = sin((20 * x - 11.125) * ((2 * PI) / 4.5));
		return (float) (x < 0.5 ? -(pow(2, 20 * x - 10) * sinCalc) / 2 : (pow(2, -20 * x + 10) * sinCalc) / 2 + 1);
	}

	public static float easeInQuad(float x) {
		return x * x;
	}

	public static float easeOutQuad(float x) {
		return 1 - (1 - x) * (1 - x);
	}

	public static float easeInOutQuad(float x) {
		return (float) (x < 0.5 ? 2 * x * x : 1 - pow(-2 * x + 2, 2) / 2);
	}

	public static float easeInQuart(float x) {
		return x * x * x * x;
	}

	public static float easeOutQuart(float x) {
		return (float) (1 - pow(1 - x, 4));
	}

	public static float easeInOutQuart(float x) {
		return (float) (x < 0.5 ? 8 * x * x * x * x : 1 - pow(-2 * x + 2, 4) / 2);
	}

	public static float easeInExponential(float x) {
		return (float) (x == 0 ? 0 : pow(2, 10 * x - 10));
	}

	public static float easeOutExponential(float x) {
		return (float) (x == 1 ? 1 : 1 - pow(2, -10 * x));
	}

	public static float easeInOutExponential(float x) {
		return (float) (x == 0 ? 0 : x == 1 ? 1 : x < 0.5 ? pow(2, 20 * x - 10) / 2 : (2 - pow(2, -20 * x + 10)) / 2);
	}
	
	public static float easeInBack(float x) {
		float c1 = 1.70158F;
		return (c1 + 1) * x * x * x - c1 * x * x;
	}
	
	public static float easeOutBack(float x) {
		float c1 = 1.70158F;
		return (float) (1 + (c1 + 1) * pow(x - 1, 3) + c1 * pow(x - 1, 2));
	}
	
	public static float easeInOutBack(float x) {
		float c1 = 1.70158F;
		float c2 = c1 * 1.525F;
		
		return (float) (x < 0.5 ? (pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2 : (pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2);
	}
	
	public static float easeInBounce(float x) {
		return 1 - easeOutBounce(1 - x);
	}
	
	public static float easeOutBounce(float x) {
		float n1 = 7.5625F;
		float d1 = 2.75F;

		if (x < 1 / d1) {
		    return n1 * x * x;
		} else if (x < 2 / d1) {
		    return (float) (n1 * (x -= (float) (1.5 / d1)) * x + 0.75);
		} else if (x < 2.5 / d1) {
		    return (float) (n1 * (x -= (float) (2.25 / d1)) * x + 0.9375);
		} else {
		    return (float) (n1 * (x -= (float) (2.625 / d1)) * x + 0.984375);
		}
	}
	
	public static float easeInOutBounce(float x) {
		return x < 0.5 ? (1 - easeOutBounce(1 - 2 * x)) / 2 : (1 + easeOutBounce(2 * x - 1)) / 2;
	}
	
	public static float getEasingValue(float x, Easing easing) {
		if (x <= 0) return 0;
		if (x >= 1) return 1;
        return switch (easing) {
            case LINEAR -> linear(x);
            case EASE_IN_SINE -> easeInSine(x);
            case EASE_OUT_SINE -> easeOutSine(x);
            case EASE_IN_OUT_SINE -> easeInOutSine(x);
            case EASE_IN_CUBIC -> easeInCubic(x);
            case EASE_OUT_CUBIC -> easeOutCubic(x);
            case EASE_IN_OUT_CUBIC -> easeInOutCubic(x);
            case EASE_IN_QUINT -> easeInQuint(x);
            case EASE_OUT_QUINT -> easeOutQuint(x);
            case EASE_IN_OUT_QUINT -> easeInOutQuint(x);
            case EASE_IN_CIRC -> easeInCirc(x);
            case EASE_OUT_CIRC -> easeOutCirc(x);
            case EASE_IN_OUT_CIRC -> easeInOutCirc(x);
            case EASE_IN_ELASTIC -> easeInElastic(x);
            case EASE_OUT_ELASTIC -> easeOutElastic(x);
            case EASE_IN_OUT_ELASTIC -> easeInOutElastic(x);
            case EASE_IN_QUAD -> easeInQuad(x);
            case EASE_OUT_QUAD -> easeOutQuad(x);
            case EASE_IN_OUT_QUAD -> easeInOutQuad(x);
            case EASE_IN_QUART -> easeInQuart(x);
            case EASE_OUT_QUART -> easeOutQuart(x);
            case EASE_IN_OUT_QUART -> easeInOutQuart(x);
            case EASE_IN_EXPONENTIAL -> easeInExponential(x);
            case EASE_OUT_EXPONENTIAL -> easeOutExponential(x);
            case EASE_IN_OUT_EXPONENTIAL -> easeInOutExponential(x);
            case EASE_IN_BACK -> easeInBack(x);
            case EASE_OUT_BACK -> easeOutBack(x);
            case EASE_IN_OUT_BACK -> easeInOutBack(x);
            case EASE_IN_BOUNCE -> easeInBounce(x);
            case EASE_OUT_BOUNCE -> easeOutBounce(x);
            case EASE_IN_OUT_BOUNCE -> easeInOutBounce(x);
        };
	}
}