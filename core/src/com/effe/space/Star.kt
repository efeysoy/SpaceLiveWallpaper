package com.effe.space

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.Texture
import java.util.*

/**
 * Created by efeyucesoy on 3/29/18.
 */

class Star(point: Vector2, size:Float, batch:SpriteBatch, texture:Texture, width:Int, height:Int){
    val p = point
    val size = size/50
    val bt = batch
    val tx = texture
    val width = width
    val height = height

    companion object {
        val rand = Random()
    }
    val color:Color = Color()


    init {
        color.r = rand.nextFloat() * 0.2f + 0.8f
        color.g = rand.nextFloat() * 0.2f + 0.8f
        color.b = rand.nextFloat() * 0.2f + 0.8f
        color.a = 1f
    }

    var sizeMult = 1f
    var shining = 0

    fun draw(delta:Float, shine:Boolean, offsetX:Int, offsetY:Int){
        val dt = delta

        if (shining != 0){
            sizeMult += shining * delta * 5
            if (sizeMult >= 4)
                shining = -1
            if (sizeMult <= 1)
                shining = 0
        }
        else
            if(shine)
                shining = 1

        val x = (p.x + offsetX) % (width * 2)
        val y = (p.y + offsetY) % (height * 2)
//
//        if (x < 0 && x > width)
//            return
//
//        if (y < 0 && y > height)
//            return
        bt.color = color
        bt.draw(tx,
                x - tx.width / 2,y - tx.width / 2,
                tx.width/2f,tx.height/2f,
                tx.width.toFloat(), tx.height.toFloat(),
                size * sizeMult, size * sizeMult,
                0f,
                0,0,
                tx.width,tx.height,false,false)

    }
}