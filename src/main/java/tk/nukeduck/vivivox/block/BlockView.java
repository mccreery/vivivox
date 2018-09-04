package tk.nukeduck.vivivox.block;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import tk.nukeduck.vivivox.helper.Vec3i;
import tk.nukeduck.vivivox.world.BlockState;

public abstract class BlockView {
    protected final Vec3i min, max;

    public BlockView(Vec3i min, Vec3i max) {
        this.min = min;
        this.max = max;
    }

    public abstract BlockState getState(Vec3i position);

    public BlockView offset(Vec3i offset) {
        return new RelativeView(this, offset);
    }

    private static final int stride = 8;

    private int vertexCount;
    private int buffer;

	public final void updateVBO(boolean smoothLighting) {
		vertexCount = 0;
        BlockWindow window = new BlockWindow(this, min, max);

        for(BlockState state : window) {
            if(state.getBlock().isOpaque()) {
                vertexCount += (6 - state.getAdjacentBlocks()) * 6;
            }
        }

        FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexCount * stride);

        for(BlockState state : window) {
            if(state.getBlock().isOpaque()) {
                state.getBlock().renderToVBO(this, state, smoothLighting, vertexData);
            }
        }

        vertexData.flip();
        buffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, buffer);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
    }

    public final void draw() {
        glBindBuffer(GL_ARRAY_BUFFER, buffer);

        glVertexPointer(3, GL_FLOAT, 32, 0);
        glTexCoordPointer(2, GL_FLOAT, 32, 12);
        glColorPointer(3, GL_FLOAT, 32, 20);

        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
    }

    private static class RelativeView extends BlockView {
        final BlockView parent;
        final Vec3i offset;

        RelativeView(BlockView parent, Vec3i offset) {
            super(parent.min.add(offset), parent.max.add(offset));

            this.parent = parent;
            this.offset = offset;
        }

        @Override
        public BlockState getState(Vec3i position) {
            return parent.getState(position.add(offset));
        }

        @Override
        public BlockView offset(Vec3i offset) {
            return new RelativeView(parent, this.offset.add(offset));
        }
    }
}
