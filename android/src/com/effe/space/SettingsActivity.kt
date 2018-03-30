package com.effe.space

import android.app.WallpaperManager
import android.support.v7.app.AppCompatActivity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.service.wallpaper.WallpaperService
import android.widget.Button
import android.widget.SeekBar


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val prefs = getSharedPreferences("com.effe.space", 0)
        val editor = prefs.edit()

        var btnPreview = findViewById<Button>(R.id.btnPreview)

        btnPreview.setOnClickListener {
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    ComponentName(baseContext, "com.effe.space.WallpaperService"))
            startActivityForResult(intent, 0)
        }

        val seekRate = findViewById<SeekBar>(R.id.seekRate)
        val seekAmount = findViewById<SeekBar>(R.id.seekStarAmount)
        val seekSize = findViewById<SeekBar>(R.id.seekSize)

        seekRate.progress = prefs.getInt("rate", 1000)

        seekRate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                editor.putInt("rate", progress + 100)
                editor.commit()
            }
        })

        seekAmount.progress = prefs.getInt("amount", 4000)

        seekAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                editor.putInt("amount", progress + 100)
                editor.commit()
            }
        })


        seekSize.progress = prefs.getInt("size", 10)

        seekSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                editor.putInt("size", progress + 1)
                editor.commit()
            }
        })
    }

}

