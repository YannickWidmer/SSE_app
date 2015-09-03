package ch.yannick.display.activityMental;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Yannick on 05.07.2015.
 */
class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //mRenderer.onSurfaceChanged(null,getWidth(),getHeight());
        //requestRender();
    }
}