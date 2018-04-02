package com.effe.space

import android.app.WallpaperManager
import android.support.v7.app.AppCompatActivity
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_settings.*
import com.pes.androidmaterialcolorpickerdialog.ColorPicker


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val prefs = getSharedPreferences("com.effe.space", 0)
        val editor = prefs.edit()
        val cp = ColorPicker(this)

        var btnPreview = findViewById<Button>(R.id.btnPreview)

        btnPreview.setOnClickListener {
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    ComponentName(baseContext, "com.effe.space.WallpaperService"))
            startActivityForResult(intent, 0)
        }

//        val seekRate = findViewById<SeekBar>(R.id.seekRate)
//        val seekAmount = findViewById<SeekBar>(R.id.seekStarAmount)
//        val seekSize = findViewById<SeekBar>(R.id.seekSize)

        checkColor.isChecked = prefs.getBoolean("colored", false)

        checkColor.setOnClickListener {
            editor.putBoolean("colored", checkColor.isChecked)
            editor.commit()
        }

        cp.color = Color.rgb(prefs.getInt("red", 255),
                prefs.getInt("green", 0),
                prefs.getInt("blue", 0))


        btnColor.setOnClickListener {
            cp.show()
            cp.setCallback {
                btnColor.background.setColorFilter(it, PorterDuff.Mode.MULTIPLY)
                editor.putInt("red", Color.red(it))
                editor.putInt("green", Color.green(it))
                editor.putInt("blue", Color.blue(it))
                editor.commit()
                cp.hide()
            }
        }

        btnColor.background.setColorFilter(cp.color, PorterDuff.Mode.MULTIPLY)

        seekRate.progress = prefs.getInt("rate", 1000)

        seekRate.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                editor.putInt("rate", progress + 100)
                editor.commit()
            }
        })

        seekStarAmount.progress = prefs.getInt("amount", 4000)

        seekStarAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

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

