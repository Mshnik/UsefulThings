package matching;

import java.util.ArrayList;

public class MatchObject<T extends MatchObject<T>> {

	enum Type{
		A,
		B
	}
	
	private static int ID = 0;
	
	public final String name;
	public final int id;
	public final Type type;
	private ArrayList<T> preferences;

	public MatchObject(String name, Type t){
		this.name = name;
		this.id = ID;
		this.type = t;
		preferences = new ArrayList<T>();
		ID++;
	}
	
	public ArrayList<T> getPreferences(){
		return new ArrayList<T>(preferences);
	}
	
	public T getPreference(int i){
		return preferences.get(i);
	}
	
	public void setPreference(T obj, int i){
		preferences.set(i, obj);
	}
	
	/** Returns this */
	public MatchObject<T> addPreference(T obj){
		preferences.add(obj);
		return this;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean equals(Object obj){
		if(! (obj instanceof MatchObject)) return false;
		return ((MatchObject) obj).id == id;
	}
	
	public int hashCode(){
		return id;
	}
	
	public String toString(){
		return id + " - " + name + " (" + type + ")";
	}
	
}
