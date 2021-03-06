package org.manapart.item_filters;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class ItemFilterBlock extends HopperBlock {
    public ItemFilterBlock() {
        super(Block.Properties.create(Material.IRON, MaterialColor.BLUE).hardnessAndResistance(4f).sound(SoundType.METAL));
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new ItemFilterEntity(false);
    }

    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isRemote) {
            return ActionResultType.PASS;
        } else {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof ItemFilterEntity) {
                player.openContainer((ItemFilterEntity) tileentity);
                player.addStat(Stats.INSPECT_HOPPER);
            }
            return ActionResultType.PASS;
        }
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof ItemFilterEntity) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (ItemFilterEntity) tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ItemFilterEntity) {
            ((ItemFilterEntity) tileentity).onEntityCollision(entityIn);
        }

    }

}
