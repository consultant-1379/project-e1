package com.team2.microservicemanagementtool.models;

import java.util.Objects;

public class BasicSemanticVersioning {
    private int major;
    private int minor;
    private int patch;

    public BasicSemanticVersioning(int major, int minor, int patch)
    {
        if(major < 0 || minor < 0 || patch < 0)
        {
            throw new IllegalArgumentException("Version numbers cannot be negative");
        }
        this.minor=minor;
        this.major=major;
        this.patch=patch;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setPatch(int patch) {
        this.patch = patch;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public String toString() {
        return "" + major+"."+minor+"."+patch;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || o.getClass() != this.getClass())
            return false;

        BasicSemanticVersioning basicOther = (BasicSemanticVersioning) o;
        return (basicOther.getMajor() == this.getMajor() && basicOther.getMinor() == this.getMinor() && basicOther.getPatch() == this.getPatch());
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }
}
