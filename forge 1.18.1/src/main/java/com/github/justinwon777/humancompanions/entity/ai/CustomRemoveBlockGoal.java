package com.github.justinwon777.humancompanions.entity.ai;

import com.github.justinwon777.humancompanions.HumanCompanions;
import com.github.justinwon777.humancompanions.entity.AbstractHumanCompanionEntity;
import com.github.justinwon777.humancompanions.entity.CompanionData;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
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
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Random;

import static com.github.justinwon777.humancompanions.entity.AbstractHumanCompanionEntity.tameTrigger;

public class CustomRemoveBlockGoal extends MoveToBlockGoal {
    protected final Block blockToRemove;
    private BlockPos blockActualPos;
    public Player player;
    private static Player _player;
    private boolean reachedTarget = false;
    private final Mob removerMob;
    private int maxStayTicks;
    public int talkTicks;
    public int blockCounter = 0;

    public int bobTextCounter = 0;

    public int tutorialTextCounter = 0;

    public int instructionsTextCounter = 0;
    public int wallCheckCounter = 0;
    public int roofCheckCounter = 0;

    public int compWallsCheckCounter = 0;
    private int ticksSinceReachedGoal;
    private static final int WAIT_AFTER_BLOCK_FOUND = 20;
    public int blocksDestroyed;
    public SimpleContainer inventory;
    public Level level;

    public CustomRemoveBlockGoal(Block pBlockToRemove, PathfinderMob p_34344_, double pSpeedModifier, int pSearchRange, SimpleContainer mobInventory, Player player, Level level) {
        super(p_34344_, pSpeedModifier, 24, pSearchRange);
        this.blockToRemove = pBlockToRemove;
        this.removerMob = p_34344_;
        this.inventory = mobInventory;
        this.player = player;
        this.level = level;
    }

//    private void getWorldName (LevelStorageSource.LevelStorageAccess levelAccess) {
//        this.levelAccess = levelAccess;
//        System.out.println("level id: " + this.levelAccess.getLevelId());
////        logger.severe(valueOf(this.levelAccess.getLevelId()) + " participant/world ID");
//    }

//    public boolean canUse() {
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

    //player front wall
    BlockPos plFrontBlockBottom1 = new BlockPos(-295, 67, -279);
    BlockPos plFrontBlockTop1 = new BlockPos(-295, 68, -279);
    BlockPos plFrontBlockBottom2 = new BlockPos(-296, 67, -279);
    BlockPos plFrontBlockTop2 = new BlockPos(-296, 68, -279);
    BlockPos plFrontBlockBottom3 = new BlockPos(-297, 67, -279);
    BlockPos plFrontBlockTop3 = new BlockPos(-297, 68, -279);
    BlockPos plFrontBlockBottom4 = new BlockPos(-298, 67, -279);
    BlockPos plFrontBlockTop4 = new BlockPos(-298, 68, -279);
    BlockPos plFrontBlockBottom5 = new BlockPos(-299, 67, -279);
    BlockPos plFrontBlockTop5 = new BlockPos(-299, 68, -279);

    //player side wall
    BlockPos plSideBlockBottom1 = new BlockPos(-299, 67, -280);
    BlockPos plSideBlockTop1 = new BlockPos(-299, 68, -280);
    BlockPos plSideBlockBottom2 = new BlockPos(-299, 67, -281);
    BlockPos plSideBlockTop2 = new BlockPos(-299, 68, -281);
    BlockPos plSideBlockBottom3 = new BlockPos(-299, 67, -282);
    BlockPos plSideBlockTop3 = new BlockPos(-299, 68, -282);
    BlockPos plSideBlockBottom4 = new BlockPos(-299, 67, -283);
    BlockPos plSideBlockTop4 = new BlockPos(-299, 68, -283);
    BlockPos plSideBlockBottom5 = new BlockPos(-299, 67, -284);
    BlockPos plSideBlockTop5 = new BlockPos(-299, 68, -284);

    //player back wall
    BlockPos plBackBlockBottom1 = new BlockPos(-299, 67, -285);
    BlockPos plBackBlockTop1 = new BlockPos(-299, 68, -285);
    BlockPos plBackBlockBottom2 = new BlockPos(-298, 67, -285);
    BlockPos plBackBlockTop2 = new BlockPos(-298, 68, -285);
    BlockPos plBackBlockBottom3 = new BlockPos(-297, 67, -285);
    BlockPos plBackBlockTop3 = new BlockPos(-297, 68, -285);
    BlockPos plBackBlockBottom4 = new BlockPos(-296, 67, -285);
    BlockPos plBackBlockTop4 = new BlockPos(-296, 68, -285);
    BlockPos plBackBlockBottom5 = new BlockPos(-295, 67, -285);
    BlockPos plBackBlockTop5 = new BlockPos(-295, 68, -285);
    BlockPos plBackBlockBottom6 = new BlockPos(-294, 67, -285);
    BlockPos plBackBlockTop6 = new BlockPos(-294, 68, -285);

    //player roof blockpos' for the border
    BlockPos plRoofBlock1 = new BlockPos(-299, 69, -285);
    BlockPos plRoofBlock2 = new BlockPos(-299, 69, -284);
    BlockPos plRoofBlock3 = new BlockPos(-299, 69, -283);
    BlockPos plRoofBlock4 = new BlockPos(-299, 69, -282);
    BlockPos plRoofBlock5 = new BlockPos(-299, 69, -281);
    BlockPos plRoofBlock6 = new BlockPos(-299, 69, -280);
    BlockPos plRoofBlock7 = new BlockPos(-299, 69, -279);
    BlockPos plRoofBlock8 = new BlockPos(-298, 69, -279);
    BlockPos plRoofBlock9 = new BlockPos(-297, 69, -279);
    BlockPos plRoofBlock10 = new BlockPos(-296, 69, -279);
    BlockPos plRoofBlock11 = new BlockPos(-295, 69, -279);
    BlockPos plRoofBlock12 = new BlockPos(-294, 69, -279);
    BlockPos plRoofBlock13 = new BlockPos(-293, 69, -279);
    BlockPos plRoofBlock14 = new BlockPos(-292, 69, -279);
    BlockPos plRoofBlock15 = new BlockPos(-291, 69, -279);
    BlockPos plRoofBlock16 = new BlockPos(-290, 69, -279);
    BlockPos plRoofBlock17 = new BlockPos(-289, 69, -279);
    BlockPos plRoofBlock18 = new BlockPos(-288, 69, -279);
    BlockPos plRoofBlock19 = new BlockPos(-288, 69, -280);
    BlockPos plRoofBlock20 = new BlockPos(-288, 69, -281);
    BlockPos plRoofBlock21 = new BlockPos(-288, 69, -282);
    BlockPos plRoofBlock22 = new BlockPos(-288, 69, -283);
    BlockPos plRoofBlock23 = new BlockPos(-288, 69, -284);
    BlockPos plRoofBlock24 = new BlockPos(-288, 69, -285);
    BlockPos plRoofBlock25 = new BlockPos(-289, 69, -285);
    BlockPos plRoofBlock26 = new BlockPos(-290, 69, -285);
    BlockPos plRoofBlock27 = new BlockPos(-291, 69, -285);
    BlockPos plRoofBlock28 = new BlockPos(-292, 69, -285);
    BlockPos plRoofBlock29 = new BlockPos(-293, 69, -285);
    BlockPos plRoofBlock30 = new BlockPos(-294, 69, -285);
    BlockPos plRoofBlock31 = new BlockPos(-295, 69, -285);
    BlockPos plRoofBlock32 = new BlockPos(-296, 69, -285);
    BlockPos plRoofBlock33 = new BlockPos(-297, 69, -285);
    BlockPos plRoofBlock34 = new BlockPos(-298, 69, -285);

    // player roof blockpos for inside
    BlockPos plRoofBlock35 = new BlockPos(-298, 69, -280);
    BlockPos plRoofBlock36 = new BlockPos(-298, 69, -281);
    BlockPos plRoofBlock37 = new BlockPos(-298, 69, -282);
    BlockPos plRoofBlock38 = new BlockPos(-298, 69, -283);
    BlockPos plRoofBlock39 = new BlockPos(-298, 69, -284);
    BlockPos plRoofBlock40 = new BlockPos(-297, 69, -280);
    BlockPos plRoofBlock41 = new BlockPos(-297, 69, -281);
    BlockPos plRoofBlock42 = new BlockPos(-297, 69, -282);
    BlockPos plRoofBlock43 = new BlockPos(-297, 69, -283);
    BlockPos plRoofBlock44 = new BlockPos(-297, 69, -284);
    BlockPos plRoofBlock45 = new BlockPos(-296, 69, -280);
    BlockPos plRoofBlock46 = new BlockPos(-296, 69, -281);
    BlockPos plRoofBlock47 = new BlockPos(-296, 69, -282);
    BlockPos plRoofBlock48 = new BlockPos(-296, 69, -283);
    BlockPos plRoofBlock49 = new BlockPos(-296, 69, -284);
    BlockPos plRoofBlock50 = new BlockPos(-295, 69, -280);
    BlockPos plRoofBlock51 = new BlockPos(-295, 69, -281);
    BlockPos plRoofBlock52 = new BlockPos(-295, 69, -282);
    BlockPos plRoofBlock53 = new BlockPos(-295, 69, -283);
    BlockPos plRoofBlock54 = new BlockPos(-295, 69, -284);
    BlockPos plRoofBlock55 = new BlockPos(-294, 69, -280);
    BlockPos plRoofBlock56 = new BlockPos(-294, 69, -281);
    BlockPos plRoofBlock57 = new BlockPos(-294, 69, -282);
    BlockPos plRoofBlock58 = new BlockPos(-294, 69, -283);
    BlockPos plRoofBlock59 = new BlockPos(-294, 69, -284);
    BlockPos plRoofBlock60 = new BlockPos(-293, 69, -280);
    BlockPos plRoofBlock61 = new BlockPos(-293, 69, -281);
    BlockPos plRoofBlock62 = new BlockPos(-293, 69, -282);
    BlockPos plRoofBlock63 = new BlockPos(-293, 69, -283);
    BlockPos plRoofBlock64 = new BlockPos(-293, 69, -284);
    BlockPos plRoofBlock65 = new BlockPos(-292, 69, -280);
    BlockPos plRoofBlock66 = new BlockPos(-292, 69, -281);
    BlockPos plRoofBlock67 = new BlockPos(-292, 69, -282);
    BlockPos plRoofBlock68 = new BlockPos(-292, 69, -283);
    BlockPos plRoofBlock69 = new BlockPos(-292, 69, -284);
    BlockPos plRoofBlock70 = new BlockPos(-291, 69, -280);
    BlockPos plRoofBlock71 = new BlockPos(-291, 69, -281);
    BlockPos plRoofBlock72 = new BlockPos(-291, 69, -282);
    BlockPos plRoofBlock73 = new BlockPos(-291, 69, -283);
    BlockPos plRoofBlock74 = new BlockPos(-291, 69, -284);
    BlockPos plRoofBlock75 = new BlockPos(-290, 69, -280);
    BlockPos plRoofBlock76 = new BlockPos(-290, 69, -281);
    BlockPos plRoofBlock77 = new BlockPos(-290, 69, -282);
    BlockPos plRoofBlock78 = new BlockPos(-290, 69, -283);
    BlockPos plRoofBlock79 = new BlockPos(-290, 69, -284);
    BlockPos plRoofBlock80 = new BlockPos(-289, 69, -280);
    BlockPos plRoofBlock81 = new BlockPos(-289, 69, -281);
    BlockPos plRoofBlock82 = new BlockPos(-289, 69, -282);
    BlockPos plRoofBlock83 = new BlockPos(-289, 69, -283);
    BlockPos plRoofBlock84 = new BlockPos(-289, 69, -284);

    BlockPos[] playerRoofCoordinates = {plRoofBlock1, plRoofBlock2, plRoofBlock3, plRoofBlock4, plRoofBlock5, plRoofBlock6, plRoofBlock7, plRoofBlock8,
            plRoofBlock9, plRoofBlock10, plRoofBlock11, plRoofBlock12, plRoofBlock13, plRoofBlock14, plRoofBlock15, plRoofBlock16, plRoofBlock17,
            plRoofBlock18, plRoofBlock19, plRoofBlock20, plRoofBlock21, plRoofBlock22, plRoofBlock23, plRoofBlock24, plRoofBlock25, plRoofBlock26,
            plRoofBlock27, plRoofBlock28, plRoofBlock29, plRoofBlock30, plRoofBlock31, plRoofBlock32, plRoofBlock33, plRoofBlock34,
            plRoofBlock35, plRoofBlock36, plRoofBlock37, plRoofBlock38, plRoofBlock39, plRoofBlock40, plRoofBlock41, plRoofBlock42, plRoofBlock43,
            plRoofBlock44, plRoofBlock45, plRoofBlock46, plRoofBlock47, plRoofBlock48, plRoofBlock49, plRoofBlock50, plRoofBlock51, plRoofBlock52,
            plRoofBlock53, plRoofBlock54, plRoofBlock55, plRoofBlock56, plRoofBlock57, plRoofBlock58, plRoofBlock59, plRoofBlock60, plRoofBlock61,
            plRoofBlock62, plRoofBlock63, plRoofBlock64, plRoofBlock65, plRoofBlock66, plRoofBlock67, plRoofBlock68, plRoofBlock69, plRoofBlock70,
            plRoofBlock71, plRoofBlock72, plRoofBlock73, plRoofBlock74, plRoofBlock75, plRoofBlock76, plRoofBlock77, plRoofBlock78, plRoofBlock79,
            plRoofBlock80, plRoofBlock81, plRoofBlock82, plRoofBlock83, plRoofBlock84
    };

    BlockPos[] playerWallsCoordinates = {plFrontBlockBottom1, plFrontBlockTop1, plFrontBlockBottom2,plFrontBlockTop2, plFrontBlockBottom3, plFrontBlockTop3, plFrontBlockBottom4,
            plFrontBlockTop4, plFrontBlockBottom5, plFrontBlockTop5,
            plSideBlockBottom1, plSideBlockTop1, plSideBlockBottom2, plSideBlockTop2, plSideBlockBottom3, plSideBlockTop3, plSideBlockBottom4,
            plSideBlockTop4, plSideBlockBottom5, plSideBlockTop5,
            plBackBlockBottom1, plBackBlockTop1, plBackBlockBottom2, plBackBlockTop2, plBackBlockBottom3, plBackBlockTop3, plBackBlockBottom4,
            plBackBlockTop4, plBackBlockBottom5, plBackBlockTop5, plBackBlockBottom6, plBackBlockTop6};
    //companion walls
    //front wall
//    BlockPos frontBlockBottom1 = new BlockPos(-902, 77, -522);
//    BlockPos frontBlockTop1 = new BlockPos(-902, 78, -522);
    BlockPos frontBlockBottom2 = new BlockPos(-292, 67, -279);
    BlockPos frontBlockTop2 = new BlockPos(-292, 68, -279);
    BlockPos frontBlockBottom3 = new BlockPos(-291, 67, -279);
    BlockPos frontBlockTop3 = new BlockPos(-291, 68, -279);
    BlockPos frontBlockBottom4 = new BlockPos(-290, 67, -279);
    BlockPos frontBlockTop4 = new BlockPos(-290, 68, -279);
    BlockPos frontBlockBottom5 = new BlockPos(-289, 67, -279);
    BlockPos frontBlockTop5 = new BlockPos(-289, 68, -279);
    BlockPos frontBlockBottom6 = new BlockPos(-288, 67, -279);
    BlockPos frontBlockTop6 = new BlockPos(-288, 68, -279);
    //side wall
    BlockPos sideBlockBottom1 = new BlockPos(-288, 67, -280);
    BlockPos sideBlockTop1 = new BlockPos(-288, 68, -280);
    BlockPos sideBlockBottom2 = new BlockPos(-288, 67, -281);
    BlockPos sideBlockTop2 = new BlockPos(-288, 68, -281);
    BlockPos sideBlockBottom3 = new BlockPos(-288, 67, -282);
    BlockPos sideBlockTop3 = new BlockPos(-288, 68, -282);
    BlockPos sideBlockBottom4 = new BlockPos(-288, 67, -283);
    BlockPos sideBlockTop4 = new BlockPos(-288, 68, -283);
    BlockPos sideBlockBottom5 = new BlockPos(-288, 67, -284);
    BlockPos sideBlockTop5 = new BlockPos(-288, 68, -284);
    BlockPos sideBlockBottom6 = new BlockPos(-288, 67, -285);
    BlockPos sideBlockTop6 = new BlockPos(-288, 68, -285);
    //back wall
    BlockPos backBlockBottom1 = new BlockPos(-289, 67, -285);
    BlockPos backBlockTop1 = new BlockPos(-289, 68, -285);
    BlockPos backBlockBottom2 = new BlockPos(-290, 67, -285);
    BlockPos backBlockTop2 = new BlockPos(-290, 68, -285);
    BlockPos backBlockBottom3 = new BlockPos(-291, 67, -285);
    BlockPos backBlockTop3 = new BlockPos(-291, 68, -285);
    BlockPos backBlockBottom4 = new BlockPos(-292, 67, -285);
    BlockPos backBlockTop4 = new BlockPos(-292, 68, -285);
    BlockPos backBlockBottom5 = new BlockPos(-293, 67, -285);
    BlockPos backBlockTop5 = new BlockPos(-293, 68, -285);

    BlockPos[] compHouseCoordinates = {frontBlockBottom2, frontBlockTop2, frontBlockBottom3, frontBlockTop3, frontBlockBottom4,
            frontBlockTop4, frontBlockBottom5, frontBlockTop5, frontBlockBottom6, frontBlockTop6,
            sideBlockBottom1, sideBlockTop1, sideBlockBottom2, sideBlockTop2, sideBlockBottom3, sideBlockTop3, sideBlockBottom4,
            sideBlockTop4, sideBlockBottom5, sideBlockTop5, sideBlockBottom6, sideBlockTop6,
            backBlockBottom1, backBlockTop1, backBlockBottom2, backBlockTop2, backBlockBottom3, backBlockTop3, backBlockBottom4,
            backBlockTop4, backBlockBottom5, backBlockTop5};

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
//        super.stop();
        System.out.println("stop");
        this.removerMob.fallDistance = 1.0F;
    }
    public boolean canContinueToUse() {
//        if (CompanionData.numberOfBlockDestroyed <= 10) {
//            return true;
//        }
//        System.out.println("canContinueToUse");
//        System.out.println("tryticks: " + tryTicks);
//        System.out.println(this.isValidTarget(this.mob.level, this.blockActualPos));
        return this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 1200 && /*this.isValidTarget(this.mob.level, this.blockActualPos)*/true;
//        return this.isValidTarget(this.mob.level, this.blockPos);


    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void start() {
//        this.moveMobToBlock();
        System.out.println("start is being called");
        this.tryTicks = 0;
        this.maxStayTicks = this.mob.getRandom().nextInt(this.mob.getRandom().nextInt(100) + 100) + 100;
        this.ticksSinceReachedGoal = 0;
    }

    public void playDestroyProgressSound(LevelAccessor pLevel, BlockPos pPos) {
    }



    public void playBreakSound(Level pLevel, BlockPos pPos) {
    }
    public boolean shouldRecalculatePath() {
        return this.tryTicks > 100;
    }

    String BobText1 = "<Bob The Builder> Hi I'm Bob, and I am here to help you build a house.";
    String BobText2 = "<Bob The Builder> A house is needed to protect yourself from monsters at night.";
    String BobText3 = "<Bob The Builder> You need to first collect 10 pieces of acacia wood, then collect 84 pieces of andesite stone.";
    String BobText4 = "<Bob The Builder> You must then craft that wood into 32 acacia planks and craft the stone into 84 polished andesite stone.";

    String BobText5 = "<Bob The Builder> You also need to craft two doors using the acacia planks.";

    String BobText6 = "<Bob The Builder> The crafting table can be found inside of the house next to where the blue flower was.";
    String BobText7 = "<Bob The Builder> I will be building half of the house's walls, and you will be building the other half of the walls as well as the roof.";
    String BobText8 = "<Bob The Builder> The walls should be two planks tall, with the stone roof one block above that covering the whole house.";
    String BobText9 = "<Bob The Builder> After that you should place the doors on the blue tiles.";
    String BobText10 = "<Bob The Builder> I'll keep you updated as you go along and let you know when you are finished.";
    String BobText11 = "<Bob The Builder> If you follow me I'll show you where to find the wood and where to build the house.";

    String[] bobTextArray = {BobText1, BobText2, BobText3, BobText4, BobText5, BobText6, BobText7, BobText8, BobText9, BobText10, BobText11};

    String PlayerText1 = "<Instructions> Welcome to FIX IT LAND, your new home!";
    String PlayerText2 = "<Instructions> Feel free to take some time to look around and then when you are ready, find Bob.";
    String PlayerText3 = "<Instructions> Bob requires some melon as payment for his help in building your house, which you will find in the farm in the corner.";

    String[] playerTextArray = {PlayerText1, PlayerText2, PlayerText3};

    String tutorialText1 = "<Instructions> Welcome to the tutorial!";
    String tutorialText2 = "<Instructions> Take you time to look around and read the signs.";
    String tutorialText3 = "<Instructions> You can use the mouse wheel to select the item you want in your character's hand.";
    String tutorialText4 = "<Instructions> Right click a chest or crafting table to use it.";
    String tutorialText5 = "<Instructions> Open and close your characters inventory with E";
    String tutorialText6 = "<Instructions> Press space bar to jump.";
    String tutorialText7 = "<Instructions> When you have food in your hand, right click an animal to become friends with it.";
    String tutorialText8 = "<Instructions> When you have a block in your hand, right click to place the block.";
    String tutorialText9 = "<Instructions> If you feel comfortable with the things you have learnt, please place a red flower from the chest on the grass block next to the trees.";
    String tutorialText10 = "<Instructions> Good luck! To see the instructions again simply press /";

    String[] tutorialTextArray = {tutorialText1, tutorialText2, tutorialText3, tutorialText4, tutorialText5,
            tutorialText6, tutorialText7, tutorialText8, tutorialText9, tutorialText10};

    /**
     * Keep ticking a continuous task that has already been started
     */

    public static Player getPlayer()
    {
        return _player;
    }
    public void tick() {
//        super.tick();
        BlockPos blockPos = this.getMoveToTarget();
        talkTicks++;
        //tutorial begin text
        if (!CompanionData.tutorialInstructionsDialogue && !CompanionData.tutorialBegin) {
//            System.out.println("!CompanionData.tutorialInstructionsDialogue && !CompanionData.tutorialBegin");
            BlockPos questPosTute = new BlockPos(52, -59, 148);
            Level level = this.level;
            Block blueOrchidTute = Blocks.BLUE_ORCHID;
//            System.out.println("pre if (!level.getBlockState(questPos).is(blueOrchid))");
            if (!level.getBlockState(questPosTute).is(blueOrchidTute) && !CompanionData.tutorialInstructionsDialogue) {
//                System.out.println("we got to the if statement, blue orchid is not there");
                if (talkTicks % 96 == 0) {
                    //((tutorialTextArray[tutorialTextCounter].length()) * 2)
                    System.out.println("String length *2: " + ((tutorialTextArray[tutorialTextCounter].length()) * 2));
                    Minecraft.getInstance().player.chat((tutorialTextArray[tutorialTextCounter]));
                    HumanCompanions.logger.severe(tutorialTextArray[tutorialTextCounter]);
                    tutorialTextCounter++;
                }
                if (tutorialTextCounter == 10) {
                    CompanionData.tutorialInstructionsDialogue = true;
                    HumanCompanions.logger.severe("tutorial_instructions_finished");
                    System.out.println("Tutorial Begin");
                }
            }
        }
        //tutorial end text
        if (!CompanionData.tutorialEnd) {
//            System.out.println("!CompanionData.tutorialEnd");
            BlockPos questPosTute2 = new BlockPos(34, -59, 170);
            Level level = this.level;
            Block poppy = Blocks.POPPY;
            if (level.getBlockState(questPosTute2).is(poppy)) {
//                System.out.println("we got to the if statement, blue orchid is not there");
                if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.chat("<Instructions> Tutorial Complete");
                HumanCompanions.logger.severe("tutorial_finished_message");
                System.out.println("Tutorial end");
                CompanionData.tutorialInstructionsDialogue = true;
                CompanionData.tutorialEnd = true;
                }
            }
        }

        // pre bob instructions
        if (!CompanionData.questBegin && !CompanionData.playerInstructionsDialogue) {
            BlockPos questPos = new BlockPos(-314, 69, -253);
            Level level = this.level;
            Block blueOrchid = Blocks.BLUE_ORCHID;
//            System.out.println("pre if (!level.getBlockState(questPos).is(blueOrchid))");
            if (!level.getBlockState(questPos).is(blueOrchid) && !CompanionData.playerInstructionsDialogue) {
                if (talkTicks % 96 * 2 == 0) {
                    System.out.println("String length *2: " + ((playerTextArray[instructionsTextCounter].length()) * 2));
                    Minecraft.getInstance().player.chat((playerTextArray[instructionsTextCounter]));
                    HumanCompanions.logger.severe(playerTextArray[instructionsTextCounter]);
                    instructionsTextCounter++;
                }
                if (instructionsTextCounter == 3) {
                    CompanionData.playerInstructionsDialogue = true;
                    HumanCompanions.logger.severe("player_instructions_finished");
                    System.out.println("Pre Bob Instructions");
                }
//                System.out.println("we got to the if statement, blue orchid is not there");
//                }
            }
        }
//        System.out.println("tame trigger = " + tameTrigger);
        //bob instructions
        if (tameTrigger && !CompanionData.questBegin) {
            if (talkTicks % 96 == 0) {
                System.out.println("String length *2: " + ((bobTextArray[bobTextCounter].length()) * 2));
                AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent(bobTextArray[bobTextCounter]), null);
                HumanCompanions.logger.severe(bobTextArray[bobTextCounter]);
                bobTextCounter++;
            }
            if (bobTextCounter == 11) {
                tameTrigger = false;
                CompanionData.questBegin = true;
                HumanCompanions.logger.severe("bob_instructions_finished");
                System.out.println("quest begin equals: " + CompanionData.questBegin);
            }
        }
        //navigation and destroying of blocks
        if (CompanionData.questBegin) {
            //this locates and removes the blocks
            if (CompanionData.numberOfWoodDestroyed < 10) {
                if (!blockPos.closerThan(this.mob.position(), this.acceptedDistance())) {
                    this.reachedTarget = false;
                    ++this.tryTicks;
                    if (this.shouldRecalculatePath()) {
                        System.out.println("is still going");
                        this.mob.getNavigation().moveTo((double) ((float) blockPos.getX()), (double) blockPos.getY(), (double) ((float) blockPos.getZ()), this.speedModifier);
                        System.out.println("renavigate");
                        //                this.mob.getNavigation().moveTo((double)((float)this.mob.position().x) +.5D, (double)((float)this.mob.position().y), (double)((float)this.mob.position().z) +.5D, this.speedModifier);
                    }
                } else {
                    System.out.println(this.mob.position());
                    this.reachedTarget = true;
                    System.out.println("reached target");
                    --this.tryTicks;
                }

                Level level = this.removerMob.level;
                Random random = this.removerMob.getRandom();

                if (this.isReachedTarget() && blockPos != null) {
                    if (this.ticksSinceReachedGoal > 0) {
                        if (!level.isClientSide) {
                            double d0 = 0.08D;
                            ((ServerLevel) level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.ACACIA_PLANKS)), (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.7D, (double) blockPos.getZ() + 0.5D, 3, ((double) random.nextFloat() - 0.5D) * 0.08D, ((double) random.nextFloat() - 0.5D) * 0.08D, ((double) random.nextFloat() - 0.5D) * 0.08D, (double) 0.15F);
                        }
                    }

                    if (this.ticksSinceReachedGoal % 2 == 0) {
                        if (this.ticksSinceReachedGoal % 6 == 0) {
                            this.playDestroyProgressSound(level, this.blockPos);
                        }
                    }

                    //this is how long it takes to destroy the block once it has been reached
                    //then removes the block at blockpos, then adds that same item to either a stack of existing or an empty slot
                    if (this.ticksSinceReachedGoal > 70) {
                        if (!level.getBlockState(blockActualPos).isAir()) {
                            level.removeBlock(blockActualPos, false);
                            CompanionData.numberOfWoodDestroyed++;
                            inventory.addItem(Items.ACACIA_PLANKS.getDefaultInstance());
                            inventory.addItem(Items.ACACIA_PLANKS.getDefaultInstance());
                            inventory.addItem(Items.POLISHED_ANDESITE.getDefaultInstance());
                            inventory.addItem(Items.POLISHED_ANDESITE.getDefaultInstance());
                        } else if (!level.getBlockState(blockActualPos.above()).isAir()) {
                            level.removeBlock(blockActualPos.above(), false);
                            CompanionData.numberOfWoodDestroyed++;
                            inventory.addItem(Items.ACACIA_PLANKS.getDefaultInstance());
                            inventory.addItem(Items.ACACIA_PLANKS.getDefaultInstance());
                            inventory.addItem(Items.POLISHED_ANDESITE.getDefaultInstance());
                            inventory.addItem(Items.POLISHED_ANDESITE.getDefaultInstance());
                            tryFindBlock();
                        }
//                        System.out.println("number of wood destroyed: " + CompanionData.numberOfWoodDestroyed);
                    }
                    if (!level.isClientSide) {
                        for (int i = 0; i < 20; ++i) {
                            double d3 = random.nextGaussian() * 0.02D;
                            double d1 = random.nextGaussian() * 0.02D;
                            double d2 = random.nextGaussian() * 0.02D;
                            ((ServerLevel) level).sendParticles(ParticleTypes.POOF, (double) blockPos.getX() + 0.5D, (double) blockPos.getY(), (double) blockPos.getZ() + 0.5D, 1, d3, d1, d2, (double) 0.15F);
                        }
                        this.playBreakSound(level, blockPos);
                    }
                }
            }

            //places the blocks
            if (CompanionData.numberOfWoodDestroyed >= 10 && !CompanionData.companionHalfWallsBuilt) {
                if (!CompanionData.isCompInPlace) {
                    this.mob.getNavigation().moveTo(-290, 67, -277, 1.5);
                    CompanionData.isCompInPlace = true;
                }
                if (this.ticksSinceReachedGoal % 8 == 0) {
                    Level level = this.mob.level;
                    BlockState blockstate = Blocks.ACACIA_PLANKS.defaultBlockState();

                    level.setBlock(compHouseCoordinates[blockCounter], blockstate, 3);
                    level.gameEvent(this.mob, GameEvent.BLOCK_PLACE, compHouseCoordinates[blockCounter]);
                    blockCounter++;
//                    System.out.println("block counter: " + blockCounter);
                }
            }

            //checks if companions has placed all their blocks
            if (blockCounter >= 32 && !CompanionData.companionHalfWallsBuilt) {
                Block acacia = Blocks.ACACIA_PLANKS;
                this.mob.getNavigation().moveTo(-290, 67, -277, 1);
                for (BlockPos pPos : compHouseCoordinates) {
                    if (level.getBlockState(pPos).is(acacia)) {
                        compWallsCheckCounter++;
                        if (compWallsCheckCounter == 32) {
                            CompanionData.companionHalfWallsBuilt = true;
                            HumanCompanions.logger.severe("companion_walls_built");
                            if (CompanionData.compDoorPlaced && CompanionData.companionHalfWallsBuilt) {
                                if (!CompanionData.compWallsFinDialogue) {
                                    AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> I've finished my half of the house!"), null);
                                    HumanCompanions.logger.severe("companion_walls_finished_message");
                                    CompanionData.compWallsFinDialogue = true;
                                }
                            }
                        }
                    }
                }
            }

            //help message
            if (CompanionData.companionHalfWallsBuilt) {
                if (!CompanionData.playerWallsHelpDialogue) {
                    AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> If you need anymore planks or stone, I have some spare!"), null);
                    HumanCompanions.logger.severe("companion_help_message");
                    CompanionData.playerWallsHelpDialogue = true;
                }
            }

            //checks if player has finished walls
            if (!CompanionData.playersHalfBuilt && !CompanionData.playerWallsBuilt) {
                if (this.ticksSinceReachedGoal % 6 == 0) {
                    Level level = this.mob.level;
                    Block acaciaWood = Blocks.ACACIA_PLANKS;

                    wallCheckCounter = 0;
                    for (BlockPos i : playerWallsCoordinates) {
                        if (level.getBlockState(i).is(acaciaWood)) {
//                        System.out.println((wallCheckCounter + 1) + " blocks complete!");
                            wallCheckCounter++;
                            System.out.println("wall check counter is: " + wallCheckCounter);
                        }
                    }
                }

                        if (wallCheckCounter >= 8) {
                            if (!CompanionData.player8WallsFinDialogue) {
                                AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> You have finished 25% of the walls!"), null);
                                HumanCompanions.logger.severe("player_walls_25_percent_finished_message");
                                CompanionData.player8WallsFinDialogue = true;
                            }
                        }
                        if (wallCheckCounter >= 16) {
                            if (!CompanionData.player16WallsFinDialogue) {
                                AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> You have finished 50% of the walls!"), null);
                                HumanCompanions.logger.severe("player_walls_50_percent_finished_message");
                                CompanionData.player16WallsFinDialogue = true;
                            }
                        }
                        if (wallCheckCounter >= 24) {
                            if (!CompanionData.player24WallsFinDialogue) {
                                AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> You have finished 75% of the walls!"), null);
                                HumanCompanions.logger.severe("player_walls_75_percent_finished_message");
                                CompanionData.player24WallsFinDialogue = true;
                            }
                        }
                        if (wallCheckCounter >= 32) {
                            CompanionData.playerWallsBuilt = true;
                            if (!CompanionData.playerWallsFinDialogue) {
                                AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> You have 100% finished the walls of the house!"), null);
                                HumanCompanions.logger.severe("player_walls_finished_message");
                                CompanionData.playerWallsFinDialogue = true;
                            }
                        }
                    }



            //checks if player has finished roof
            if (!CompanionData.playersHalfBuilt && !CompanionData.playerRoofBuilt) {
                if (this.ticksSinceReachedGoal % 6 == 0) {
                    Level level = this.mob.level;
                    Block andesiteStone = Blocks.POLISHED_ANDESITE;

                    roofCheckCounter = 0;
                    for (BlockPos e : playerRoofCoordinates) {
                        if (level.getBlockState(e).is(andesiteStone)) {
//                        System.out.println((roofCheckCounter + 1) + " blocks complete!");
                            roofCheckCounter++;
                        }
                    }
                        if (roofCheckCounter >= 21) {
                            if (!CompanionData.player21RoofFinDialogue) {
                                AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> You have finished 25% of the roof!"), null);
                                HumanCompanions.logger.severe("player_roof_25_percent_finished_message");
                                CompanionData.player21RoofFinDialogue = true;
                            }
                        }
                        if (roofCheckCounter >= 42) {
                            if (!CompanionData.player42RoofFinDialogue) {
                                AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> You have finished 50% of the roof!"), null);
                                HumanCompanions.logger.severe("player_roof_50_percent_finished_message");
                                CompanionData.player42RoofFinDialogue = true;
                            }
                        }
                        if (roofCheckCounter >= 63) {
                            if (!CompanionData.player63RoofFinDialogue) {
                                AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> You have finished 75% of the roof!"), null);
                                HumanCompanions.logger.severe("player_roof_75_percent_finished_message");
                                CompanionData.player63RoofFinDialogue = true;
                            }
                        }
                    }
                    if (roofCheckCounter >= 84) {
                        CompanionData.playerRoofBuilt = true;
                        if (!CompanionData.playerRoofFinDialogue) {
                            AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> You have 100% finished the roof of the house!"), null);
                            HumanCompanions.logger.severe("player_roof_finished_message");
                            CompanionData.playerRoofFinDialogue = true;
                        }
                    }
                }

            if (!CompanionData.playerDoorPlaced) {
                Level level = this.mob.level;
                BlockPos playerDoorPos = new BlockPos(-294, 67, -279);
                BlockPos playerDoorPos1 = new BlockPos(-293, 67, -279);
                Block acaciaDoor = Blocks.ACACIA_DOOR;
//                System.out.println("blockstate of left door is: " + level.getBlockState(playerDoorPos));
                if (level.getBlockState(playerDoorPos).is(acaciaDoor) && level.getBlockState(playerDoorPos1).is(acaciaDoor)) {
//                        is(acaciaDoor)) {
                    CompanionData.playerDoorPlaced = true;
                    if (!CompanionData.playerDoorFinDialogue) {
                        System.out.println("acacia door placed by player");
                        AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> You have correctly placed the door of the house!"), null);
                        HumanCompanions.logger.severe("player_door_finished_message");
                        CompanionData.playerDoorFinDialogue = true;
                    }
                }
            }

            if (CompanionData.playerWallsBuilt && CompanionData.playerRoofBuilt && CompanionData.playerDoorPlaced) {
                CompanionData.playersHalfBuilt = true;
                if (!CompanionData.playerHalfFinDialogue) {
                    AbstractHumanCompanionEntity.getPlayer().sendMessage(new TextComponent("<Bob The Builder> You have 100% finished the house, well done!"), null);
                    CompanionData.playerHalfFinDialogue = true;
                }
            }
        }
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
    /**
     * Return true to set given position as destination
     */
    protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
        ChunkAccess chunkaccess = pLevel.getChunk(SectionPos.blockToSectionCoord(pPos.getX()), SectionPos.blockToSectionCoord(pPos.getZ()), ChunkStatus.FULL, false);
        if (chunkaccess == null) {
            return false;
        } else {
            return chunkaccess.getBlockState(pPos).is(this.blockToRemove);
        }
    }

}
