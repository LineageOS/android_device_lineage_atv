/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */
package org.lineageos.atvpackagedisabler

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

object PackageDisabler {
    private const val TAG = "PackageDisabler"
    private const val PREFS_NAME = "PackageDisablerPrefs"
    private const val KEY_DONE = "first_boot_done"

    private val PACKAGES_TO_DISABLE = listOf(
        "com.android.documentsui",
    )

    fun disablePackages(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.getBoolean(KEY_DONE, false)) {
            Log.d(TAG, "Not first boot, skipping")
            return
        }

        PACKAGES_TO_DISABLE.forEach { disable(context, it) }

        prefs.edit().putBoolean(KEY_DONE, true).apply()
        Log.i(TAG, "First-boot disable complete")
    }

    private fun disable(context: Context, packageName: String) {
        try {
            context.packageManager.setApplicationEnabledSetting(
                packageName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                0
            )
            Log.i(TAG, "Disabled: $packageName")
        } catch (e: IllegalArgumentException) {
            Log.w(TAG, "Package not found, skipping: $packageName")
        } catch (e: SecurityException) {
            Log.e(TAG, "SecurityException disabling $packageName", e)
        }
    }
}

