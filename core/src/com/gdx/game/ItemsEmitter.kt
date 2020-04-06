package com.gdx.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class ItemsEmitter(val gameScreen: GameScreen) {
    val items = List(50) { Item() }
    private val itemRegions = TextureRegion(Texture("all/loot/items.png")).split(33, 28)[0]
    private val weaponRegions = listOf(Texture("all/weapons/11.png"),
            Texture("all/weapons/10-1.png"),
            Texture("all/weapons/8-1.png")
    )

    fun render(batch: SpriteBatch) {
        items.forEach {
            if (it.isActive()) {
                if (it.type.index < 5) batch.draw(itemRegions[it.type.index], it.position.x - 12, it.position.y - 8)
                else batch.draw(weaponRegions[it.type.index - 5], it.position.x - 12, it.position.y - 8)
            }
        }
    }

    fun generateRandItem(x: Float, y: Float, count: Int) {
        for (i in 1..count) {
            val type = when ((0..80).random()) {
                in 0..4 -> return
                in 5..20 -> Item.Type.COIN
                in 21..35 -> Item.Type.COIN_STACK
                in 36..40 -> Item.Type.SPEED_POTION
                in 41..55 -> Item.Type.HEAL_POTION
                in 56..65 -> Item.Type.DOUBLE_COIN_STACK
                in 66..75 -> Item.Type.MACHETE
                in 76..79 -> Item.Type.SWORD
                else -> Item.Type.ALEBARD
            }

            for (it in items) {
                if (!(it.isActive())) {
                    it.activate(x, y, type)
                    break
                }
            }
        }
    }

    fun generateItem(x: Float, y: Float, type: Item.Type) {
            for (it in items) {
                if (!(it.isActive())) {
                    it.activate(x, y, type)
                    break
                }
            }
    }

    fun update(deltaTime: Float) {
        items.forEach {
            if (it.isActive()) {
                it.update(deltaTime)
            }
        }
    }
}