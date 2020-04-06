package com.gdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import java.io.File

class Map {
    private var dirt = Texture("earth/dirt5.png")
    private var dirt2 = Texture("earth/dirt6.png")
    private var dirt3 = Texture("earth/dirt7.png")
    private var wall = Texture("earth/wall.png")
    private var stone1 = Texture("earth/stone-1.png")
    private var stone2 = Texture("earth/stone-3.png")

    private val CELLS_X = 26
    private val CELLS_Y = 18
    val data = MutableList (CELLS_X) { MutableList (CELLS_Y) { '0' }}

    fun isReachable(position: Vector2) = if (position.x <= 20 || position.x >= 780 || position.y <= 20 || position.y >= 520) false
        else (data[((position.x) / 32).toInt()][(position.y / 32).toInt()] == '0' || data[((position.x) / 32).toInt()][(position.y / 32).toInt()] == '4' || data[((position.x) / 32).toInt()][(position.y / 32).toInt()] == '5')

    fun createFromFile(mapFile: File) {
        val dataSource = mapFile.readLines()

        for (i in data.indices)
            for (j in data[i].indices) {
                data[i][CELLS_Y - 1 - j] = dataSource[j][i]
            }
    }

    fun render(batch: SpriteBatch) {
        for (i in data.size - 1 downTo 0)
            for (j in data[i].size downTo 0) {
                batch.draw(dirt, i * 32F, j * 32F)
            }

        for (i in data.size - 1 downTo 0)
            for (j in data[i].size - 1 downTo 0) {
                when (data[i][j]) {
                '1' -> batch.draw(wall, i * 32F, j * 32F)
                '2' -> batch.draw(stone1, i * 32F, j * 32F)
                '3' -> batch.draw(stone2, i * 32F, j * 32F)
                '4' -> batch.draw(dirt2, i * 32F, j * 32F)
                '5' -> batch.draw(dirt3, i * 32F, j * 32F)
                }
            }
    }

    fun changeTextures(mapNumber: Int) {
        if (mapNumber == 2) {
            dirt = Texture("earth/earth2.png")
            dirt3 = Texture("earth/earth2-spike.png")
            dirt2 = Texture("earth/earth2.png")
            wall = Texture("earth/wall2.png")
            stone1 = Texture("earth/stone-2.png")
            stone2 = Texture("earth/tree-2.png")
        }
    }
}