package net.fabricmc.example.mixin;

import net.minecraft.client.world.GeneratorType;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;

import java.util.List;

import javax.xml.soap.Text;

import org.spongepowered.asm.mixin.Final;
//import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//@Mixin(TitleScreen.class)
@Mixin(GeneratorType.class)
public class ExampleMixin {
//	@Shadow 
//	@Final
//	private List<GeneratorType> VALUES;
	
//	@Inject(at = @At("TAIL"), method = "GeneratorType(Ljava/lang/String;)GeneratorType")
//	private void GeneratorType(String translationKey) {
//		System.out.println("This line is printed by an example mod mixin!");
//		int test = 0;
//		for(GeneratorType asdf : VALUES) {
//			System.out.println(test++);
//		}
//	}
}
