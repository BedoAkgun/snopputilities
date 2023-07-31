
package net.bedirakgun.snoppsadditions.item;

import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.World;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ActionResult;
import net.minecraft.network.IPacket;
import net.minecraft.item.UseAction;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.client.util.ITooltipFlag;

import net.bedirakgun.snoppsadditions.procedures.StoneStaffCooldownProcedure;
import net.bedirakgun.snoppsadditions.procedures.StoneHealingStaffDirectHealingProcedure;
import net.bedirakgun.snoppsadditions.procedures.SpawnStoneStaffHealAreaProcedure;
import net.bedirakgun.snoppsadditions.entity.renderer.StoneHealingStaffRenderer;
import net.bedirakgun.snoppsadditions.SnoppsAdditionsModElements;

import java.util.stream.Stream;
import java.util.Random;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.AbstractMap;

@SnoppsAdditionsModElements.ModElement.Tag
public class StoneHealingStaffItem extends SnoppsAdditionsModElements.ModElement {
	@ObjectHolder("snopps_additions:stone_healing_staff")
	public static final Item block = null;
	public static final EntityType arrow = (EntityType.Builder.<ArrowCustomEntity>create(ArrowCustomEntity::new, EntityClassification.MISC)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(ArrowCustomEntity::new)
			.size(0.5f, 0.5f)).build("projectile_stone_healing_staff").setRegistryName("projectile_stone_healing_staff");

	public StoneHealingStaffItem(SnoppsAdditionsModElements instance) {
		super(instance, 47);
		FMLJavaModLoadingContext.get().getModEventBus().register(new StoneHealingStaffRenderer.ModelRegisterHandler());
	}

	@Override
	public void initElements() {
		elements.items.add(() -> new ItemRanged());
		elements.entities.add(() -> arrow);
	}

	public static class ItemRanged extends Item {
		public ItemRanged() {
			super(new Item.Properties().group(ItemGroup.COMBAT).maxDamage(180));
			setRegistryName("stone_healing_staff");
		}

		@Override
		public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity entity, Hand hand) {
			entity.setActiveHand(hand);
			return new ActionResult(ActionResultType.SUCCESS, entity.getHeldItem(hand));
		}

		@Override
		public void addInformation(ItemStack itemstack, World world, List<ITextComponent> list, ITooltipFlag flag) {
			super.addInformation(itemstack, world, list, flag);
			list.add(new StringTextComponent("On direct hit: 3 Hearts heal"));
			list.add(new StringTextComponent("On ground impact: Regeneration III effect in a 2.5 block radius for 3 seconds"));
			list.add(new StringTextComponent("\u00A785.5 second cooldown"));
		}

		@Override
		public UseAction getUseAction(ItemStack itemstack) {
			return UseAction.BOW;
		}

		@Override
		public int getUseDuration(ItemStack itemstack) {
			return 72000;
		}

		@Override
		public void onPlayerStoppedUsing(ItemStack itemstack, World world, LivingEntity entityLiving, int timeLeft) {
			if (!world.isRemote && entityLiving instanceof ServerPlayerEntity) {
				ServerPlayerEntity entity = (ServerPlayerEntity) entityLiving;
				double x = entity.getPosX();
				double y = entity.getPosY();
				double z = entity.getPosZ();
				if (true) {
					ArrowCustomEntity entityarrow = shoot(world, entity, random, 0.3f, 0, 0);
					itemstack.damageItem(1, entity, e -> e.sendBreakAnimation(entity.getActiveHand()));
					entityarrow.pickupStatus = AbstractArrowEntity.PickupStatus.DISALLOWED;

					StoneStaffCooldownProcedure.executeProcedure(
							Stream.of(new AbstractMap.SimpleEntry<>("entity", entity), new AbstractMap.SimpleEntry<>("itemstack", itemstack))
									.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
				}
			}
		}
	}

	@OnlyIn(value = Dist.CLIENT, _interface = IRendersAsItem.class)
	public static class ArrowCustomEntity extends AbstractArrowEntity implements IRendersAsItem {
		public ArrowCustomEntity(FMLPlayMessages.SpawnEntity packet, World world) {
			super(arrow, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, World world) {
			super(type, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, double x, double y, double z, World world) {
			super(type, x, y, z, world);
		}

		public ArrowCustomEntity(EntityType<? extends ArrowCustomEntity> type, LivingEntity entity, World world) {
			super(type, entity, world);
		}

		@Override
		public IPacket<?> createSpawnPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack getItem() {
			return new ItemStack(Items.FIREWORK_STAR);
		}

		@Override
		protected ItemStack getArrowStack() {
			return ItemStack.EMPTY;
		}

		@Override
		protected void arrowHit(LivingEntity entity) {
			super.arrowHit(entity);
			entity.setArrowCountInEntity(entity.getArrowCountInEntity() - 1);
		}

		@Override
		public void onEntityHit(EntityRayTraceResult entityRayTraceResult) {
			super.onEntityHit(entityRayTraceResult);
			Entity entity = entityRayTraceResult.getEntity();
			Entity sourceentity = this.func_234616_v_();
			Entity immediatesourceentity = this;
			double x = this.getPosX();
			double y = this.getPosY();
			double z = this.getPosZ();
			World world = this.world;

			StoneHealingStaffDirectHealingProcedure.executeProcedure(Stream.of(new AbstractMap.SimpleEntry<>("entity", entity)).collect(HashMap::new,
					(_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
		}

		@Override
		public void func_230299_a_(BlockRayTraceResult blockRayTraceResult) {
			super.func_230299_a_(blockRayTraceResult);
			double x = blockRayTraceResult.getPos().getX();
			double y = blockRayTraceResult.getPos().getY();
			double z = blockRayTraceResult.getPos().getZ();
			World world = this.world;
			Entity entity = this.func_234616_v_();
			Entity immediatesourceentity = this;

			SpawnStoneStaffHealAreaProcedure.executeProcedure(Stream
					.of(new AbstractMap.SimpleEntry<>("world", world), new AbstractMap.SimpleEntry<>("x", x), new AbstractMap.SimpleEntry<>("y", y),
							new AbstractMap.SimpleEntry<>("z", z))
					.collect(HashMap::new, (_m, _e) -> _m.put(_e.getKey(), _e.getValue()), Map::putAll));
		}

		@Override
		public void tick() {
			super.tick();
			double x = this.getPosX();
			double y = this.getPosY();
			double z = this.getPosZ();
			World world = this.world;
			Entity entity = this.func_234616_v_();
			Entity immediatesourceentity = this;
			if (this.inGround)
				this.remove();
		}
	}

	public static ArrowCustomEntity shoot(World world, LivingEntity entity, Random random, float power, double damage, int knockback) {
		ArrowCustomEntity entityarrow = new ArrowCustomEntity(arrow, entity, world);
		entityarrow.shoot(entity.getLook(1).x, entity.getLook(1).y, entity.getLook(1).z, power * 2, 0);
		entityarrow.setSilent(true);
		entityarrow.setIsCritical(false);
		entityarrow.setDamage(damage);
		entityarrow.setKnockbackStrength(knockback);
		world.addEntity(entityarrow);
		double x = entity.getPosX();
		double y = entity.getPosY();
		double z = entity.getPosZ();
		world.playSound((PlayerEntity) null, (double) x, (double) y, (double) z,
				(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.note_block.chime")),
				SoundCategory.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityarrow;
	}

	public static ArrowCustomEntity shoot(LivingEntity entity, LivingEntity target) {
		ArrowCustomEntity entityarrow = new ArrowCustomEntity(arrow, entity, entity.world);
		double d0 = target.getPosY() + (double) target.getEyeHeight() - 1.1;
		double d1 = target.getPosX() - entity.getPosX();
		double d3 = target.getPosZ() - entity.getPosZ();
		entityarrow.shoot(d1, d0 - entityarrow.getPosY() + (double) MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F, d3, 0.3f * 2, 12.0F);
		entityarrow.setSilent(true);
		entityarrow.setDamage(0);
		entityarrow.setKnockbackStrength(0);
		entityarrow.setIsCritical(false);
		entity.world.addEntity(entityarrow);
		double x = entity.getPosX();
		double y = entity.getPosY();
		double z = entity.getPosZ();
		entity.world.playSound((PlayerEntity) null, (double) x, (double) y, (double) z,
				(net.minecraft.util.SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.note_block.chime")),
				SoundCategory.PLAYERS, 1, 1f / (new Random().nextFloat() * 0.5f + 1));
		return entityarrow;
	}
}
