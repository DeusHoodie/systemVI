package com.systemvi.engine.ui.widgets

import com.systemvi.engine.ui.{Widget, WidgetRenderer}
import org.joml.Vector2f

abstract class StatelessWidget extends Widget {
  var child:Widget=build()
  override def build():Widget
  override def calculateSize(maxParentSize:Vector2f): Vector2f = {
    size.set(maxParentSize)
    if(child!=null){
      child.calculateSize(maxParentSize)
      size.set(child.size)
    }
    return size
  }
  override def calculatePosition(parentPosition:Vector2f): Unit = {
    position.set(parentPosition)
    if(child!=null){
      child.calculatePosition(position)
    }
  }
  override def draw(renderer: WidgetRenderer): Unit = {
    if(child!=null)child.draw(renderer)
  }
  override def debugPrint(tabs: String): Unit = {
    println(s"$tabs StatelessWidget")
    if(child!=null)child.debugPrint(s"$tabs\t")
  }
}
