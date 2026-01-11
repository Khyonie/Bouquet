package coffee.khyonieheart.bouquet.gl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.lwjgl.opengl.GL30;

import coffee.khyonieheart.bouquet.math.Matrix4;
import coffee.khyonieheart.bouquet.math.Vector4;

public class GlShader implements AutoCloseable
{
	private int handle;

	private Map<String, Integer> uniformLocations = new HashMap<>();

	public GlShader(
		String vertexSource,
		String fragmentSource
	) {
		int vertexShader = GL30.glCreateShader(GL30.GL_VERTEX_SHADER);
		int fragmentShader = GL30.glCreateShader(GL30.GL_FRAGMENT_SHADER);

		System.out.print("Creating new shader... ");
		GL30.glShaderSource(vertexShader, vertexSource);
		GL30.glCompileShader(vertexShader);
		if (GL30.glGetShaderi(vertexShader, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE)
		{
			System.out.println("Failed to compile vertex shader");
			throw new IllegalStateException("Failed to compile vertex shader: " + GL30.glGetShaderInfoLog(vertexShader));
		}
		System.out.print("Created and compiled vertex shader... ");

		GL30.glShaderSource(fragmentShader, fragmentSource);
		GL30.glCompileShader(fragmentShader);
		if (GL30.glGetShaderi(fragmentShader, GL30.GL_COMPILE_STATUS) == GL30.GL_FALSE)
		{
			System.out.println("Failed to compile fragment shader");
			throw new IllegalStateException("Failed to compile fragment shader: " + GL30.glGetShaderInfoLog(fragmentShader));
		}
		System.out.print("Created and compiled fragment shader, linking... ");

		this.handle = GL30.glCreateProgram();
		GL30.glAttachShader(this.handle, vertexShader);
		GL30.glAttachShader(this.handle, fragmentShader);
		GL30.glLinkProgram(this.handle);

		System.out.println("Done, ID is " + this.handle);
	}

	public GlShader bind()
	{
		GL30.glUseProgram(this.handle);

		return this;
	}

	public static void unbind()
	{
		GL30.glUseProgram(0);
	}

	public int getUniformlocation(
		String name
	)
		throws IllegalArgumentException
	{
		// Check cache
		if (this.uniformLocations.containsKey(name))
		{
			return this.uniformLocations.get(name);
		}

		int uniform = GL30.glGetUniformLocation(this.handle, name);
		if (uniform == -1)
		{
			throw new IllegalStateException("No such uniform \"" + name + "\" in shader #" + this.handle);
		}

		System.out.println("Caching uniform \"" + name + "\" with location " + uniform + " for shader " + this.handle);
		this.uniformLocations.put(name, uniform);
		return uniform;
	}

	public GlShader setUniformMat4(
		String name,
		Matrix4 mat4,
		boolean transpose
	) {
		GL30.glUniformMatrix4fv(this.getUniformlocation(name), transpose, mat4.toArray());
		return this;
	}

	public GlShader setUniformVec4(
		String name,
		Vector4 vec4
	) {
		GL30.glUniform4fv(this.getUniformlocation(name), vec4.toArray());
		return this;
	}

	public GlShader setUniformVec4(
		String name,
		float x,
		float y,
		float z,
		float w
	) {
		GL30.glUniform4f(this.getUniformlocation(name), x, y, z, w);
		return this;
	}

	public GlShader setUniformVec3(
		String name,
		float x,
		float y,
		float z
	) {
		GL30.glUniform3f(this.getUniformlocation(name), x, y, z);
		return this;
	}

	public GlShader setUniformVec2(
		String name,
		float x,
		float y
	) {
		GL30.glUniform2f(this.getUniformlocation(name), x, y);
		return this;
	}

	public GlShader setUniformInt1(
		String name,
		int i
	) {
		GL30.glUniform1i(this.getUniformlocation(name), i);
		return this;
	}

	public GlShader setUniformFloat1(
		String name,
		float f
	) {
		GL30.glUniform1f(this.getUniformlocation(name), f);
		return this;
	}

	@Override
	public void close() throws Exception 
	{
		if (this.handle == 0)
		{
			return;
		}
		System.out.println("Deleting shader " + this.handle);
		GL30.glDeleteProgram(this.handle);
	}

	/**
	 * Creates and loads a shader from internally stored shadercode files.
	 */
	public static GlShader loadInternal(
		String vShaderPath,
		String fShaderPath
	) {
		File file = new File(System.getProperty("java.class.path").split(File.pathSeparator)[0]);
		String vShaderSource, fShaderSource;
		try (JarFile jar = new JarFile(file))
		{
			JarEntry vEntry = jar.getJarEntry(vShaderPath);
			if (vEntry == null)
			{
				throw new IOException("No such file \"" + vShaderPath + "\"");
			}
			vShaderSource = new String(jar.getInputStream(vEntry).readAllBytes());

			JarEntry fEntry = jar.getJarEntry(fShaderPath);
			if (fEntry == null)
			{
				throw new IOException("No such file \"" + fShaderPath + "\"");
			}
			fShaderSource = new String(jar.getInputStream(fEntry).readAllBytes());
		} catch (IOException e) {
			throw new IllegalStateException("Failed to load internal shader", e);
		}
		return new GlShader(vShaderSource, fShaderSource);
	}
}
