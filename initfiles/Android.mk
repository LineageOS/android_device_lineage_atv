LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE               := init.lineage.atv.rc
LOCAL_MODULE_CLASS         := ETC
ifneq ($(TARGET_ATV_FORCE_1080_SCALING),)
LOCAL_SRC_FILES            := init.lineage.atv.override.rc
else
LOCAL_SRC_FILES            := init.lineage.atv.rc
endif
LOCAL_MODULE_RELATIVE_PATH := init
LOCAL_PRODUCT_MODULE       := true
include $(BUILD_PREBUILT)
