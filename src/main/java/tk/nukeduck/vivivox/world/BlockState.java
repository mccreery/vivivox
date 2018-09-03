package tk.nukeduck.vivivox.world;

import tk.nukeduck.vivivox.block.Block;
import tk.nukeduck.vivivox.helper.GeneralTools;
import tk.nukeduck.vivivox.helper.Vec3i;

public abstract class BlockState {
    public abstract Vec3i getPosition();
    public abstract Block getBlock();
    public abstract byte getBiome();
    public abstract byte getLightLevel();

    public abstract void setBlock(Block block);

    public abstract BlockState move(Vec3i offset);

    public final BlockState up() {return move(new Vec3i(0, 1, 0));}
    public final BlockState down() {return move(new Vec3i(0, -1, 0));}
    public final BlockState xpos() {return move(new Vec3i(1, 0, 0));}
    public final BlockState xneg() {return move(new Vec3i(-1, 0, 0));}
    public final BlockState zpos() {return move(new Vec3i(0, 0, 1));}
    public final BlockState zneg() {return move(new Vec3i(0, 0, -1));}

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

    public static BlockState AIR = new BlockState() {
        @Override
        public Vec3i getPosition() {
            return Vec3i.ZERO;
        }

        @Override
        public Block getBlock() {
            return Block.air;
        }

        @Override
        public byte getBiome() {
            return 0;
        }

        @Override
        public byte getLightLevel() {
            return 0;
        }

        @Override
        public void setBlock(Block block) {}

        @Override
        public BlockState move(Vec3i offset) {
            return this;
        }
    };
}
