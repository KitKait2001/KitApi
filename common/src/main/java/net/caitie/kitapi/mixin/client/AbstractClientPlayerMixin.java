package net.caitie.kitapi.mixin.client;

import net.caitie.kitapi.KitApiMain;
import net.caitie.kitapi.api.entity.player.IKitPlayer;
import net.caitie.kitapi.api.util.Utils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Shadow public abstract PlayerSkin getSkin();

    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    protected void kitApi_AbstractClientPlayer_getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        if (KitApiMain.CONFIG.renderCustomPlayers) {
            PlayerSkin original = getSkin();
            PlayerSkin skin = new PlayerSkin(Utils.BLANK_TEXTURE_64, null, original.capeTexture(), original.elytraTexture(), ((IKitPlayer)(AbstractClientPlayer)(Object)this).isMale() ? PlayerSkin.Model.WIDE : PlayerSkin.Model.SLIM, original.secure());
            cir.setReturnValue(skin);
        }
    }

}
