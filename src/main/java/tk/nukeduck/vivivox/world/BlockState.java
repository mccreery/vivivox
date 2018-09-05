package tk.nukeduck.vivivox.world;

import tk.nukeduck.vivivox.block.Block;
import tk.nukeduck.vivivox.block.BlockView;
import tk.nukeduck.vivivox.block.BlockWindow;
import tk.nukeduck.vivivox.helper.GeneralTools;
import tk.nukeduck.vivivox.helper.Vec3i;

public abstract class BlockState {
    public abstract Vec3i getPosition();
    public abstract Block getBlock();
    public abstract byte getBiome();
    public abstract byte getLightLevel();

    public void setBlock(Block block) {}
    public void setLightLevel(byte light) {}
    public void setBiome(byte id) {}

    public abstract BlockState move(Vec3i offset);

    private static final Vec3i UP = new Vec3i(0, 1, 0);
    private static final Vec3i DOWN = new Vec3i(0, -1, 0);
    private static final Vec3i XPOS = new Vec3i(1, 0, 0);
    private static final Vec3i XNEG = new Vec3i(-1, 0, 0);
    private static final Vec3i ZPOS = new Vec3i(0, 0, 1);
    private static final Vec3i ZNEG = new Vec3i(0, 0, -1);

    public final BlockState up() {return move(UP);}
    public final BlockState down() {return move(DOWN);}
    public final BlockState xpos() {return move(XPOS);}
    public final BlockState xneg() {return move(XNEG);}
    public final BlockState zpos() {return move(ZPOS);}
    public final BlockState zneg() {return move(ZNEG);}

    public final int getAdjacentBlocks() {
        return GeneralTools.count(
            up().getBlock().isOpaque(),
            down().getBlock().isOpaque(),
            xpos().getBlock().isOpaque(),
            xneg().getBlock().isOpaque(),
            zpos().getBlock().isOpaque(),
            zneg().getBlock().isOpaque()
        );
    }

    public static class OOB extends BlockState {
        private final BlockView world;
        private final Vec3i position;
        private final Block block;

        public OOB(BlockView world, Vec3i position, Block block) {
            this.world = world;
            this.position = position;
            this.block = block;
        }

        @Override
        public Vec3i getPosition() {
            return position;
        }

        @Override
        public Block getBlock() {
            return block;
        }

        @Override
        public byte getLightLevel() {
            return (byte)new BlockWindow(world, position.sub(new Vec3i(1, 1, 1)), position.add(new Vec3i(1, 1, 1))).getAverageLight();
        }

        @Override
        public byte getBiome() {
            return 0;
        }

        @Override
        public BlockState move(Vec3i offset) {
            return world.getState(position.add(offset));
        }
    }
}
