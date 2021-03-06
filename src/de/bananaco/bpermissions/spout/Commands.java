package de.bananaco.bpermissions.spout;

import java.util.Arrays;
import java.util.List;

import org.spout.api.Spout;
import org.spout.api.command.CommandSource;

import de.bananaco.bpermissions.api.Group;
import de.bananaco.bpermissions.api.User;
import de.bananaco.bpermissions.api.World;
import de.bananaco.bpermissions.api.WorldManager;
import de.bananaco.bpermissions.api.util.Calculable;
import de.bananaco.bpermissions.api.util.CalculableType;
import de.bananaco.bpermissions.api.util.Permission;

public class Commands {
	
	private final WorldManager instance = WorldManager.getInstance();
	
	private World world = null;
	private CalculableType calc = null;
	private String name = null;
	
	protected Commands() {
		if (Spout.getEngine().getWorlds().size() == 1)
			world = instance.getWorld(Spout.getEngine().getWorlds().iterator().next().getName());
	}
	
	protected String format(String message) {
		return Permissions.format(message);
	}
	
	public void setWorld(String w, CommandSource sender) {
		World world = instance.getWorld(w);
		// If the world does not exist
		if(world == null) {
			sender.sendMessage(format("Please select a loaded world!"));
			return;
		}
		// If a different world is selected
		if(!world.equals(this.world))
			calc = null;
		
		this.world = world;
		sender.sendMessage(format("Set selected world to "+world.getName()));
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setCalculable(CalculableType type, String c, CommandSource sender) {
		// If the world does not exist
		if(world == null) {
			sender.sendMessage(format("Please select a loaded world!"));
			return;
		}
		calc = type;
		name = c;
		sender.sendMessage(format(getCalculable().getName()+" selected."));
	}
	
	public Calculable getCalculable() {
		if(name == null)
			return null;
		if(calc == null)
			return null;
		return world.get(name, calc);
	}
	
	/*
	 * Main functions go here
	 */
	
	public void addGroup(String group, CommandSource sender) {
		getCalculable().addGroup(group);
		sender.sendMessage(format("Added "+group+" to "+getCalculable().getName()));
	}
	
	public void removeGroup(String group, CommandSource sender) {
		getCalculable().removeGroup(group);
		sender.sendMessage(format("Removed "+group+" from "+getCalculable().getName()));
	}
	
	public void setGroup(String group, CommandSource sender) {
		getCalculable().getGroupsAsString().clear();
		getCalculable().addGroup(group);
		sender.sendMessage(format("Set "+getCalculable().getName()+"'s group to "+group));
	}
	
	public void listGroups(CommandSource sender) {
		List<String> groups = getCalculable().serialiseGroups();
		String[] gr = groups.toArray(new String[groups.size()]);
		String mgr = Arrays.toString(gr);
		sender.sendMessage(format("The "+getCalculable().getType().getName()+" "+getCalculable().getName()+" has these groups:"));
		sender.sendMessage(mgr);
	}
	
	public void addPermission(String permission, CommandSource sender) {
		Permission perm = Permission.loadFromString(permission);
		getCalculable().addPermission(perm.name(), perm.isTrue());
		sender.sendMessage(format("Added "+perm.toString()+" to "+getCalculable().getName()));
	}
	
	public void removePermission(String permission, CommandSource sender) {
		getCalculable().removePermission(permission);
		sender.sendMessage(format("Removed "+permission+" from "+getCalculable().getName()));
	}
	
	public void listPermissions(CommandSource sender) {
		List<String> permissions = getCalculable().serialisePermissions();
		String[] pr = permissions.toArray(new String[permissions.size()]);
		String mpr = Arrays.toString(pr);
		sender.sendMessage(format("The "+getCalculable().getType().getName()+" "+getCalculable().getName()+" has these permissions:"));
		sender.sendMessage(mpr);
	}
	
	public void hasPermission(String node, CommandSource sender) {
		Calculable c = getCalculable();
		if(c instanceof User) {
		User user = (User) c;
		sender.sendMessage(format(user.getName() + " - " + node+ ": " +user.hasPermission(node)));
		} else if(c instanceof Group) {
			Group group = (Group) c;
			sender.sendMessage(format(group.getName() + " - " + node+ ": " +group.hasPermission(node)));
		}
	}
	
	public void setValue(String key, String value, CommandSource sender) {
		getCalculable().setValue(key, value);
		sender.sendMessage(format(key + " set to " + value + " for " + getCalculable().getName()));
	}
	
	public void showValue(String key, CommandSource sender) {
		String value = getCalculable().getValue(key);
		sender.sendMessage(format("Meta for "+calc.getName()+" "+getCalculable().getName()+" - "+key+": "+value));
	}
	
	/**
	 * Remind the user to save when changes are finished!
	 */
	public void save() {
		// Now saves everything
		WorldManager.getInstance().saveAll();
	}

}