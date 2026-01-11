package coffee.khyonieheart.bouquet.math;

public class Vector4
{
	private float x, y, z, w;

	public Vector4(
		float x,
		float y,
		float z,
		float w
	) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public float[] toArray()
	{
		return new float[] { x, y, z, w };
	}
}
