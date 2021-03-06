package space.bbkr.locky;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;

public class LockItem extends Item {

	public LockItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		PlayerEntity player = ctx.getPlayer();
		if (!ctx.getWorld().isClient && player.isSneaking()) {
			ItemStack lock = player.getStackInHand(player.getActiveHand());
			BlockEntity be = ctx.getWorld().getBlockEntity(ctx.getBlockPos());
			if (be instanceof LockableContainerBlockEntity) {
				CompoundTag tag = be.toTag(new CompoundTag());
				if (lock.hasCustomName() && (!tag.contains("Lock") || tag.getString("Lock").equals(""))) {
					tag.putString("Lock", lock.getName().getString());
					player.addChatMessage(new TranslatableText("msg.locky.locked", lock.getName().getString()), true);
					if (!player.isCreative()) {
						player.getStackInHand(player.getActiveHand()).decrement(1);
					}
					ctx.getWorld().playSound(null, ctx.getBlockPos().getX(), ctx.getBlockPos().getY(), ctx.getBlockPos().getZ(), SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 0.5F, ctx.getWorld().random.nextFloat() * 0.1F + 0.9F);
				} else {
					if (!lock.hasCustomName()) player.addChatMessage(new TranslatableText("msg.locky.cantlock.noname"), true);
					else player.addChatMessage(new TranslatableText("msg.locky.cantlock.alreadylocked"), true);
					ctx.getWorld().playSound(null, ctx.getBlockPos().getX(), ctx.getBlockPos().getY(), ctx.getBlockPos().getZ(), SoundEvents.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.5F, ctx.getWorld().random.nextFloat() * 0.1F + 0.9F);
				}
				be.fromTag(tag);
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}
}
