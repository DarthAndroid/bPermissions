package de.bananaco.bpermissions.unit;

import java.util.Arrays;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.Group;
import de.bananaco.bpermissions.api.User;
import de.bananaco.bpermissions.api.World;
import de.bananaco.bpermissions.api.util.Calculable;
import de.bananaco.bpermissions.api.util.CalculableType;

public class CalculableTest {

	private final World world;

	public CalculableTest(World world) {
		this.world = world;
	}
	
	public void printLine() {
		System.out.println("#################################################");
	}

	public void testPriority() {
		printLine();
		// Create the groups
		Calculable group0 = new Group("default", world);
		Calculable group1 = new Group("moderator", world);
		Calculable group2 = new Group("admin", world);
		// Define the priority
		group0.setValue("priority", "0");
		group1.setValue("priority", "5");
		group2.setValue("priority", "20");
		// Define the test0
		group0.setValue("test0", group0.getName());
		group1.setValue("test0", group1.getName());
		group2.setValue("test0", group2.getName());
		// Define the test1
		group0.setValue("test1", group0.getName());
		group1.setValue("test1", group1.getName());
		// Define the test2
		group0.setValue("test2", group0.getName());
		// Add to the system
		world.add(group0);
		world.add(group1);
		world.add(group2);
		// Create the user
		Calculable user = new User("test", world);
		// Add the groups
		user.addGroup(group0.getName());
		user.addGroup(group1.getName());
		user.addGroup(group2.getName());
		// Add to the system
		world.add(user);
		// Calculate the meta
		try {
			user.calculateEffectiveMeta();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Test the test0
		String test0 = user.getEffectiveValue("test0");
		String test1 = user.getEffectiveValue("test1");
		String test2 = user.getEffectiveValue("test2");

		// Print the results
		System.out.println("default, moderator, admin assigned to User 'user'");
		System.out.println("'user' has groups:");
		System.out.println(Arrays.toString(ApiLayer.getGroups(world.getName(), CalculableType.USER, user.getName())));

		System.out.println("default priority: "+group0.getPriority());
		System.out.println("moderator priority: "+group1.getPriority());
		System.out.println("admin priority: "+group2.getPriority());

		System.out.println("test0 expected admin got "+test0);
		System.out.println("test1 expected moderator got "+test1);
		System.out.println("test2 expected default got "+test2);
	}

	public void testPermissions() {
		printLine();
		// Create the groups
		Calculable group0 = new Group("default", world);
		Calculable group1 = new Group("moderator", world);
		Calculable group2 = new Group("admin", world);
		// A non-building group to add
		Calculable group3 = new Group("non-builder", world);
		group3.setValue("priority", "100");
		// add the permissions
		group0.addPermission("permission.build", false);
		group1.addPermission("permission.build", true);
		
		group3.addPermission("permission.build", false);
		// Add to the system
		world.add(group0);
		world.add(group1);
		world.add(group2);
		world.add(group3);
		// Create the user
		Calculable user = new User("test", world);
		// Add the groups
		group1.addGroup(group0.getName());
		group2.addGroup(group1.getName());
		user.addGroup(group2.getName());
		// Add to the system
		world.add(user);
		// Calculate the permissions
		try {
		user.calculateEffectivePermissions();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("default: permission.build:false");
		System.out.println("moderator: permission.build: true");
		System.out.println("admin: unset");
		System.out.println("non-builder: permission.build: false priority: 100");
		System.out.println("admin -> moderator -> default");
		System.out.println("user has group: admin");
		System.out.println("expected: true - user has permission.build:"+user.hasPermission("permission.build"));
		System.out.println("addgroup: non-builder");
		user.addGroup(group3.getName());
		System.out.println("expected: false - user has permission.build:"+user.hasPermission("permission.build"));
		System.out.println("remove permission.build from group: non-builder");
		group3.removePermission("permission.build");
		System.out.println("expected: true - user has permission.build:"+user.hasPermission("permission.build"));
	}

}