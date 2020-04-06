package com.gdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2

class SpikeEmitter (private val gameScreen: GameScreen) {
    val spikes = List(50) { Spike(gameScreen) }
    private val regions = TextureRegion(Texture("all/loot/items.png")).split(33, 28)[0]

    fun render(batch: SpriteBatch) {
        spikes.forEach {
            it.render(batch)
        }
    }


     fun setup(map: Map) {
         for (i in map.data.indices)
             for (j in map.data[i].indices){
                 val it = map.data[i][j]
                 if (it == '5') {
                     for (spike in spikes) {
                         if (!spike.ready) {
                             spike.setup(Vector2((i * 32).toFloat(), (j * 32).toFloat()))
                             break
                         }
                     }
                 }
             }
     }

    fun clear() {
        spikes.forEach {
            it.deactivate()
        }
    }

    fun update(deltaTime: Float, map: Map) {
        spikes.forEach {
            it.update(deltaTime, map)
        }
    }
}