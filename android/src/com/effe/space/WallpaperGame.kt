package com.effe.space

import android.content.SharedPreferences
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.android.AndroidLiveWallpaper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector2
import java.util.*

class WallpaperGame(sp: SharedPreferences) : Game(), SharedPreferences.OnSharedPreferenceChangeListener {
    internal lateinit var batch: SpriteBatch
    internal lateinit var img: Texture
    internal lateinit var stars:ArrayList<Star>
    var sp = sp

    val rand = Random()
    var starCount = 4000

    var pitch = 0f
    var roll = 0f
    var azim = 0f

    val mat = FloatArray(16)
    var shineProb = 0.01f
    var starSize = 1f
    val color = Color(Color.RED)
    var isColored = false

    lateinit var font: BitmapFont

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        shineProb = p0!!.getInt("rate", 100) / 100000f
        starSize = p0.getInt("size", 10) / 10f
        starCount = p0.getInt("amount", 4000)

        color.r = p0.getInt("red", 255) / 255f
        color.g = p0.getInt("green", 0) / 255f
        color.b = p0.getInt("blue", 0) / 255f

        isColored = sp.getBoolean("colored", false)

        stars.clear()

        for (i in 1..starCount){
            val star = Star(Vector2(rand.nextFloat() * Gdx.graphics.width * 2,
                    rand.nextFloat() * Gdx.graphics.height * 2)
                    , rand.nextFloat() * starSize, batch, img,
                    Gdx.graphics.width, Gdx.graphics.height)

            if (isColored)
                star.color = color

            stars.add(star)
        }
    }

    override fun create() {
        batch = SpriteBatch()
        img = Texture("star.png")
        stars = ArrayList<Star>()

        shineProb = sp.getInt("rate", 100) / 100000f
        starSize = sp.getInt("size", 10) / 10f
        starCount = sp.getInt("amount", 4000)

        color.r = sp.getInt("red", 255) / 255f
        color.g = sp.getInt("green", 0) / 255f
        color.b = sp.getInt("blue", 0) / 255f

        isColored = sp.getBoolean("colored", false)

        for (i in 1..starCount){
            val star = Star(Vector2(rand.nextFloat() * Gdx.graphics.width * 2,
                    rand.nextFloat() * Gdx.graphics.height * 2)
                    , rand.nextFloat() * starSize, batch, img,
                    Gdx.graphics.width, Gdx.graphics.height)

            if (isColored)
                star.color = color

            stars.add(star)

        }

        batch.maxSpritesInBatch = starCount

        Gdx.input.getRotationMatrix(mat)

        pitch += mat[9]
        roll  += mat[8]
        azim  += mat[6]
        font = BitmapFont()
    }

    var  r = 0f
    override fun render() {
        Gdx.input.getRotationMatrix(mat)

        val dAzim = (mat[6] - azim) / 100
        val dPitch= (mat[9] - pitch) / 100
        val dRoll = (mat[8] - roll) / 100



        pitch +=(mat[9] - pitch)/ 100
        roll  +=(mat[8] - roll )/ 100
        azim  +=(mat[6] - azim )/ 100

        r += ((1 - Math.abs(pitch)) * -dRoll + Math.abs(pitch) * dAzim)

        if (r > 1 || r < -1)
            r = 0f

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()

        for(star in stars){
            star.draw(Gdx.graphics.deltaTime, rand.nextFloat() < shineProb,
                         (r * Gdx.graphics.width).toInt(),
                         (pitch * Gdx.graphics.height).toInt())
        }

//        val mat = FloatArray(16)
//        Gdx.input.getRotationMatrix(mat)
//        var k = 1
//        for(i in mat)
//            font.draw(batch, k.toString() + " - " + Math.round(i* 10).toString(), 500f, 200f + (k++*100))


        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }

    override fun pause() {
        super.pause()
        sp.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun resume() {
        super.resume()
        sp.registerOnSharedPreferenceChangeListener(this)
    }
}
