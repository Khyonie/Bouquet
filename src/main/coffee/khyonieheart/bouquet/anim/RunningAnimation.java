package coffee.khyonieheart.bouquet.anim;

import java.util.function.Function;

public class RunningAnimation 
{
	/** Time range. */
	private double min, max;
	private int start, end;
	/** Current time. */
	private double time;
	/** Rate of change from one tick to the next. */
	private double delta;
	/** Function used to calculate interpolation. */
	private Function<Double, Double> interpolationFunction;
	private AnimationProperty property;
	private Animatable target;

	public RunningAnimation(
		double min,
		double max,
		int start,
		int end,
		double initialTime,
		double delta,
		Function<Double, Double> interpolationFunction,
		Animatable target,
		AnimationProperty property
	) {
		this.min = min;
		this.max = max;
		this.start = start;
		this.end = end;
		this.time = initialTime;
		this.delta = delta;
		this.interpolationFunction = interpolationFunction;
		this.target = target;
		this.property = property;
	}

	public AnimationProperty getProperty()
	{
		return this.property;
	}

	public Animatable getTarget() 
	{
		return target;
	}
	
	public int getInterpolated()
	{
		int interpolatedValue = this.property.interpolate(start, end, interpolationFunction.apply(time));
		this.time = Math.max(0f, Math.min(1f, this.time + this.delta));
		return interpolatedValue;
	}
}
