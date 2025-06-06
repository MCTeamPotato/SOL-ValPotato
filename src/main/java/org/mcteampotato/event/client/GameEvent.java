package org.mcteampotato.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Matrix4f;
import org.mcteampotato.SOLValpotato;
import org.mcteampotato.attchment.FoodDataAttachment;
import org.mcteampotato.attchment.FoodInstance;
import org.mcteampotato.config.ValpotatoConfig;

@EventBusSubscriber(modid = SOLValpotato.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class GameEvent {
    @SubscribeEvent
    public static void overlayEvent(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (mc.player.isCreative()) return;
        if (mc.player.getAirSupply() < mc.player.getMaxAirSupply()) return;
        if (mc.player.getVehicle() != null) return;
        FoodDataAttachment foodData = mc.player.getData(SOLValpotato.FOOD_DATA);
        GuiGraphics guiGraphics = event.getGuiGraphics();
        int width = guiGraphics.guiWidth() / 2 + 91;
        int height = guiGraphics.guiHeight() - 32;
        for (int i = 0; i < foodData.getSlots().size(); i++) {
            FoodInstance foodInstance = foodData.getSlots().get(i);
            int actualWidth = width - (i % 4) * 20;
            int actualHeight = height - (i / 4) * 20;
            guiGraphics.fill(actualWidth - 18, actualHeight - 18, actualWidth, actualHeight, 128 << 24 & 0xFF000000);
            guiGraphics.renderItem(foodInstance.getInfo().getItemStack(), actualWidth - 18, actualHeight - 18);
            PoseStack pose = guiGraphics.pose();
            pose.pushPose();
            Matrix4f matrix4f = pose.last().pose();
            matrix4f.translate(0.0F, 0.0F, 200.0F);
            int remainingSeconds = foodInstance.getRemainingSeconds();
            boolean isMin = remainingSeconds >= 60;
            if (isMin) remainingSeconds = (int) Math.ceil((float) remainingSeconds / 60);
            String text = "%s%s".formatted(remainingSeconds, isMin ? "m" : "s");
            guiGraphics.drawString(mc.font, text, (actualWidth - mc.font.width(text)), actualHeight -9, 0xFFFFFFFF, false);
            pose.popPose();
        }
    }
}