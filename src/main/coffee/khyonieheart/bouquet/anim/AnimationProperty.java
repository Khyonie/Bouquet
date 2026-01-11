package coffee.khyonieheart.bouquet.anim;

import coffee.khyonieheart.bouquet.Color;

public enum AnimationProperty
{
	X_POS {
		@Override
		public int interpolate(int start, int end, double time)
		{
			float difference = (float) end - (float) start;
			return (int) Math.round(start + (difference * time));
		}
	},
	Y_POS {
		@Override
		public int interpolate(int start, int end, double time)
		{
			float difference = (float) end - (float) start;
			return (int) Math.round(start + (difference * time));
		}
	},
	WIDTH {
		@Override
		public int interpolate(int start, int end, double time)
		{
			float difference = (float) end - (float) start;
			return (int) Math.round(start + (difference * time));
		}
	},
	HEIGHT {
		@Override
		public int interpolate(int start, int end, double time)
		{
			float difference = (float) end - (float) start;
			return (int) Math.round(start + (difference * time));
		}
	},
	FG_COLOR {
		@Override
		public int interpolate(int start, int end, double time)
		{
			return Color.interpolate(start, end, time);
		}
	},
	BG_COLOR {
		@Override
		public int interpolate(int start, int end, double time)
		{
			return Color.interpolate(start, end, time);
		}
	};

	public abstract int interpolate(
		int start,
		int end,
		double time
	);
}
