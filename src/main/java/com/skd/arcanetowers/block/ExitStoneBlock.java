package com.skd.arcanetowers.block;

import com.skd.arcanetowers.ArcaneTowers;
import com.skd.expeditioncore.protection.StructureProtection;
import java.util.List;
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

    private static final VoxelShape LOWER_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape UPPER_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 14.0, 12.0);

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
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? LOWER_SHAPE : UPPER_SHAPE;
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
