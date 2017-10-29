package tk.nukeduck.vivivox.world;

public class NoiseGenerator {
	public enum NoiseQuality
	{
		/** Generates coherent noise quickly. When a coherent-noise function with
		 * this quality setting is used to generate a bump-map image, there are
		 * noticeable "creasing" artifacts in the resulting image. This is
		 * because the derivative of that function is discontinuous at integer
		 * boundaries.
		 */
		QUALITY_FAST,

		/// Generates standard-quality coherent noise. When a coherent-noise
		/// function with this quality setting is used to generate a bump-map
		/// image, there are some minor "creasing" artifacts in the resulting
		/// image. This is because the second derivative of that function is
		/// discontinuous at integer boundaries.
		QUALITY_STD,

		/// Generates the best-quality coherent noise. When a coherent-noise
		/// function with this quality setting is used to generate a bump-map
		/// image, there are no "creasing" artifacts in the resulting image. This
		/// is because the first and second derivatives of that function are
		/// continuous at integer boundaries.
		QUALITY_BEST
	}
	
	static final int X_NOISE_GEN = 1619;
	static final int Y_NOISE_GEN = 31337;
	static final int Z_NOISE_GEN = 6971;
	static final int SEED_NOISE_GEN = 1013;
	static final int SHIFT_NOISE_GEN = 8;
	
	public static double GradientCoherentNoise3D (double x, double y, double z, int seed, NoiseQuality noiseQuality)
	{
		// Create a unit-length cube aligned along an integer boundary. This cube
		// surrounds the input point.
		int x0 = (x > 0.0? (int)x: (int)x - 1);
		int x1 = x0 + 1;
		int y0 = (y > 0.0? (int)y: (int)y - 1);
		int y1 = y0 + 1;
		int z0 = (z > 0.0? (int)z: (int)z - 1);
		int z1 = z0 + 1;

		// Map the difference between the coordinates of the input value and the
		// coordinates of the cube's outer-lower-left vertex onto an S-curve.
		double xs = 0, ys = 0, zs = 0;
		switch (noiseQuality)
		{
		case QUALITY_FAST:
			xs = (x - (double)x0);
			ys = (y - (double)y0);
			zs = (z - (double)z0);
			break;
		case QUALITY_STD:
			xs = SCurve3 (x - (double)x0);
			ys = SCurve3 (y - (double)y0);
			zs = SCurve3 (z - (double)z0);
			break;
		case QUALITY_BEST:
			xs = SCurve5 (x - (double)x0);
			ys = SCurve5 (y - (double)y0);
			zs = SCurve5 (z - (double)z0);
			break;
		}

		// Now calculate the noise values at each vertex of the cube. To generate
		// the coherent-noise value at the input point, interpolate these eight
		// noise values using the S-curve value as the interpolant (trilinear
		// interpolation.)
		double n0, n1, ix0, ix1, iy0, iy1;
		n0 = GradientNoise3D (x, y, z, x0, y0, z0, seed);
		n1 = GradientNoise3D (x, y, z, x1, y0, z0, seed);
		ix0 = linearInterp (n0, n1, xs);
		n0 = GradientNoise3D (x, y, z, x0, y1, z0, seed);
		n1 = GradientNoise3D (x, y, z, x1, y1, z0, seed);
		ix1 = linearInterp (n0, n1, xs);
		iy0 = linearInterp (ix0, ix1, ys);
		n0 = GradientNoise3D (x, y, z, x0, y0, z1, seed);
		n1 = GradientNoise3D (x, y, z, x1, y0, z1, seed);
		ix0 = linearInterp (n0, n1, xs);
		n0 = GradientNoise3D (x, y, z, x0, y1, z1, seed);
		n1 = GradientNoise3D (x, y, z, x1, y1, z1, seed);
		ix1 = linearInterp (n0, n1, xs);
		iy1 = linearInterp (ix0, ix1, ys);

		return linearInterp (iy0, iy1, zs);
	}

	public static double GradientNoise3D(double fx, double fy, double fz, int ix, int iy, int iz, int seed)
	{
		// Randomly generate a gradient vector given the integer coordinates of the
		// input value. This implementation generates a random number and uses it
		// as an index into a normalized-vector lookup table.
		int vectorIndex = (X_NOISE_GEN * ix
				+ Y_NOISE_GEN * iy
				+ Z_NOISE_GEN * iz
				+ SEED_NOISE_GEN * seed)
				& 0xffffffff;
		
		vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
		vectorIndex &= 0xff;

		double xvGradient = VectorTable.getRandomVectors(vectorIndex, 0);
		double yvGradient = VectorTable.getRandomVectors(vectorIndex, 1);
		double zvGradient = VectorTable.getRandomVectors(vectorIndex, 2);
		// array size too large when using this original, changed to above for all 3
		// double zvGradient = vectorTable.getRandomVectors(vectorIndex << 2, 2);

		// Set up us another vector equal to the distance between the two vectors
		// passed to this function.
		double xvPoint = (fx - (double)ix);
		double yvPoint = (fy - (double)iy);
		double zvPoint = (fz - (double)iz);

		// Now compute the dot product of the gradient vector with the distance
		// vector. The resulting value is gradient noise. Apply a scaling value
		// so that this noise value ranges from -1.0 to 1.0.
		return ((xvGradient * xvPoint)
				+ (yvGradient * yvPoint)
				+ (zvGradient * zvPoint)) * 2.12;
	}

	public static int IntValueNoise3D (int x, int y, int z, int seed)
	{
		// All constants are primes and must remain prime in order for this noise
		// function to work correctly.
		int n = (X_NOISE_GEN * x
				+ Y_NOISE_GEN * y
				+ Z_NOISE_GEN * z
				+ SEED_NOISE_GEN * seed)
				& 0x7fffffff;

		n = (n >> 13) ^ n;

		return (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
	}

	public static double ValueCoherentNoise3D (double x, double y, double z, int seed,
			NoiseQuality noiseQuality)
	{
		// Create a unit-length cube aligned along an integer boundary. This cube
		// surrounds the input point.
		int x0 = (x > 0.0? (int)x: (int)x - 1);
		int x1 = x0 + 1;
		int y0 = (y > 0.0? (int)y: (int)y - 1);
		int y1 = y0 + 1;
		int z0 = (z > 0.0? (int)z: (int)z - 1);
		int z1 = z0 + 1;

		// Map the difference between the coordinates of the input value and the
		// coordinates of the cube's outer-lower-left vertex onto an S-curve.
		double xs = 0, ys = 0, zs = 0;
		switch (noiseQuality)
		{
		case QUALITY_FAST:
			xs = (x - (double)x0);
			ys = (y - (double)y0);
			zs = (z - (double)z0);
			break;
		case QUALITY_STD:
			xs = SCurve3 (x - (double)x0);
			ys = SCurve3 (y - (double)y0);
			zs = SCurve3 (z - (double)z0);
			break;
		case QUALITY_BEST:
			xs = SCurve5 (x - (double)x0);
			ys = SCurve5 (y - (double)y0);
			zs = SCurve5 (z - (double)z0);
			break;
		}

		// Now calculate the noise values at each vertex of the cube. To generate
		// the coherent-noise value at the input point, interpolate these eight
		// noise values using the S-curve value as the interpolant (trilinear
		// interpolation.)
		double n0, n1, ix0, ix1, iy0, iy1;
		n0 = ValueNoise3D (x0, y0, z0, seed);
		n1 = ValueNoise3D (x1, y0, z0, seed);
		ix0 = linearInterp (n0, n1, xs);
		n0 = ValueNoise3D (x0, y1, z0, seed);
		n1 = ValueNoise3D (x1, y1, z0, seed);
		ix1 = linearInterp (n0, n1, xs);
		iy0 = linearInterp (ix0, ix1, ys);
		n0 = ValueNoise3D (x0, y0, z1, seed);
		n1 = ValueNoise3D (x1, y0, z1, seed);
		ix0 = linearInterp (n0, n1, xs);
		n0 = ValueNoise3D (x0, y1, z1, seed);
		n1 = ValueNoise3D (x1, y1, z1, seed);
		ix1 = linearInterp (n0, n1, xs);
		iy1 = linearInterp (ix0, ix1, ys);

		return linearInterp (iy0, iy1, zs);
	}

	public static double ValueNoise3D (int x, int y, int z, int seed)
	{
		return 1.0 - ((double)IntValueNoise3D (x, y, z, seed) / 1073741824.0);
	}

	/// Modifies a floating-point value so that it can be stored in a
	/// int32 variable.
	///
	/// @param n A floating-point number.
	///
	/// @returns The modified floating-point number.
	///
	/// This function does not modify @a n.
	///
	/// In libnoise, the noise-generating algorithms are all integer-based;
	/// they use variables of type int32. Before calling a noise
	/// function, pass the @a x, @a y, and @a z coordinates to this function to
	/// ensure that these coordinates can be cast to a int32 value.
	///
	/// Although you could do a straight cast from double to int32, the
	/// resulting value may differ between platforms. By using this function,
	/// you ensure that the resulting value is identical between platforms.
	public static double MakeInt32Range (double n)
	{
		if (n >= 1073741824.0)
			return (2.0 * (n % 1073741824.0)) - 1073741824.0;
		else if (n <= -1073741824.0)
			return (2.0 * (n % 1073741824.0)) + 1073741824.0;
		else
			return n;
	}

	public static double cubicInterp (double n0, double n1, double n2, double n3,
			double a)
	{
		double p = (n3 - n2) - (n0 - n1);
		double q = (n0 - n1) - p;
		double r = n2 - n0;
		double s = n1;
		return p * a * a * a + q * a * a + r * a + s;
	}

	/// Performs linear interpolation between two values.
	///
	/// @param n0 The first value.
	/// @param n1 The second value.
	/// @param a The alpha value.
	///
	/// @returns The interpolated value.
	///
	/// The alpha value should range from 0.0 to 1.0. If the alpha value is
	/// 0.0, this function returns @a n0. If the alpha value is 1.0, this
	/// function returns @a n1.
	public static double linearInterp (double n0, double n1, double a)
	{
		return ((1.0 - a) * n0) + (a * n1);
	}

	/// Maps a value onto a cubic S-curve.
	///
	/// @param a The value to map onto a cubic S-curve.
	///
	/// @returns The mapped value.
	///
	/// @a a should range from 0.0 to 1.0.
	///
	/// The derivative of a cubic S-curve is zero at @a a = 0.0 and @a a =
	/// 1.0
	public static double SCurve3 (double a)
	{
		return (a * a * (3.0 - 2.0 * a));
	}

	/// Maps a value onto a quintic S-curve.
	///
	/// @param a The value to map onto a quintic S-curve.
	///
	/// @returns The mapped value.
	///
	/// @a a should range from 0.0 to 1.0.
	///
	/// The first derivative of a quintic S-curve is zero at @a a = 0.0 and
	/// @a a = 1.0
	///
	/// The second derivative of a quintic S-curve is zero at @a a = 0.0 and
	/// @a a = 1.0
	static double SCurve5 (double a)
	{
		double a3 = a * a * a;
		double a4 = a3 * a;
		double a5 = a4 * a;
		return (6.0 * a5) - (15.0 * a4) + (10.0 * a3);
	}
}
