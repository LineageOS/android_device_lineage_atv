#
# Copyright (C) 2019 The LineageOS Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# System properties
include $(LOCAL_PATH)/system_prop.mk

# Overlays
PRODUCT_PACKAGE_OVERLAYS += \
    device/lineage/atv/overlay

PRODUCT_PACKAGES += \
    init.lineage.atv.adb.rc

# Force 1080p on ATV interface (opt-out)
ifneq ($(TARGET_ATV_RES_FORCE),false)
PRODUCT_PACKAGES += \
    init.lineage.atv.resolution.rc
endif

# Sabrina remote
PRODUCT_PACKAGES += \
    Vendor_18d1_Product_9450.idc \
    Vendor_18d1_Product_9450.kl

# Onn remote
PRODUCT_PACKAGES += \
    Vendor_0957_Product_0005.idc \
    Vendor_0957_Product_0005.kl

# Sabrina remote
PRODUCT_PACKAGES += \
    Vendor_18d1_Product_9450.idc \
    Vendor_18d1_Product_9450.kl

# IDCs for shield controllers
PRODUCT_PACKAGES += \
    Vendor_0955_Product_7212.idc \
    Vendor_0955_Product_7213.idc \
    Vendor_0955_Product_7214.idc \
    Vendor_0955_Product_7217.idc

# Key layouts for shield controllers
PRODUCT_PACKAGES += \
    Vendor_0955_Product_7212.kl \
    Vendor_0955_Product_7213.kl \
    Vendor_0955_Product_7214.kl \
    Vendor_0955_Product_7217.kl

# Key layouts for steam controller
PRODUCT_PACKAGES += \
    Vendor_28de_Product_1102.kl \
    Vendor_28de_Product_1142.kl

# Overlays
PRODUCT_ENFORCE_RRO_EXCLUDED_OVERLAYS += \
    device/google/atv/overlay

# Settings
PRODUCT_PACKAGES += \
    TvSettingsTwoPanel

# TVLauncher
PRODUCT_PACKAGES += \
    TVLauncherNoGMS \
    TVRecommendationsNoGMS

# priv-app permissions
PRODUCT_COPY_FILES +=\
    device/lineage/atv/permissions/privapp-permissions-lineage-atv.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/permissions/privapp-permissions-lineage-atv.xml

# Soong namespaces
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH)
