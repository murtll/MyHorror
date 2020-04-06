package com.gdx.game

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

class Item {
    private var active = false
    val position = Vector2(0F, 0F)
    private val velocity = Vector2(0F, 0F)
    private val velocityStart = Vector2(velocity)
    var type = Type.COIN

    enum class Type(val index: Int) {
        COIN(0), COIN_STACK(1), DOUBLE_COIN_STACK(2),
        HEAL_POTION(3), SPEED_POTION(4),
        SWORD(5), MACHETE(6), ALEBARD(7)
    }

    fun isActive() = active

    fun deactivate() {
        active = false
    }

    fun activate(x: Float, y: Float, _type: Type) {
        position.set(x, y)
        velocity.set(MathUtils.random(-20F, 20F), MathUtils.random(-20F, 20F))
        velocityStart.set(velocity)
        type = _type
        active = true
    }

    fun update(deltaTime: Float) {
        position.mulAdd(velocity, deltaTime)

        if (velocity.x > 0) {
            velocity.x -= deltaTime * 8
        } else if (velocity.x < 0) {
            velocity.x += deltaTime * 8
        }
        if (velocity.y > 0) {
            velocity.y -= deltaTime * 8
        } else if (velocity.y < 0) {
            velocity.y += deltaTime * 8
        }
    }
}