package coffee.khyonieheart.bouquet.engine;

import java.util.ArrayList;
import java.util.List;

import coffee.khyonieheart.bouquet.anim.RunningAnimation;

public class TweenManager
{
	private static List<RunningAnimation> activeAnimations = new ArrayList<>();

	public static void tick()
	{
		for (RunningAnimation anim : activeAnimations)
		{
			anim.getTarget().animate(anim.getProperty(), anim.getInterpolated());
		}
	}
}
