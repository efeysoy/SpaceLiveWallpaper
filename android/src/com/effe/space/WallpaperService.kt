package com.effe.space

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService

/**
 * Created by efeyucesoy on 3/23/18.
 */
class WallpaperService : AndroidLiveWallpaperService() {
    override fun onCreateApplication() {
        super.onCreateApplication()

        val config = AndroidApplicationConfiguration()

        config.numSamples = 1

        initialize(WallpaperGame(getSharedPreferences("com.effe.space", 0)), config)

    }
}