package dev._2lstudios.jelly.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import dev._2lstudios.jelly.math.Cuboid;
import dev._2lstudios.jelly.math.Vector3;
import dev._2lstudios.jelly.utils.WorldUtils;

public class Configuration extends YamlConfiguration {

    private File file;

    public Configuration(File file) {
        this.file = file;
    }

    /* Utils methods */
    public void load() throws FileNotFoundException, IOException, InvalidConfigurationException {
        this.load(this.file);
    }

    public void save() throws IOException {
        this.save(this.file);
    }

    public void safeSave() {
        try {
            this.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIfNotExist(String path, Object value) {
        if (!this.contains(path) && path != null && !path.isEmpty()) {
            this.set(path, value);
            this.safeSave();
        }
    }

    /* Primitive object get and set */
    @Override
    public int getInt(String path, int defaultValue) {
        this.setIfNotExist(path, defaultValue);
        return super.getInt(path, defaultValue);
    }

    @Override
    public String getString(String path, String defaultValue) {
        this.setIfNotExist(path, defaultValue);
        return super.getString(path, defaultValue);
    }

    @Override
    public boolean getBoolean(String path, boolean defaultValue) {
        this.setIfNotExist(path, defaultValue);
        return super.getBoolean(path, defaultValue);
    }

    public List<String> getStringList(String path, List<String> defaultValue) {
        this.setIfNotExist(path, defaultValue);
        return super.getStringList(path);
    }

    /* Custom object get and set */
    private Sound getSound(String key) {
        String name = this.getString(key);

        for (Sound sound : Sound.values()) {
            if (name.equals(sound.name())) {
                return sound;
            }
        }

        Bukkit.getLogger().warning("Couldn't load sound '" + name + "' from configuration file! (Invalid name?)");
        return null;
    }

    public Sound getSound(String key, String defaultValue) {
        this.setIfNotExist(key, defaultValue);
        return this.getSound(key);
    }

    public Sound getSound(String key, Sound defaultValue) {
        return this.getSound(key, defaultValue.toString());
    }

    public Vector3 getVector3(String key) {
        double x = this.getDouble(key + ".x", Integer.MIN_VALUE);
        double y = this.getDouble(key + ".y", Integer.MIN_VALUE);
        double z = this.getDouble(key + ".z", Integer.MIN_VALUE);

        if (x == Integer.MIN_VALUE || y == Integer.MIN_VALUE || z == Integer.MIN_VALUE) {
            return null;
        }

        return new Vector3(x, y, z);
    }

    public Cuboid getCuboid(String key) {
        Vector3 firstPoint = this.getVector3(key + ".first_point");
        Vector3 secondPoint = this.getVector3(key + ".second_point");

        if (firstPoint == null || secondPoint == null) {
            return null;
        }

        return new Cuboid(firstPoint, secondPoint);
    }

    public Location getLocation(String key, boolean getWorld) {
        World world = getWorld ? WorldUtils.getWorldSafe(this.getString(key + ".world")) : null;
        double x = this.getDouble(key + ".x");
        double y = this.getDouble(key + ".y");
        double z = this.getDouble(key + ".z");
        float pitch = (float) this.getDouble(key + ".pitch");
        float yaw = (float) this.getDouble(key + ".yaw");

        return new Location(world, x, y, z, yaw, pitch);
    }

    public Location getLocation(String key) {
        return this.getLocation(key, true);
    }

    public void setVector3(String key, Vector3 vector) {
        this.set(key + ".x", vector.getX());
        this.set(key + ".y", vector.getY());
        this.set(key + ".z", vector.getZ());
    }

    public void setCuboid(String key, Cuboid cuboid) {
        this.setVector3(key + ".first_point", cuboid.getFirstPoint());
        this.setVector3(key + ".second_point", cuboid.getSecondPoint());
    }

    public void setLocation(String key, Location location, boolean includeWorld) {
        if (includeWorld) {
            this.set(key + ".world", location.getWorld().getName());
        }

        this.set(key + ".x", location.getX());
        this.set(key + ".y", location.getY());
        this.set(key + ".z", location.getZ());
        this.set(key + ".pitch", location.getPitch());
        this.set(key + ".yaw", location.getYaw());
    }

    public void setLocation(String key, Location location) {
        this.setLocation(key, location, true);
    }
}
