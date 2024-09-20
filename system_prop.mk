# Enable Bluetooth Remote Pairing in Setupwraith
PRODUCT_PROPERTY_OVERRIDES += \
    atv.setup.bt_remote_pairing=true

# Logging
ifneq ($(TARGET_BUILD_VARIANT),eng)
PRODUCT_SYSTEM_DEFAULT_PROPERTIES += \
    log.tag.cast_ClientAuthCredsWidevine=S \
    log.tag.cast_shell=S \
    log.tag.cast_VolumeControlAndroid=S \
    log.tag.katniss_interactor_CastIdFetcher=S \
    log.tag.katniss_interactor_CastServiceInfoImpl=S \
    log.tag.katniss_interactor_LoadCastServiceInfoTaskImpl=S \
    log.tag.katniss_search_LoadCastServiceInfoTaskImpl=S
endif

