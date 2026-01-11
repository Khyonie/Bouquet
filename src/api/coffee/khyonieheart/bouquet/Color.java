package coffee.khyonieheart.bouquet;

public class Color
{
	/**
	 * Calculates the perceived brightness of a color.
	 */
	public static float luminance(
		int color
	) {
		int red = color >>> 24;
		int green = (color >> 16) & 0x00FF0000;
		int blue = (color >> 8) & 0x0000FF00;
		return (0.2126f * red) + (0.7152f * green) + (0.0722f * blue);
	}

	/**
	 * Converts a 32-bit RGB integer into three HSV floats, with alpha preserved as channel 4.
	 */
	public static float[] rgbToHsv(
		int color
	) {
		float red = (float) (color >>> 24) / 255f;
		float blue = (float) ((color & 0x00FF0000) >>> 16) / 255f;
		float green = (float) ((color & 0x0000FF00) >>> 8) / 255f;
		float alpha = (float) (color & 0x000000FF) / 255f;

		float max = Math.max(red, Math.max(blue, green));
		float min = Math.min(red, Math.min(blue, green));
		float delta = max - min;

		float h = 0f;

		if (delta > 1e-6F)
		{
			if (max == red)
			{
				h = ((green - blue) / delta) % 6f;
			} else if (max == green) {
				h = ((blue - red) / delta) + 2f;
			} else {
				h = ((red - green) / delta) + 4f;
			}

			h *= 60;
			if (h < 0)
			{
				h += 360f;
			}
		}

		float s = (max == 0f) ? 0f : (delta / max);
		float v = max;

		return new float[] { h, s, v, alpha };
	}

	public static void hueShift(
		float[] hsv,
		float shiftDegrees
	) {
		hsv[0] = (hsv[0] + shiftDegrees) % 360f;
		if (hsv[0] < 0)
		{
			hsv[0] += 360f;
		}
	}

	public static int hsvToRgb(
		float[] hsv
	) {
		return hsvToRgb(hsv[0], hsv[1], hsv[2], hsv[3]);
	}

	/**
	 * Converts three HSV floats into a 32-bit RGBA color.
	 * <b>Alpha is set to 255 for this method.</b>
	 */
	public static int hsvToRgb(
		float h,
		float s,
		float v,
		float alpha
	) {
		float c = v * s;  
		float x = c * (1 - Math.abs((h / 60f) % 2 - 1));
		float m = v - c;

		float red = 0, green = 0, blue = 0;

		if (0 <= h && h < 60) 
		{
			red = c; 
			green = x; 
			blue = 0;
		} else if (60 <= h && h < 120) {
			red = x; 
			green = c; 
			blue = 0;
		} else if (120 <= h && h < 180) {
			red = 0; 
			green = c; 
			blue = x;
		} else if (180 <= h && h < 240) {
			red = 0; 
			green = x; 
			blue = c;
		} else if (240 <= h && h < 300) {
			red = x; 
			green = 0; 
			blue = c;
		} else {
			red = c; 
			green = 0; 
			blue = x;
		}

		int redInt = Math.round((red + m) * 255f);
		int greenInt = Math.round((green + m) * 255f);
		int blueInt = Math.round((blue + m) * 255f);
		int alphaInt = Math.round(alpha * 255f);

		int color = (redInt << 24) | (greenInt << 16) | (blueInt << 8) | alphaInt;
		return color;
	}

	public static int adjustRgb(
		int rgb,
		float shift,
		float saturation,
		float value
	) {
		float[] hsv = rgbToHsv(rgb);
		hueShift(hsv, shift);
		hsv[1] *= saturation;
		hsv[2] *= value;

		return hsvToRgb(hsv);
	}

	public static int interpolate(
		int start,
		int end,
		double time
	) {
		// Convert both to HSV
		float[] startHsv = rgbToHsv(start);
		float[] endHsv = rgbToHsv(end);
		
		// Get difference
		float differenceH = endHsv[0] - startHsv[0];
		float differenceS = endHsv[1] - startHsv[1];
		float differenceV = endHsv[2] - startHsv[2];
		float differenceA = endHsv[3] - startHsv[3];

		// Interpolate
		return hsvToRgb(
			(float) (startHsv[0] + (differenceH * time)), 
			(float) (startHsv[1] + (differenceS * time)), 
			(float) (startHsv[2] + (differenceV * time)), 
			(float) (startHsv[3] + (differenceA * time))
		);
	}
}
