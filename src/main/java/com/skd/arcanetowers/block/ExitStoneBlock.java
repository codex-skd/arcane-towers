package com.skd.arcanetowers.block;

import com.skd.arcanetowers.ArcaneTowers;
import com.skd.expeditioncore.protection.StructureProtection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The Exit Stone: a two-block-tall standing stone found in the final room of every tower's dungeon.
 *
 * <p>Using it from either half locates the tower structure it belongs to, unseals that structure
 * (expedition_core {@link StructureProtection}) and teleports the player to the surface, two blocks
 * outside the tower's north (-Z) door. Using it outside any tower is inert.</p>
 */
public class ExitStoneBlock extends Block {

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final double[][] LOWER_BOXES_NORTH = {
            {7.25, 0.0, 6.5, 8.75, 3.0, 9.5},
            {6.6, 3.0, 5.6, 9.9, 8.0, 10.4},
            {6.41, 6.97, 5.2, 13.31, 13.65, 10.8},
            {6.2, 12.5, 5.2, 12.8, 16.0, 10.8}
    };

    private static final double[][] UPPER_BOXES_NORTH = {
            {6.2, 0.0, 5.2, 12.8, 3.5, 10.8},
            {7.64, 1.81, 5.6, 15.66, 9.73, 10.4},
            {10.0, 8.5, 6.0, 14.0, 13.5, 10.0},
            {11.6, 12.48, 6.8, 15.25, 16.0, 9.6}
    };

    private static final Map<Direction, VoxelShape> LOWER_SHAPES = buildShapes(LOWER_BOXES_NORTH);
    private static final Map<Direction, VoxelShape> UPPER_SHAPES = buildShapes(UPPER_BOXES_NORTH);

    /** The four tower structure keys, in lookup order. */
    private static final List<Tower> TOWERS = List.of(
            new Tower("stone_tower", "tauren"),
            new Tower("frost_tower", "goliath"),
            new Tower("sandstone_tower", "centaur"),
            new Tower("umbral_tower", "giant")
    );

    public ExitStoneBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HALF, DoubleBlockHalf.LOWER)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, FACING);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Map<Direction, VoxelShape> shapes =
                state.getValue(HALF) == DoubleBlockHalf.LOWER ? LOWER_SHAPES : UPPER_SHAPES;
        return shapes.get(state.getValue(FACING));
    }

    private static Map<Direction, VoxelShape> buildShapes(double[][] boxes) {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        shapes.put(Direction.NORTH, makeShape(boxes, 0));
        shapes.put(Direction.EAST, makeShape(boxes, 1));
        shapes.put(Direction.SOUTH, makeShape(boxes, 2));
        shapes.put(Direction.WEST, makeShape(boxes, 3));
        return shapes;
    }

    private static VoxelShape makeShape(double[][] boxes, int clockwiseSteps) {
        VoxelShape shape = Shapes.empty();
        for (double[] box : boxes) {
            double x1 = box[0];
            double y1 = box[1];
            double z1 = box[2];
            double x2 = box[3];
            double y2 = box[4];
            double z2 = box[5];
            for (int step = 0; step < clockwiseSteps; step++) {
                double nx1 = 16.0 - z1;
                double nz1 = x1;
                double nx2 = 16.0 - z2;
                double nz2 = x2;
                x1 = Math.min(nx1, nx2);
                x2 = Math.max(nx1, nx2);
                z1 = Math.min(nz1, nz2);
                z2 = Math.max(nz1, nz2);
            }
            shape = Shapes.or(shape, Block.box(x1, y1, z1, x2, y2, z2));
        }
        return shape;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        if (pos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(pos.above()).canBeReplaced(context)) {
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(HALF, DoubleBlockHalf.LOWER);
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        BlockPos above = pos.above();
        level.setBlock(above, copyWaterloggedFrom(level, above,
                this.defaultBlockState()
                        .setValue(FACING, state.getValue(FACING))
                        .setValue(HALF, DoubleBlockHalf.UPPER)), 3);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState,
                                     LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf half = state.getValue(HALF);
        if (facing.getAxis() != Direction.Axis.Y || half == DoubleBlockHalf.LOWER != (facing == Direction.UP)
                || (facingState.is(this) && facingState.getValue(HALF) != half)) {
            return half == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive(level, currentPos)
                    ? Blocks.AIR.defaultBlockState()
                    : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(state, level, pos);
        }
        BlockState below = level.getBlockState(pos.below());
        if (state.getBlock() != this) {
            return super.canSurvive(state, level, pos);
        }
        return below.is(this) && below.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (player.isCreative() || !player.hasCorrectToolForDrops(state, level, pos)) {
                preventDropFromBottomPart(level, pos, state, player);
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    private static void preventDropFromBottomPart(Level level, BlockPos pos, BlockState state, Player player) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockPos below = pos.below();
            BlockState belowState = level.getBlockState(below);
            if (belowState.is(state.getBlock()) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                BlockState replacement = belowState.getFluidState().is(Fluids.WATER)
                        ? Blocks.WATER.defaultBlockState()
                        : Blocks.AIR.defaultBlockState();
                level.setBlock(below, replacement, 35);
                level.levelEvent(player, 2001, below, Block.getId(belowState));
            }
        }
    }

    private static BlockState copyWaterloggedFrom(LevelReader level, BlockPos pos, BlockState state) {
        return state.hasProperty(BlockStateProperties.WATERLOGGED)
                ? state.setValue(BlockStateProperties.WATERLOGGED, level.isWaterAt(pos))
                : state;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player,
                                               BlockHitResult hitResult) {
        if (!(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        Located located = locateTower(serverLevel, pos);
        if (located == null) {
            serverPlayer.displayClientMessage(Component.translatable("arcane_towers.exit_stone.inert"), true);
            return InteractionResult.sidedSuccess(false);
        }

        boolean newlyUnlocked = StructureProtection.unlockAt(serverLevel, pos, located.key());
        if (newlyUnlocked) {
            Component message = Component.translatable("arcane_towers.tower.unsealed");
            for (ServerPlayer nearby : serverLevel.getPlayers(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) <= 96 * 96)) {
                nearby.displayClientMessage(message, true);
            }
        }

        teleportToEntrance(serverLevel, serverPlayer, located);
        serverPlayer.displayClientMessage(Component.translatable("arcane_towers.exit_stone.used"), true);
        return InteractionResult.sidedSuccess(false);
    }

    /** Finds which tower structure contains this stone; returns {@code null} if none. */
    @Nullable
    private static Located locateTower(ServerLevel level, BlockPos pos) {
        var registry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);
        for (Tower tower : TOWERS) {
            Structure structure = registry.get(tower.key());
            if (structure == null) {
                continue;
            }
            StructureStart start = level.structureManager().getStructureAt(pos, structure);
            if (start.isValid()) {
                return new Located(tower, tower.key(), start);
            }
        }
        return null;
    }

    private static void teleportToEntrance(ServerLevel level, ServerPlayer player, Located located) {
        BlockPos origin = player.blockPosition();
        spawnParticles(level, origin);

        Optional<BoundingBox> towerBox = findTowerPieceBox(located.start(), located.tower().id());
        if (towerBox.isPresent()) {
            BoundingBox box = towerBox.get();
            int x = (box.minX() + box.maxX()) / 2;
            int z = box.minZ() - 2;
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos destination = new BlockPos(x, y, z);
            player.teleportTo(x + 0.5, y, z + 0.5);
            spawnParticles(level, destination);
        }

        level.playSound(null, origin, SoundEvents.ENDERMAN_TELEPORT, SoundSource.BLOCKS, 1.0f, 1.0f);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
    }

    private static void spawnParticles(ServerLevel level, BlockPos pos) {
        level.sendParticles(ParticleTypes.PORTAL,
                pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 40, 0.4, 0.6, 0.4, 0.2);
    }

    /**
     * Finds the bounding box of the piece whose template is {@code arcane_towers:<id>/tower}.
     *
     * <p>{@link SinglePoolElement}'s template has no getter in 1.21.1, so the element is matched by its
     * string form, which reads {@code "Single[Left[arcane_towers:<id>/tower]]"}.</p>
     */
    private static Optional<BoundingBox> findTowerPieceBox(StructureStart start, String id) {
        String template = ArcaneTowers.MOD_ID + ":" + id + "/tower";
        for (StructurePiece piece : start.getPieces()) {
            if (!(piece instanceof PoolElementStructurePiece poolPiece)) {
                continue;
            }
            StructurePoolElement element = poolPiece.getElement();
            if (!(element instanceof SinglePoolElement)) {
                continue;
            }
            if (element.toString().contains(template)) {
                return Optional.of(poolPiece.getBoundingBox());
            }
        }
        return Optional.empty();
    }

    /** The tower a stone belongs to, its structure key and the resolved structure start. */
    private record Located(Tower tower, ResourceKey<Structure> key, StructureStart start) {}

    /** Data of one of the four towers: id (for pool lookups and lang) and its guardian entity id. */
    public record Tower(String id, String guardian) {

        public ResourceKey<Structure> key() {
            return ResourceKey.create(Registries.STRUCTURE,
                    ResourceLocation.fromNamespaceAndPath(ArcaneTowers.MOD_ID, this.id));
        }
    }

    /** The four tower records, in lookup order. */
    public static List<Tower> towers() {
        return TOWERS;
    }
}
