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

# Init files
PRODUCT_PACKAGES += \
    init.lineage.atv.rc

# IDCs for shield controllers
PRODUCT_PACKAGES += \
    Vendor_0955_Product_7212.idc \
    Vendor_0955_Product_7213.idc \
    Vendor_0955_Product_7214.idc

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

ifneq ($(WITH_GMS),true)
# LeanbackLauncher
PRODUCT_PACKAGES += \
    TVLauncherNoGMS \
    TVRecommendationsNoGMS
endif

# priv-app permissions
PRODUCT_COPY_FILES +=\
    device/lineage/atv/permissions/privapp-permissions-lineage-atv.xml:$(TARGET_COPY_OUT_SYSTEM)/etc/permissions/privapp-permissions-lineage-atv.xml
