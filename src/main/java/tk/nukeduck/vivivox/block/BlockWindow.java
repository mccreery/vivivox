package tk.nukeduck.vivivox.block;

import java.util.Iterator;

import tk.nukeduck.vivivox.helper.Vec3i;
import tk.nukeduck.vivivox.world.BlockState;

public class BlockWindow implements Iterable<BlockState> {
    private final BlockView parent;
    private final Vec3i min, max;

    public BlockWindow(BlockView parent, Vec3i min, Vec3i max) {
        this.parent = parent;
        this.min = min;
        this.max = max;
    }

    public BlockWindow move(Vec3i offset) {
        return new BlockWindow(parent, min.add(offset), max.add(offset));
    }

    @Override
    public Iterator<BlockState> iterator() {
        return new It();
    }

    public float getAverageLight() {
        float light = 0;
        int count = 0;

        for(BlockState state : this) {
            if(!state.getBlock().isOpaque()) {
                light += state.getLightLevel();
                ++count;
            }
        }

        if(count > 0) {
            return light / count;
        } else {
            return 0;
        }
    }

    private class It implements Iterator<BlockState> {
        private int x = min.x, y = min.y, z = min.z;

        @Override
        public boolean hasNext() {
            return z < max.z;
        }

        @Override
        public BlockState next() {
            BlockState state = parent.getState(new Vec3i(x, y, z));

            if(++x >= max.x) {
                if(++y >= max.y) {
                    ++z;
                    y = min.y;
                }
                x = min.x;
            }
            return state;
        }
    }
}
