package com.systemvi.examples.texturetest;

import com.systemvi.engine.application.Application;
import com.systemvi.engine.camera.Camera;
import com.systemvi.engine.model.Mesh;
import com.systemvi.engine.model.VertexAttribute;
import com.systemvi.engine.shader.Shader;
import com.systemvi.engine.texture.Texture;
import com.systemvi.engine.window.Window;
import static org.lwjgl.opengl.GL33.*;

public class App extends Application {
    public App(int openglVersionMajor, int openglVersionMinor, int targetFPS) {
        super(openglVersionMajor, openglVersionMinor, targetFPS);
    }
    Window window;
    Mesh mesh;
    Shader shader;
    Camera camera;
    Texture texture;

    @Override
    public void setup() {
        window=new Window(800,600,"Texture Test");
        mesh=new Mesh(
            new VertexAttribute("position",2),
            new VertexAttribute("color",4),
            new VertexAttribute("texCoords",2)
        );
        int width=800,height=800;
        mesh.setVertexData(new float[]{
            //position                //color           //texCoords
                  100,        100,    0, 0, 0.5f, 1,    0, 0,
                  100, 100+height,    1, 0, 0.5f, 1,    0, 1,
            100+width,        100,    0, 1, 0.5f, 1,    1, 0,
            100+width, 100+height,    1, 1, 0.5f, 1,    1, 1,
        });
        mesh.setIndices(new int[]{
            0,1,2,
            1,2,3,
        });
        shader=new Shader(
            "assets/examples/textureTest/vertex.glsl",
            "assets/examples/textureTest/fragment.glsl"
        );
        if(!shader.isCompiled()){
            System.out.println(shader.getLog());
        }
        camera=new Camera();
        camera.setPosition(400,300,0);
        camera.setScreenSize(800,600);
        camera.setScale(1,-1,1);
        camera.update();
        texture=new Texture("assets/examples/textureTest/wall.jpg");
        texture.setBorderColor(1,0,1,1);
        texture.setRepeat(GL_CLAMP_TO_EDGE,GL_REPEAT);
        texture.setSamplerFilter(GL_LINEAR_MIPMAP_NEAREST,GL_NEAREST);

        window.addOnResizeListener((width1, height1) -> {
            camera.setPosition(width1/2,height1/2,0);
            camera.setScreenSize(width1,height1);
            camera.update();
        });
    }

    @Override
    public void loop(float delta) {
        if(window.shouldClose())close();
        window.pollEvents();

        glClearColor(0.2f,0.6f,0.8f,1.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        texture.bind(10);
        shader.use();
        shader.setUniform("wall",10);
        shader.setUniform("view",camera.getView());
        shader.setUniform("projection",camera.getProjection());
        mesh.drawElements(2);

        window.swapBuffers();
    }
}