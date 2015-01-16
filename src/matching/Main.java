package matching;

import java.util.HashSet;

import matching.MatchObject.Type;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		HashSet<SpacialMatchObject> objs = new HashSet<>();
		
//		for(int i = 0; i < 10; i++){
//			objs.add(new SpacialMatchObject("Dude " + i, Type.A, new Vector((i + 1) * 50, 50)));
//		}
//				
//		for(int i = 0; i < 10; i++){
//			objs.add(new SpacialMatchObject("Girl " + i, Type.B, new Vector((i + 1) * 50, 400)));
//		}
		int i = 50;
		SpacialMatchObject alex = new SpacialMatchObject("Alex", Type.A, new Vector((i++) * 50, 100));
		SpacialMatchObject bob = new SpacialMatchObject("Bob", Type.A, new Vector((i++) * 50, 100));
		SpacialMatchObject clark = new SpacialMatchObject("Clark", Type.A, new Vector((i++) * 50, 100));
		SpacialMatchObject derek = new SpacialMatchObject("Derek", Type.A, new Vector((i++) * 50, 100));
		SpacialMatchObject eric = new SpacialMatchObject("Eric", Type.A, new Vector((i++) * 50, 100));

		i = 50;
		SpacialMatchObject faye = new SpacialMatchObject("Faye", Type.B, new Vector((i++) * 50, 300));
		SpacialMatchObject gayle = new SpacialMatchObject("Gayle", Type.B, new Vector((i++) * 50, 300));
		SpacialMatchObject hillary = new SpacialMatchObject("Hillary", Type.B, new Vector((i++) * 50, 300));
		SpacialMatchObject ilana = new SpacialMatchObject("Ilana", Type.B, new Vector((i++) * 50, 300));
		SpacialMatchObject julia = new SpacialMatchObject("Julia", Type.B, new Vector((i++) * 50, 300));

		objs.add(alex);
		objs.add(bob);
		objs.add(clark);
		objs.add(derek);
		objs.add(eric);
		objs.add(faye);
		objs.add(gayle);
		objs.add(hillary);
		objs.add(ilana);
		objs.add(julia);
		
		alex.addPreference(faye).addPreference(gayle);
		bob.addPreference(gayle).addPreference(ilana);
		clark.addPreference(julia).addPreference(gayle);
		derek.addPreference(faye).addPreference(ilana);
		eric.addPreference(faye).addPreference(hillary);
		
		faye.addPreference(alex).addPreference(clark);
		gayle.addPreference(clark).addPreference(derek);
		hillary.addPreference(bob).addPreference(eric);
		ilana.addPreference(alex).addPreference(derek);
		julia.addPreference(eric).addPreference(derek);
		
		
		
		new GUI(objs);
	}

}
