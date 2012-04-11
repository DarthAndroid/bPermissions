package de.bananaco.bpermissions.spout;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.ConfigurationNode;
/**
 * This preparses the yaml and fixes common errors before loading the file
 */
public class YamlConfiguration {

	private Configuration config = null;
	
	public YamlConfiguration() {

	}
	
	public YamlConfiguration(File file) {
		config = new org.spout.api.util.config.yaml.YamlConfiguration(file);
	}

	public Object get(String string, Object defaultValue) {
		Object value = config.getNode(string).getValue();
		if(value == null)
			return defaultValue;
		return value;
	}

	public void set(String string, Object value) {
		config.getNode(string).setValue(value);
	}
	
	public void load(File file) {
		config = new org.spout.api.util.config.yaml.YamlConfiguration(file);
		try {
			config.load();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void save(File file) throws Exception {
		if(!file.exists()) {
			file.mkdirs();
			file.createNewFile();
		}
		config.save();
	}

	public String getString(String string, String string2) {
		return config.getNode(string).getString(string2);
	}

	public ConfigurationNode getNode(String path) {
		return config.getNode(path, null);
	}
	
	public Set<String> getKeys(String path) {
		return config.getNode(path).getKeys(false);
	}

	public List<String> getStringList(String string) {
		return config.getNode(string).getStringList();
	}

	public Object get(String key) {
		return config.getNode(key).getValue();
	}

	public String getString(String key) {
		return config.getNode(key).getString();
	}

	public boolean getBoolean(String string, boolean def) {
		return config.getNode(string).getBoolean(def);
	}

	public boolean getBoolean(String string) {
		return config.getNode(string).getBoolean();
	}
}
