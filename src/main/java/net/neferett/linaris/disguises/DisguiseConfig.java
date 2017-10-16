package net.neferett.linaris.disguises;

import net.neferett.linaris.disguises.utilities.PacketsManager;

public class DisguiseConfig {

    private static boolean animationEnabled = true;
    private static boolean bedEnabled = true;
    private static boolean blowDisguisesOnAttack = true;
    private static boolean collectEnabled = true;
    private static boolean colorizeSheep = true;
    private static boolean colorizeWolf = true;
    private static String disguiseBlownMessage = "nothing";
    private static int disguiseCloneExpire;
    private static int disguiseEntityExpire;
    private static boolean entityAnimationsAdded = true;
    private static boolean entityStatusEnabled = true;
    private static boolean equipmentEnabled = false;
    private static boolean hearSelfDisguise = true;
    private static boolean viewSelfDisguise = true;
    private static boolean hidingArmor = true;
    private static boolean hidingHeldItem = false;
    private static boolean keepDisguiseEntityDespawn = false;
    private static boolean keepDisguisePlayerDeath = false;
    private static boolean keepDisguisePlayerLogout = false;
    private static int maxClonedDisguises;
    private static boolean maxHealthIsDisguisedEntity = false;
    private static boolean miscDisguisesForLivingEnabled = false;
    private static boolean modifyBoundingBox = true;
    private static boolean movementEnabled = true;
    private static boolean sendsEntityMetadata = true;
    private static boolean sendVelocity = true;
    private static boolean showNameAboveHead = true;
    private static boolean showNameAboveHeadAlwaysVisible = true;
    private static boolean targetDisguises = true;
    private static boolean undisguiseSwitchWorlds = false;
    private static String updateNotificationPermission;
    private static boolean witherSkullEnabled = true;

    public static String getDisguiseBlownMessage() {
        return disguiseBlownMessage;
    }

    public static int getDisguiseCloneExpire() {
        return disguiseCloneExpire;
    }

    public static int getDisguiseEntityExpire() {
        return disguiseEntityExpire;
    }

    public static int getMaxClonedDisguises() {
        return maxClonedDisguises;
    }

    public static String getUpdateNotificationPermission() {
        return updateNotificationPermission;
    }

    public static boolean isAnimationPacketsEnabled() {
        return animationEnabled;
    }

    public static boolean isBedPacketsEnabled() {
        return bedEnabled;
    }

    public static boolean isCollectPacketsEnabled() {
        return collectEnabled;
    }

    public static boolean isDisguiseBlownOnAttack() {
        return blowDisguisesOnAttack;
    }

    /**
     * @deprecated Spelling mistake.
     */
    @Deprecated
    public static boolean isEnquipmentPacketsEnabled() {
        return equipmentEnabled;
    }

    public static boolean isEntityAnimationsAdded() {
        return entityAnimationsAdded;
    }

    public static boolean isEntityStatusPacketsEnabled() {
        return entityStatusEnabled;
    }

    public static boolean isEquipmentPacketsEnabled() {
        return equipmentEnabled;
    }

    /**
     * Is the plugin modifying the inventory packets so that players when self disguised, do not see their armor floating around
     */
    public static boolean isHidingArmorFromSelf() {
        return hidingArmor;
    }

    /**
     * Does the plugin appear to remove the item they are holding, to prevent a floating sword when they are viewing self disguise
     */
    public static boolean isHidingHeldItemFromSelf() {
        return hidingHeldItem;
    }

    public static boolean isKeepDisguiseOnEntityDespawn() {
        return keepDisguiseEntityDespawn;
    }

    public static boolean isKeepDisguiseOnPlayerDeath() {
        return keepDisguisePlayerDeath;
    }

    public static boolean isKeepDisguiseOnPlayerLogout() {
        return keepDisguisePlayerLogout;
    }

    public static boolean isMaxHealthDeterminedByDisguisedEntity() {
        return maxHealthIsDisguisedEntity;
    }

    public static boolean isMetadataPacketsEnabled() {
        return sendsEntityMetadata;
    }

    public static boolean isMiscDisguisesForLivingEnabled() {
        return miscDisguisesForLivingEnabled;
    }

    public static boolean isModifyBoundingBox() {
        return modifyBoundingBox;
    }

    public static boolean isMonstersIgnoreDisguises() {
        return targetDisguises;
    }

    public static boolean isMovementPacketsEnabled() {
        return movementEnabled;
    }

    public static boolean isNameAboveHeadAlwaysVisible() {
        return showNameAboveHeadAlwaysVisible;
    }

    public static boolean isNameOfPlayerShownAboveDisguise() {
        return showNameAboveHead;
    }

    public static boolean isSelfDisguisesSoundsReplaced() {
        return hearSelfDisguise;
    }

    public static boolean isSheepDyeable() {
        return colorizeSheep;
    }

    /**
     * Is the sound packets caught and modified
     */
    public static boolean isSoundEnabled() {
        return PacketsManager.isHearDisguisesEnabled();
    }

    public static boolean isUndisguiseOnWorldChange() {
        return undisguiseSwitchWorlds;
    }

    /**
     * Is the velocity packets sent
     *
     * @return
     */
    public static boolean isVelocitySent() {
        return sendVelocity;
    }

    /**
     * The default value if a player views his own disguise
     *
     * @return
     */
    public static boolean isViewDisguises() {
        return viewSelfDisguise;
    }

    public static boolean isWitherSkullPacketsEnabled() {
        return witherSkullEnabled;
    }

    public static boolean isWolfDyeable() {
        return colorizeWolf;
    }

    public static void setAddEntityAnimations(boolean isEntityAnimationsAdded) {
        entityAnimationsAdded = isEntityAnimationsAdded;
    }

    public static void setAnimationPacketsEnabled(boolean enabled) {
        if (enabled != isAnimationPacketsEnabled()) {
            animationEnabled = enabled;
            PacketsManager.setupMainPacketsListener();
        }
    }

    public static void setBedPacketsEnabled(boolean enabled) {
        if (enabled != isBedPacketsEnabled()) {
            bedEnabled = enabled;
            PacketsManager.setupMainPacketsListener();
        }
    }

    public static void setCollectPacketsEnabled(boolean enabled) {
        if (enabled != isCollectPacketsEnabled()) {
            collectEnabled = enabled;
            PacketsManager.setupMainPacketsListener();
        }
    }

    public static void setDisguiseBlownMessage(String newMessage) {
        disguiseBlownMessage = newMessage;
    }

    public static void setDisguiseBlownOnAttack(boolean blowDisguise) {
        blowDisguisesOnAttack = blowDisguise;
    }

    public static void setDisguiseCloneExpire(int newExpires) {
        disguiseCloneExpire = newExpires;
    }

    public static void setDisguiseEntityExpire(int newExpires) {
        disguiseEntityExpire = newExpires;
    }

    @Deprecated
    public static void setEnquipmentPacketsEnabled(boolean enabled) {
        setEquipmentPacketsEnabled(enabled);
    }

    public static void setEntityStatusPacketsEnabled(boolean enabled) {
        if (enabled != isEntityStatusPacketsEnabled()) {
            entityStatusEnabled = enabled;
            PacketsManager.setupMainPacketsListener();
        }
    }

    public static void setEquipmentPacketsEnabled(boolean enabled) {
        if (enabled != isEquipmentPacketsEnabled()) {
            equipmentEnabled = enabled;
            PacketsManager.setupMainPacketsListener();
        }
    }

    /**
     * Can players hear their own disguises
     */
    public static void setHearSelfDisguise(boolean replaceSound) {
        if (hearSelfDisguise != replaceSound) {
            hearSelfDisguise = replaceSound;
        }
    }

    /**
     * Set the plugin to hide self disguises armor from theirselves
     */
    public static void setHideArmorFromSelf(boolean hideArmor) {
        if (hidingArmor != hideArmor) {
            hidingArmor = hideArmor;
            PacketsManager.setInventoryListenerEnabled(isHidingHeldItemFromSelf() || isHidingArmorFromSelf());
        }
    }

    /**
     * Does the plugin appear to remove the item they are holding, to prevent a floating sword when they are viewing self disguise
     */
    public static void setHideHeldItemFromSelf(boolean hideHelditem) {
        if (hidingHeldItem != hideHelditem) {
            hidingHeldItem = hideHelditem;
            PacketsManager.setInventoryListenerEnabled(isHidingHeldItemFromSelf() || isHidingArmorFromSelf());
        }
    }

    public static void setKeepDisguiseOnEntityDespawn(boolean keepDisguise) {
        keepDisguiseEntityDespawn = keepDisguise;
    }

    public static void setKeepDisguiseOnPlayerDeath(boolean keepDisguise) {
        keepDisguisePlayerDeath = keepDisguise;
    }

    public static void setKeepDisguiseOnPlayerLogout(boolean keepDisguise) {
        keepDisguisePlayerLogout = keepDisguise;
    }

    public static void setMaxClonedDisguises(int newMax) {
        maxClonedDisguises = newMax;
    }

    public static void setMaxHealthDeterminedByDisguisedEntity(boolean isDetermined) {
        maxHealthIsDisguisedEntity = isDetermined;
    }

    public static void setMetadataPacketsEnabled(boolean enabled) {
        sendsEntityMetadata = enabled;
    }

    public static void setMiscDisguisesForLivingEnabled(boolean enabled) {
        if (enabled != isMiscDisguisesForLivingEnabled()) {
            miscDisguisesForLivingEnabled = enabled;
            PacketsManager.setupMainPacketsListener();
        }
    }

    public static void setModifyBoundingBox(boolean modifyBounding) {
        modifyBoundingBox = modifyBounding;
    }

    public static void setMonstersIgnoreDisguises(boolean ignore) {
        targetDisguises = ignore;
    }

    public static void setMovementPacketsEnabled(boolean enabled) {
        if (enabled != isMovementPacketsEnabled()) {
            movementEnabled = enabled;
            PacketsManager.setupMainPacketsListener();
        }
    }

    public static void setNameAboveHeadAlwaysVisible(boolean alwaysVisible) {
        showNameAboveHeadAlwaysVisible = alwaysVisible;
    }

    public static void setNameOfPlayerShownAboveDisguise(boolean showNames) {
        showNameAboveHead = showNames;
    }

    public static void setSheepDyeable(boolean color) {
        colorizeSheep = color;
    }

    /**
     * Set if the disguises play sounds when hurt
     */
    public static void setSoundsEnabled(boolean isSoundsEnabled) {
        PacketsManager.setHearDisguisesListener(isSoundsEnabled);
    }

    public static void setUndisguiseOnWorldChange(boolean isUndisguise) {
        undisguiseSwitchWorlds = isUndisguise;
    }

    public static void setUpdateNotificationPermission(String newPermission) {
        updateNotificationPermission = newPermission;
    }

    /**
     * Disable velocity packets being sent for w/e reason. Maybe you want every ounce of performance you can get?
     *
     * @param sendVelocityPackets
     */
    public static void setVelocitySent(boolean sendVelocityPackets) {
        sendVelocity = sendVelocityPackets;
    }

    public static void setViewDisguises(boolean seeOwnDisguise) {
        viewSelfDisguise = seeOwnDisguise;
    }

    public static void setWitherSkullPacketsEnabled(boolean enabled) {
        witherSkullEnabled = enabled;
    }

    public static void setWolfDyeable(boolean color) {
        colorizeWolf = color;
    }

    private DisguiseConfig() {
    }
}
