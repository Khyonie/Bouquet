package coffee.khyonieheart.bouquet.math;

public class Matrix4
{
	private float 
		m00, m01, m02, m03,
		m10, m11, m12, m13,
		m20, m21, m22, m23,
		m30, m31, m32, m33;

	public Matrix4(
		float m00, float m01, float m02, float m03,
		float m10, float m11, float m12, float m13,
		float m20, float m21, float m22, float m23,
		float m30, float m31, float m32, float m33
	) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;

		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;

		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;

		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	public static Matrix4 identity()
	{
		return new Matrix4(
			1f, 0f, 0f, 0f,
			0f, 1f, 0f, 0f,
			0f, 0f, 1f, 0f,
			0f, 0f, 0f, 1f
		);
	}

	public static Matrix4 zero()
	{
		return new Matrix4(
			0f, 0f, 0f, 0f,
			0f, 0f, 0f, 0f,
			0f, 0f, 0f, 0f,
			0f, 0f, 0f, 0f
		);
	}

	public static Matrix4 ofTransform(
		float translateX,
		float translateY,
		float scaleX,
		float scaleY
	) {
		return new Matrix4(
			scaleX, 0f, 0f, translateX,
			0f, scaleY, 0f, translateY,
			0f, 0f, 1f, 0f,
			0f, 0f, 0f, 1f
		);
	}

	public static Matrix4 orthographic(
		float left,
		float right,
		float top,
		float bottom,
		float near,
		float far
	) {
		return new Matrix4(
			2 / (right - left), 0f, 0f, ((right + left) / (right - left)) * -1f,
			0f, 2 / (top - bottom), 0f, ((top + bottom) / (top - bottom)) * -1f,
			0f, 0f, -2f / (far - near), ((far + near) / (far - near)) * -1f,
			0f, 0f, 0f, 1f
		);
	}

	public Matrix4 translate(
		float x,
		float y
	) {
		this.m03 = x;
		this.m13 = y;

		return this;
	}

	public Matrix4 scale(
		float sx,
		float sy
	) {
		this.m00 = sx;
		this.m11 = sy;

		return this;
	}

	public float[] toArray()
	{
		return new float[] {
			m00, m01, m02, m03,
			m10, m11, m12, m13,
			m20, m21, m22, m23,
			m30, m31, m32, m33
		};
	}

	@Override
	public String toString()
	{
		return "[" + m00 + ", " + m01 + ", " + m02 + ", " + m03 + "]\n" 
			 + "[" + m10 + ", " + m11 + ", " + m12 + ", " + m13 + "]\n" 
			 + "[" + m20 + ", " + m21 + ", " + m22 + ", " + m23 + "]\n" 
			 + "[" + m30 + ", " + m31 + ", " + m32 + ", " + m33 + "]";
	}
}
