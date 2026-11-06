package io.github.apace100.origins.content;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

public class TemporaryCobwebBlock extends WebBlock {

	public TemporaryCobwebBlock(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	protected void tick(@NonNull BlockState state, ServerLevel world, @NonNull BlockPos pos, @NonNull RandomSource random) {
		if(!world.isClientSide()) {
			world.setBlock(pos, Blocks.AIR.defaultBlockState(),UPDATE_ALL);
		}
	}

	@Override
	public VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter world, @NonNull BlockPos pos, @NonNull CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public VoxelShape getCollisionShape(@NonNull BlockState state, @NonNull BlockGetter world, @NonNull BlockPos pos, @NonNull CollisionContext context) {
		return Shapes.empty();
	}

	@Override
	public void onPlace(@NonNull BlockState state, Level worldIn, @NonNull BlockPos pos, @NonNull BlockState oldState, boolean isMoving) {
		worldIn.scheduleTick(pos, this, 60);
	}

}
