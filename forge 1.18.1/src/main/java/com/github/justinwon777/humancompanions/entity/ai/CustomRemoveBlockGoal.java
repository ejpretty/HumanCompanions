package com.github.justinwon777.humancompanions.entity.ai;

import com.github.justinwon777.humancompanions.container.CompanionContainer;
import com.github.justinwon777.humancompanions.entity.AbstractHumanCompanionEntity;
import com.github.justinwon777.humancompanions.entity.CompanionData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.crafting.conditions.FalseCondition;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraft.world.entity.player.Player;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

import static java.beans.XMLDecoder.createHandler;

public class CustomRemoveBlockGoal extends MoveToBlockGoal {
    protected final Block blockToRemove;
    private BlockPos blockActualPos;
    public Player player;
//    protected final PathfinderMob mob;
//    private final ItemStackHandler itemHandler = createHandler();
//
//
//    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

//    protected final TamableAnimal mob;
    private boolean reachedTarget = false;
    private final Mob removerMob;
    private int maxStayTicks;

    private int ticksSinceReachedGoal;
    private static final int WAIT_AFTER_BLOCK_FOUND = 20;
    public int blocksDestroyed;

    public SimpleContainer inventory;


    //    public RemoveBlockGoal(Block p_25840_, AbstractPlayerCompanionEntity companion, double p_25842_, int p_25843_) {
//        super(companion, p_25842_, 24, p_25843_);
//        this.blockToRemove = p_25840_;
//        this.removerMob = companion;
//    }
//        ItemStack stack = this.itemHandler.getStackInSlot(i);
    public CustomRemoveBlockGoal(Block pBlockToRemove, PathfinderMob p_34344_, double pSpeedModifier, int pSearchRange, int blocksDestroyed, SimpleContainer mobInventory, Player player) {
        super(p_34344_, pSpeedModifier, 10, pSearchRange);
        this.blockToRemove = pBlockToRemove;
        this.removerMob = p_34344_;
        this.blocksDestroyed = blocksDestroyed;
        this.inventory = mobInventory;
//        this.player = player;
    }

//    public boolean iterateBlocks(Player player) {
//        public int woodUnfinished =
//        ItemStack woodFinished = new ItemStack(Blocks.ACACIA_WOOD, (<= 5);
//        ItemStack stoneFinished = new ItemStack(Blocks.STONE, 15);
////        int i = i;
//        if (player.getInventory().contains(woodFinished)) {
//            this.blockToRemove == pBlocks.STONE;
//            if (player.getInventory().contains(stoneFinished)) {
//                blockToRemove == Blocks.ACACIA_WOOD;
//            }
//        }
//    }

//    public boolean canUse() {
//        if (CompanionData.numberOfBlockDestroyed <= 10) {
//            return true;
//            }
////        } if (!net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.removerMob.level, this.blockPos, this.removerMob)) {
////            return false;
////        } else if (this.nextStartTick > 0) {
////            --this.nextStartTick;
////            return false;
////        } else if (this.getMoveToTarget()) {
////            this.nextStartTick = reducedTickDelay(100);
////            return true;
////        } else {
////            this.nextStartTick = this.nextStartTick(this.mob);
////            return false;
////        }
//        return true;
//    }


    @Override
    public boolean requiresUpdateEveryTick() {
        return super.requiresUpdateEveryTick();
    }

    private boolean tryFindBlock() {
        return this.blockPos != null && this.isValidTarget(this.mob.level, this.blockPos) ? true : this.findNearestBlock();
    }
    public BlockPos getMoveToTarget() {

        blockActualPos = this.blockPos;


        Vec3 southPos = new Vec3(blockActualPos.south().getX(), blockActualPos.south().getY(), blockActualPos.south().getZ());
        Vec3 eastPos = new Vec3(blockActualPos.east().getX(), blockActualPos.east().getY(), blockActualPos.east().getZ());
        Vec3 northPos = new Vec3(blockActualPos.north().getX(), blockActualPos.north().getY(), blockActualPos.north().getZ());
        Vec3 westPos = new Vec3(blockActualPos.west().getX(), blockActualPos.west().getY(), blockActualPos.west().getZ());

        double disSouth = this.mob.position().distanceTo(southPos);
        double disEast = this.mob.position().distanceTo(eastPos);
        double disNorth = this.mob.position().distanceTo(northPos);
        double disWest = this.mob.position().distanceTo(westPos);

        if (disSouth < disEast) {
            if (disSouth < disNorth) {
                if (disSouth < disWest)
                    return blockActualPos.south();
                else
                    return blockActualPos.west();
            } else if (disNorth < disWest)
                return blockActualPos.north();
            else
                return blockActualPos.west();
        }
        else if (disEast < disNorth) {
            if (disEast < disWest)
                return blockActualPos.east();
            else
                return blockActualPos.west();
        }
        else if (disNorth < disWest)
            return blockActualPos.north();
        else
            return blockActualPos.west();

    }

    public double acceptedDistance() {
        return 1.5D;
    }
    protected boolean isReachedTarget() {
        return this.reachedTarget;
    }
//    protected void moveMobToBlock() {
//        this.mob.getNavigation().moveTo((double)((float)this.blockPos.getX()) + 0.5D, (double)(this.blockPos.getY()/*+1*/), (double)((float)this.blockPos.getZ()) + 0.5D, this.speedModifier);
//    }
    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        super.stop();
//        ItemStack woodFinished = new ItemStack(Blocks.ACACIA_LOG, (10));
//        if (player.getInventory().contains(woodFinished)) {
//            stop();}
        this.removerMob.fallDistance = 1.0F;
    }
    public boolean canContinueToUse() {
        if (CompanionData.numberOfBlockDestroyed <= 10) {
            return true;
        }
        return this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 100 && this.isValidTarget(this.mob.level, this.blockPos);


    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
//        this.moveMobToBlock();

        this.tryTicks = 0;
        this.maxStayTicks = this.mob.getRandom().nextInt(this.mob.getRandom().nextInt(100) + 100) + 100;
        this.ticksSinceReachedGoal = 0;
    }

    public void playDestroyProgressSound(LevelAccessor pLevel, BlockPos pPos) {
    }

    public void playBreakSound(Level pLevel, BlockPos pPos) {
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
//        super.tick();
        BlockPos blockPos = this.getMoveToTarget();
        if (!blockPos.closerThan(this.mob.position(), this.acceptedDistance())) {
            this.reachedTarget = false;
            ++this.tryTicks;
            if (this.shouldRecalculatePath()) {
                System.out.print("is still going");
                this.mob.getNavigation().moveTo((double)((float)blockPos.getX()), (double)blockPos.getY(), (double)((float)blockPos.getZ()), this.speedModifier);
                System.out.print("renavigate");
            }
        } else {
            System.out.print("reached target");

            this.reachedTarget = true;
            --this.tryTicks;
        }



        Level level = this.removerMob.level;
//        BlockPos blockpos1 = this.removerMob.blockPosition();
//        blockpos = this.getPosWithBlock(blockpos1, level);
        Random random = this.removerMob.getRandom();

        if (this.isReachedTarget() && blockPos != null) {
            if (this.ticksSinceReachedGoal > 0) {
//                Vec3 vec3 = this.removerMob.getDeltaMovement();
//                this.removerMob.setDeltaMovement(vec3.x, 0.3D, vec3.z);
                if (!level.isClientSide) {
                    double d0 = 0.08D;
                    ((ServerLevel)level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.EGG)), (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 0.7D, (double)blockPos.getZ() + 0.5D, 3, ((double)random.nextFloat() - 0.5D) * 0.08D, ((double)random.nextFloat() - 0.5D) * 0.08D, ((double)random.nextFloat() - 0.5D) * 0.08D, (double)0.15F);
                }
            }

            if (this.ticksSinceReachedGoal % 2 == 0) {
//                Vec3 vec31 = this.removerMob.getDeltaMovement();
//                this.removerMob.setDeltaMovement(vec31.x, -0.3D, vec31.z);
                if (this.ticksSinceReachedGoal % 6 == 0) {
                    this.playDestroyProgressSound(level, this.blockPos);
                }
            }

            //this is how long it takes to destroy the block once it has been reached
            if (this.ticksSinceReachedGoal > 50) {
                level.removeBlock(blockActualPos, false);
                CompanionData.numberOfBlockDestroyed++;
                inventory.addItem(Items.ACACIA_LOG.getDefaultInstance());
                if (!level.getBlockState(blockActualPos.above()).isAir()) {
                    level.removeBlock(blockActualPos.above(), false);
                    CompanionData.numberOfBlockDestroyed++;
                    inventory.addItem(Items.ACACIA_LOG.getDefaultInstance());
                }
//                level.removeBlock(blockActualPos.above(), false);
                System.out.print(CompanionData.numberOfBlockDestroyed);
                System.out.println(">>>>>>>>>>>>>");
                System.out.println(inventory.toString());
                System.out.println(">>>>>>>>>>>>>");
//                if(inventory != null)
//                {
//                    inventory.addItem(Items.BIRCH_LOG.getDefaultInstance());
//                    int numberofAcaciaLogsWithinCompanion = 0;
////                    for(int i = 0; i < player.getInventory().items.size(); i++)
////                    {
////                        if(player.getInventory().getItem(i).is(Items.ACACIA_LOG))
////                        {
////
////                        }
////                    }
//                    System.out.println(">>>>>>>>>>>>>");
//                    System.out.println(inventory.toString());
//                    System.out.println(">>>>>>>>>>>>>");
//                }
//                else
//                {
//                    System.out.println(">>>>>>>>>>>>> The inventory is NULL");
//                }
//                if (this.inventory.isEmpty()) {
//                 this.inventory.inserItem(1,1)); }
//                for (int i = 0; i < 4; i++) {
//                    ItemStack itemstackWood = ;
//                    if(!itemstackWood.
////                            .isEmpty()) {
//
//                        this.inventory.setItem(i, itemstack);
//                    }
//                }
//                }
                }
                if (!level.isClientSide) {
                    for(int i = 0; i < 20; ++i) {
                        double d3 = random.nextGaussian() * 0.02D;
                        double d1 = random.nextGaussian() * 0.02D;
                        double d2 = random.nextGaussian() * 0.02D;
                        ((ServerLevel)level).sendParticles(ParticleTypes.POOF, (double)blockPos.getX() + 0.5D, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5D, 1, d3, d1, d2, (double)0.15F);
                    }
                    this.playBreakSound(level, blockPos);
                }
            }
            buildHouse();
            ++this.ticksSinceReachedGoal;
        }

    @Nullable
    private BlockPos getPosWithBlock(BlockPos pPos, BlockGetter pLevel) {
        if (pLevel.getBlockState(pPos).is(this.blockToRemove)) {
            return pPos;
        } else {
            BlockPos[] ablockpos = new BlockPos[]{pPos.below(), pPos.west(), pPos.east(), pPos.north(), pPos.south(), pPos.below().below()};

            for(BlockPos blockpos : ablockpos) {
                if (pLevel.getBlockState(blockpos).is(this.blockToRemove)) {
                    return blockpos;
                }
            }

            return null;
        }
    }
    public void buildHouse() {
        Level level = this.mob.level;
        BlockPos pPos = new BlockPos(-907, 78, -522);
        BlockState blockstate = Blocks.ACACIA_PLANKS.defaultBlockState();
        if (CompanionData.numberOfBlockDestroyed > 10) {
//            this.ticksSinceReachedGoal--;
            level.setBlock(pPos, blockstate, 3);
//            this.ticksSinceReachedGoal--;
            level.gameEvent(this.mob, GameEvent.BLOCK_PLACE, pPos);
        }
    }

    /**
     * Return true to set given position as destination
     */
    protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
        ChunkAccess chunkaccess = pLevel.getChunk(SectionPos.blockToSectionCoord(pPos.getX()), SectionPos.blockToSectionCoord(pPos.getZ()), ChunkStatus.FULL, false);
        if (chunkaccess == null) {
            return false;
        } else {
            return chunkaccess.getBlockState(pPos).is(this.blockToRemove)
                    && pPos.getY() > 69
                    && chunkaccess.getBlockState(pPos.east()).isAir()
                    && chunkaccess.getBlockState(pPos.west()).isAir()
                    && chunkaccess.getBlockState(pPos.south()).isAir()
                    && chunkaccess.getBlockState(pPos.north()).isAir();
//            && chunkaccess.getBlockState(pPos.above()).isAir() && chunkaccess.getBlockState(pPos.above(2)).isAir();
        }
    }
}
