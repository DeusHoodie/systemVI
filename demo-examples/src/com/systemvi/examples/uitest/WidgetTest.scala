package com.systemvi.examples.uitest

import com.systemvi.engine.application.Game
import com.systemvi.engine.ui.utils.three.ThreeWalker
import com.systemvi.engine.ui.widgets._
import com.systemvi.engine.ui.{Scene, Widget, WidgetRenderer, utils}
import com.systemvi.engine.utils.Utils
import com.systemvi.engine.utils.Utils.Buffer
import com.systemvi.engine.window.{InputMultiplexer, Window}
import org.joml.{Vector2f, Vector4f}

import scala.collection.mutable

class WidgetTest extends Game(3,3,60,800,600,"Widget test"){

  private var scene:Scene=null
  override def setup(window: Window): Unit = {
    scene=Scene(
      window=window,
      root=App()
    )
    setInputProcessor(new InputMultiplexer(this,scene))
  }
  override def loop(delta: Float): Unit = {
    Utils.clear(0,0,0,0,Buffer.COLOR_BUFFER)
    scene.resize(800,600)
    scene.draw()
  }
}
