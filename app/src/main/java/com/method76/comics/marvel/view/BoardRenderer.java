package com.method76.comics.marvel.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.method76.comics.marvel.common.constant.AppConst;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * 보드 그림
 */
public class BoardRenderer implements Renderer, AppConst {

	private Context context;
	private BoardView yutBoard;
	private float[] mProjMatrix = new float[16];
	private float[] mVMatrix = new float[16];
	private float[] mMVPMatrix = new float[16];
	private float[] mModelMatrix = new float[16];
	private float[] mTempMatrix = new float[16];
	private float[] mRotationMatrix = new float[16];
	private final float angle = 45.0f;


	/** Constructor to set the handed over context */
	public BoardRenderer(Context context) {
		this.context = context;    
		this.yutBoard = new BoardView();
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Load the texture for the yutBoard
		yutBoard.loadGLTexture(gl, this.context);
		// Tell OpenGL to generate textures.

		gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping ( NEW )
		// Enable blending using premultiplied alpha.
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f); 	//Black Background
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); 
		
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Replace the current matrix with the identity matrix
		gl.glLoadIdentity();
		// Translates 10 units into the screen.
		// Rotate yutBoard
		gl.glTranslatef(0, 0, -2.4f);
//		gl.glRotatef(-angle, 1f, 0, 0);   	 	// x,y 축 회전
		gl.glScalef(1.0f, 0.9f, 1f);				// x,y,z 크기 변환
		// Draw yutBoard
		yutBoard.draw(gl);
	}


	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if(height == 0) { 						//Prevent A Divide By Zero By
			height = 1; 						//Making Height Equal One
		}

		gl.glViewport(0, 0, width, height); 	//Reset The Current Viewport
		gl.glMatrixMode(GL10.GL_PROJECTION); 	//Select The Projection Matrix
		gl.glLoadIdentity(); 					//Reset The Projection Matrix

		float ratio = (float) width / height;
	    // this projection matrix is applied to object coordinates
	    // in the onDrawFrame() method
		Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	    
		//Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float)height, 0.1f, 100.0f);

		gl.glMatrixMode(GL10.GL_MODELVIEW); 	//Select The Modelview Matrix
		gl.glLoadIdentity(); 					//Reset The Modelview Matrix
		
	}


	/**
	 * Helper method to load a GL texture from a bitmap
	 *
	 * Note that the caller should "recycle" the bitmap
	 *
	 * @return the ID of the texture returned from glGenTextures()
	 */
	public static int loadGLTextureFromBitmap( Bitmap bitmap, GL10 gl ) {

		// Generate one texture pointer
		int[] textureIds = new int[1];
		gl.glGenTextures( 1, textureIds, 0 );

		// bind this texture
		gl.glBindTexture( GL10.GL_TEXTURE_2D, textureIds[0] );

		// Create Nearest Filtered Texture
		gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
		gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );

		gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT );
		gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT );

		// Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		return textureIds[0];
	}


	/**
	 * Create a texture from a given resource
	 *
	 * @param resourceID the ID of the resource to be loaded
	 * @param scaleToPO2 determines whether the image should be scaled up to the next highest power
	 * of two, or whether it should be "inset" into such an image. Having textures that are
	 * dimensions of some power-of-two is critical for performance in opengl.
	 *
	 * @return the ID of the texture returned from glGenTextures()
	 */
	public static int loadGLTextureFromResource( int resourceID, Context context,
												 boolean scaleToPO2, GL10 gl ) {

		// pull in the resource
		Bitmap bitmap = null;
		Resources resources = context.getResources();

		Drawable image = resources.getDrawable( resourceID );
		float density = resources.getDisplayMetrics().density;

		int originalWidth = (int)(image.getIntrinsicWidth() / density);
		int originalHeight = (int)(image.getIntrinsicHeight() / density);

		int powWidth = getNextHighestPO2( originalWidth );
		int powHeight = getNextHighestPO2( originalHeight );

		if ( scaleToPO2 ) {
			image.setBounds( 0, 0, powWidth, powHeight );
		} else {
			image.setBounds( 0, 0, originalWidth, originalHeight );
		}

		// Create an empty, mutable bitmap
		bitmap = Bitmap.createBitmap( powWidth, powHeight, Bitmap.Config.ARGB_4444 );
		// get a canvas to paint over the bitmap
		Canvas canvas = new Canvas( bitmap );
		bitmap.eraseColor(0);

		image.draw( canvas ); // draw the image onto our bitmap

		int textureId = loadGLTextureFromBitmap( bitmap, gl );

		bitmap.recycle();

		return textureId;
	}


	/**
	 * Calculates the next highest power of two for a given integer.
	 *
	 * @param n the number
	 * @return a power of two equal to or higher than n
	 */
	public static int getNextHighestPO2( int n ) {
		n -= 1;
		n = n | (n >> 1);
		n = n | (n >> 2);
		n = n | (n >> 4);
		n = n | (n >> 8);
		n = n | (n >> 16);
		n = n | (n >> 32);
		return n + 1;
	}


}