package tk.nukeduck.vivivox.helper;

public class Vec3i {
    public static Vec3i ZERO = new Vec3i(0, 0, 0);

    public final int x, y, z;

    public Vec3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i add(Vec3i other) {
        return new Vec3i(x + other.x, y + other.y, z + other.z);
    }

    public Vec3i sub(Vec3i other) {
        return new Vec3i(x - other.x, y - other.y, z - other.z);
    }

    public Vec3i mul(double scale) {
        return new Vec3i((int)Math.round(x * scale), (int)Math.round(y * scale), (int)Math.round(z * scale));
    }

    public boolean in(Vec3i min, Vec3i max) {
        return x >= min.x && x < max.x && y >= min.y && y < max.y && z >= min.z && z < max.z;
    }

    @Override
    public String toString() {
        return x + ", " + y + ", " + z;
    }
}
