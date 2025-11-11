package me.lortseam.bettermounthud.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public abstract class GuiMixin {
    @Shadow @Final private Minecraft minecraft;

    @Redirect(method = "renderFoodLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    private int bettermounthud$alwaysRenderFood(Gui gui, LivingEntity entity) {
        return 0;
    }

    @Redirect(method = "maybeRenderJumpMeter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;jumpableVehicle()Lnet/minecraft/world/entity/PlayerRideableJumping;"))
    private PlayerRideableJumping bettermounthud$switchBar(LocalPlayer player) {
        return bettermounthud$showExperienceBar() ? null : player.jumpableVehicle();
    }

    @Redirect(method = "maybeRenderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;jumpableVehicle()Lnet/minecraft/world/entity/PlayerRideableJumping;"))
    private PlayerRideableJumping bettermounthud$renderExperienceBar_jumpableVehicle(LocalPlayer player) {
        return null;
    }

    @Redirect(method = "maybeRenderExperienceBar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;isExperienceBarVisible()Z"))
    private boolean bettermounthud$renderExperienceBar(Gui gui) {
        return bettermounthud$showExperienceBar();
    }

    @Redirect(method = "renderExperienceLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;isExperienceBarVisible()Z"))
    private boolean bettermounthud$renderExperienceLevel(Gui gui) {
        return bettermounthud$showExperienceBar();
    }

    @Unique
    private boolean bettermounthud$showExperienceBar() {
        if (!minecraft.gameMode.hasExperience()) return false;
        if (minecraft.player.jumpableVehicle() == null) return true;

        if (!minecraft.options.keyJump.isDown()) {
            if (minecraft.player.getJumpRidingScale() <= 0) {
                return true;
            }
        }

        return false;
    }
}
