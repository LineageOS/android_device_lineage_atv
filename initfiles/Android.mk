LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
<<<<<<< HEAD   (c9eca8 TVLauncherNoGMS: Allow launcher to run with gms installed)
LOCAL_MODULE               := init.lineage.atv.rc
=======
LOCAL_MODULE               := init.lineage.atv.adb.rc
LOCAL_MODULE_CLASS         := ETC
LOCAL_SRC_FILES            := init.lineage.atv.adb.rc
LOCAL_MODULE_RELATIVE_PATH := init
include $(BUILD_PREBUILT)

include $(CLEAR_VARS)
LOCAL_MODULE               := init.lineage.atv.resolution.rc
>>>>>>> CHANGE (a0b130 Support restarting adb when adb port prop is set)
LOCAL_MODULE_CLASS         := ETC
LOCAL_SRC_FILES            := init.lineage.atv.rc
LOCAL_MODULE_RELATIVE_PATH := init
include $(BUILD_PREBUILT)
