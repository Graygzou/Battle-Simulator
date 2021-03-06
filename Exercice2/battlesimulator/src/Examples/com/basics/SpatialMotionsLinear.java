package basics;


import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;



public class SpatialMotionsLinear extends SimpleApplication {

    public static void main(String[] args) {
        SpatialMotionsLinear app = new SpatialMotionsLinear();
        
        //set vSinc on to get stable 60 fps
        AppSettings aps = new AppSettings(true);
        aps.setVSync(true);
        app.setSettings(aps);
        app.start();
        
    }

    Geometry geom;     
    Vector3f vecAim;
    Vector3f vec1;
    Vector3f vec2;     
    Vector3f vec3;  
    float angla;
    Quaternion vectry;
    Vector3f front;
    Vector3f vecB;
    Quaternion qRot=new Quaternion();
    float ang;
    float moveMe;
    float remainingDist;
    


    public void vectorz(){
     vec1 = new Vector3f(1,0,1);
     vec2   = new Vector3f(20,0,5);     
     vec3 = new Vector3f(5,0,15);  
   }     
    
              
    @Override
    public void simpleInitApp() {
        
        vectorz();
        
        Box b = new Box(Vector3f.ZERO, 1, 1, 1);
        geom = new Geometry("Box", b);
        geom.setLocalScale(1,1,2);
        geom.updateModelBound();

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        geom.setLocalTranslation(vec1);
        rootNode.attachChild(geom);
        
        flyCam.setMoveSpeed(30);
        viewPort.setBackgroundColor(ColorRGBA.Gray);
        cam.setLocation(new Vector3f(23.0f, 35.0f, 33.0f));
        cam.setRotation(new Quaternion (-0.16484f, 0.83351f, -0.40033f, -0.34323f));
        
    }


    public void transObj (float tpf, Spatial spatial, boolean boo) {
         
        //Aim to move
        if (spatial.getLocalTranslation().equals(vec1) == true) vecAim = vec2;
        if (spatial.getLocalTranslation().equals(vec2) == true) vecAim = vec3;
        if (spatial.getLocalTranslation().equals(vec3) == true) vecAim = vec1;
        
      
      //rotation  
      angla = tpf * 2.1f; //speed

      vectry = spatial.getWorldRotation();
      front = vectry.mult(Vector3f.UNIT_Z).normalize();
      
      vecB = spatial.getWorldTranslation().subtract(vecAim).setY(spatial.getWorldTranslation().y).normalize();
      
      
      qRot.lookAt(vecB, Vector3f.UNIT_Y);
      
      ang = vecB.angleBetween(front);
      
      if (ang>0.01f) { 
          
      vectry.slerp(qRot, angla/ang);        
      spatial.setLocalRotation(vectry);
      }
      else spatial.setLocalRotation(qRot);
      
//      System.out.println(tpf);
     
      //translation
     moveMe = tpf * 7f; //speed
     remainingDist = spatial.getLocalTranslation().distance(vecAim);
     
     if (remainingDist > 0.01f && !spatial.getWorldTranslation().equals(vecAim)) {
     spatial.setLocalTranslation(FastMath.interpolateLinear(moveMe/remainingDist, spatial.getLocalTranslation(), vecAim));
     }
     else {
         if (remainingDist <= 0.2f && !spatial.getWorldTranslation().equals(vecAim)) 
             spatial.setLocalTranslation(vecAim);
     }
     
    }
     
    
@Override
public void simpleUpdate(float tpf)
{
    
        transObj(tpf,geom,true);
        
 }

}
