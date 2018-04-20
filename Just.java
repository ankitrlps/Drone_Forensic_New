package application;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
public class Just extends Application {
	  private static final int DRAW_WIDTH  = 900;
	  private static final int DRAW_HEIGHT = 500;

	  private static final double CAMERA_INITIAL_DISTANCE = -1500;
	  private static final double CAMERA_NEAR_CLIP = 0.1;
	  private static final double CAMERA_FAR_CLIP = 10000.0;

	  private Rotate worldRotateY = new Rotate(0, Rotate.Y_AXIS);

	  private Animation myAnimation;

	  //private Canvas canvas;          //Area on which to draw graphics items.
	  //private GraphicsContext gtx;    //Drawing methods for the Canvas.

	  Group root = new Group();
	  Group world = new Group();

	  PerspectiveCamera camera = new PerspectiveCamera(true);
	  @Override
	  //=========================================================================
	  //start(Stage stage)
	  //This is a JavaFX callback method. It is called by JavaFX after JavaFX
	  //   has created a window. The parameter, Stage stage, is a pointer to
	  //   the part of the window where the programmer can add widgets, such as
	  //   buttons, menus and canvases.
	  //=========================================================================
	  public void start(Stage stage) throws Exception
	  {
	    //canvas = new Canvas(DRAW_WIDTH, DRAW_HEIGHT);
	    //gtx = canvas.getGraphicsContext2D();

	    root.getChildren().add(world);
	    root.setDepthTest(DepthTest.ENABLE);

	    buildLights();
	    buildCamera();
	    buildMolecule();

	    Scene scene = new Scene(root, DRAW_WIDTH, DRAW_HEIGHT, true);
	    scene.setFill(Color.GREY);
	    //handleKeyboard(scene, world);
	    //handleMouse(scene, world);


	    //Set the window's title in its title bar.
	    stage.setTitle("Javafx3D_block");
	    stage.setScene(scene);
	    stage.show();

	    scene.setCamera(camera);

	    myAnimation = new Animation();
	    myAnimation.start();
	  }

	  private void buildLights()
	  {
	    PointLight light = new PointLight();
	    light.setColor(Color.WHITE);
	    Group lightGroup = new Group();
	    lightGroup.getChildren().add(light);
	    lightGroup.setTranslateZ(-600);
	    lightGroup.setTranslateY(600);
	    lightGroup.setTranslateX(600);
	    root.getChildren().add(lightGroup);
	  }

	  private void buildCamera()
	  {
	    root.getChildren().add(camera);

	    camera.setNearClip(CAMERA_NEAR_CLIP);
	    camera.setFarClip(CAMERA_FAR_CLIP);
	    camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);

	    Rotate cameraRotateZ = new Rotate(180, Rotate.Z_AXIS); //To make +y in the up direction
	    camera.getTransforms().addAll (cameraRotateZ);
	  }


	  private void buildMolecule()
	  {
	    final PhongMaterial blackMaterial = new PhongMaterial();
	    blackMaterial.setDiffuseColor(Color.BLACK);
	    blackMaterial.setSpecularColor(Color.WHITE);

	    final PhongMaterial redMaterial = new PhongMaterial();
	    redMaterial.setDiffuseColor(Color.DARKRED);
	    redMaterial.setSpecularColor(Color.RED);

	    final PhongMaterial whiteMaterial = new PhongMaterial();
	    whiteMaterial.setDiffuseColor(Color.WHITE);
	    whiteMaterial.setSpecularColor(Color.LIGHTBLUE);

	    final PhongMaterial greyMaterial = new PhongMaterial();
	    greyMaterial.setDiffuseColor(Color.DARKGREY);
	    greyMaterial.setSpecularColor(Color.GREY);

	    final PhongMaterial greenMaterial = new PhongMaterial();
	    greenMaterial.setDiffuseColor(Color.GREEN);
	    greenMaterial.setSpecularColor(Color.GREEN);

	    final PhongMaterial blueMaterial = new PhongMaterial();
	    blueMaterial.setDiffuseColor(Color.DARKBLUE);
	    blueMaterial.setSpecularColor(Color.BLUE);



	    Sphere carbonSphere = new Sphere(40.0);
	    Sphere redSphere = new Sphere(50.0);
	    Sphere whiteSphere = new Sphere(70.0);
	    Sphere greenSphere = new Sphere(50.0);

	    Box blueBox1 = new Box(40,150,20);
	    Box blueBox2 = new Box(40,150,20);

	    carbonSphere.setMaterial(blackMaterial);
	    redSphere.setMaterial(redMaterial);
	    whiteSphere.setMaterial(whiteMaterial);
	    greenSphere.setMaterial(greenMaterial);
	    blueBox1.setMaterial(blueMaterial);
	    blueBox2.setMaterial(blueMaterial);

	    redSphere.setTranslateX(100);
	    whiteSphere.setTranslateX(-100);

	    greenSphere.setTranslateY(100);
	    greenSphere.setTranslateZ(-200);

	    blueBox1.setTranslateX(200);
	    blueBox1.setTranslateZ(50);

	    blueBox2.setTranslateX(200);
	    blueBox2.setTranslateZ(50);
	    Rotate blueBox2RotateX = new Rotate(30, Rotate.X_AXIS);
	    blueBox2.getTransforms().addAll (blueBox2RotateX);

	    world.getChildren().addAll(carbonSphere, redSphere, whiteSphere, greenSphere,  blueBox1, blueBox2);
	    world.getTransforms().addAll(worldRotateY);
	  }

	  //===========================================================================================
	  // Animation is an inner class of our JavafxRandomBox class.
	  // Animation is an "inner class" because it is inside the JavafxRandomBox class.
	  // Since Animation extends AnimationTimer, the Animation class MUST implement
	  //   public void handle(long now), a callback method that is called by JavaFX at 60Hz.
	  //===========================================================================================
	  class Animation extends AnimationTimer
	  {
	    @Override
	    //=========================================================================================
	    //handel is a callback method called by JavaFX at 60Hz.
	    //  Try printing the value of the parameter now. Can you guess what it is?
	    //  Why is it a long?
	    //  Note: printing to the console (System.out.println()) is a relatively slow task.
	    //        Thus, while it can be instructive to call
	    //          System.out.println("JavafxRandomBox.Animaton.handle() now="+now);
	    //        in this callback, remember that JavaFX is calling it at 60Hz.
	    //        Therefore, if you leave that print statement in handle, it will slow down the
	    //        program.
	    //=========================================================================================
	    public void handle(long now)
	    {
	      double angle = worldRotateY.getAngle();
	      worldRotateY.setAngle(angle+0.25);
	    }
	  } //This bracket ends Animation, the inner class.


	  public static void main(String[] args)
	  {
	    launch(args);
	  }
}
