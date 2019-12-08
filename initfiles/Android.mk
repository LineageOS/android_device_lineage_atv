LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE               := init.lineage.atv.rc
LOCAL_MODULE_CLASS         := ETC
LOCAL_SRC_FILES            := init.lineage.atv.rc
LOCAL_MODULE_RELATIVE_PATH := init
include $(BUILD_PREBUILT)
