package me.pronil.orbitalpunishment.utils;

public enum Permissions {

    WARN("orbitalpunishment.warn"),
    KICK("orbitalpunishment.kick"),
    MUTE("orbitalpunishment.mute"),
    UNMUTE("orbitalpunishment.unmute"),
    TEMPBAN("orbitalpunishment.tempban"),
    BAN("orbitalpunishment.ban"),
    UNBAN("orbitalpunishment.unban"),
    GET_PUNISHMENTS("orbitalpunishment.getpunishments");

    public final String permission;

    Permissions(String perm) {
        this.permission = perm;
    }

    public String get() {
        return this.permission;
    }
}
