package com.systemvi.examples.minecraft.renderer;

import com.systemvi.engine.camera.Camera;
import com.systemvi.engine.renderers.ShapeRenderer;
import com.systemvi.engine.renderers.TextureRenderer;
import com.systemvi.engine.shader.Shader;
import com.systemvi.engine.texture.Format;
import com.systemvi.engine.texture.FrameBuffer;
import com.systemvi.engine.texture.Texture;
import com.systemvi.engine.utils.OpenGLUtils;
import com.systemvi.examples.minecraft.materials.Material;
import com.systemvi.examples.minecraft.world.Chunk;
import com.systemvi.examples.minecraft.world.World;
import org.joml.Vector3f;

public class WorldRenderer {
    public Vector3f sunDirection;
    public Vector3f sunColor;
    public Vector3f amibient;

    public Texture color,normal,depth,position;
    private final FrameBuffer frameBuffer;
    private final Shader blockFaceShader;
    public WorldRenderer(int width,int height){
        color=new Texture(width,height, Format.RGB);
        depth=new Texture(width,height,Format.DEPTH32);
        normal=new Texture(width,height,Format.RGB);
        position=new Texture(width,height,Format.RGB32);

        frameBuffer= FrameBuffer.builder()
            .color(color)
            .color(normal)
            .color(position)
            .depth(depth)
            .build();

        blockFaceShader=Shader.builder()
            .fragment("assets/examples/minecraft/blockFaceRenderer/fragment.glsl")
            .vertex("assets/examples/minecraft/blockFaceRenderer/vertex.glsl")
            .build();
        if(!blockFaceShader.isCompiled()){
            System.out.println(blockFaceShader.getLog());
        }
    }

    public void render(World world, Camera camera, Material material){
        Chunk[][][] chunks=world.getChunks();

        frameBuffer.begin();
        OpenGLUtils.clear(0,0,0,0, OpenGLUtils.Buffer.COLOR_BUFFER, OpenGLUtils.Buffer.DEPTH_BUFFER);
        OpenGLUtils.enableDepthTest();
        OpenGLUtils.enableFaceCulling();

        blockFaceShader.use();
        blockFaceShader.setUniform("view",camera.getView());
        blockFaceShader.setUniform("projection",camera.getProjection());

        material.diffuse.bind(0);
        blockFaceShader.setUniform("diffuseMap",0);

        material.normal.bind(1);
        blockFaceShader.setUniform("normalMap",1);
        for(int i=0;i<chunks.length;i++){
            for(int j=0;j<chunks[i].length;j++){
                for(int k=0;k<chunks[i][j].length;k++){
                    Chunk chunk=chunks[i][j][k];
                    chunk.mesh.drawInstancedElements(chunk.triangles,chunk.instancesToDraw);
                }
            }
        }

        OpenGLUtils.disableDepthTest();
        OpenGLUtils.disableFaceCulling();
        frameBuffer.end();
    }
}