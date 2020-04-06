package com.gdx.game.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.gdx.game.MyGame

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        config.height = 540
        config.width = 800
//        config.fullscreen = true
        LwjglApplication(MyGame(), config)
    }
}