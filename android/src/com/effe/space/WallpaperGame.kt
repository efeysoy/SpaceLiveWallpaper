package com.effe.space

import android.content.SharedPreferences
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
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

    lateinit var font: BitmapFont

    override fun onSharedPreferenceChanged(p0: SharedPreferences?, p1: String?) {
        shineProb = p0!!.getInt("rate", 100) / 100000f
        starSize = p0.getInt("size", 10) / 10f
        starCount = p0.getInt("amount", 4000)

        stars.clear()

        for (i in 1..starCount){
            stars.add(Star(Vector2(rand.nextFloat() * Gdx.graphics.width * 2,
                    rand.nextFloat() * Gdx.graphics.height * 2)
                    , rand.nextFloat() * starSize, batch, img,
                    Gdx.graphics.width, Gdx.graphics.height))
        }
    }

    override fun create() {
        batch = SpriteBatch()
        img = Texture("star.png")
        stars = ArrayList<Star>()

        shineProb = sp.getInt("rate", 100) / 100000f
        starSize = sp.getInt("size", 10) / 10f
        starCount = sp.getInt("amount", 4000)

        for (i in 1..starCount){
            stars.add(Star(Vector2(rand.nextFloat() * Gdx.graphics.width * 2,
                    rand.nextFloat() * Gdx.graphics.height * 2)
                    , rand.nextFloat() * starSize, batch, img,
                    Gdx.graphics.width, Gdx.graphics.height))
        }

        batch.maxSpritesInBatch = starCount

        Gdx.input.getRotationMatrix(mat)

        pitch =mat[9]
        roll = mat[8]
        azim = mat[10]

        font = BitmapFont()
    }

    override fun render() {
        Gdx.input.getRotationMatrix(mat)

        pitch +=(mat[9] - pitch)/ 100
        roll += (mat[8] - roll) / 100
        azim += (mat[4] - azim) / 100

        var r = (1-Math.abs(pitch)) * roll + (Math.abs(pitch) * azim)

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
//        var str = ""
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
