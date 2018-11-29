package comp557.a4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;

/**
 * Simple scene loader based on XML file format.
 */
public class Scene {
    
    /** List of surfaces in the scene */
    public List<Intersectable> surfaceList = new ArrayList<Intersectable>();
	
	/** All scene lights */
	public Map<String,Light> lights = new HashMap<String,Light>();

    /** Contains information about how to render the scene */
    public Render render;
    
    /** The ambient light colour */
    public Color3f ambient = new Color3f();

    /** 
     * Default constructor.
     */
    public Scene() {
    	this.render = new Render();
    }
    
    /**
     * renders the scene
     */
    public void render(boolean showPanel) {
 
        Camera cam = render.camera; 
        int w = cam.imageSize.width;
        int h = cam.imageSize.height;
        
        render.init(w, h, showPanel);
        
        for ( int i = 0; i < h && !render.isDone(); i++ ) {
            for ( int j = 0; j < w && !render.isDone(); j++ ) {
            	
                // TODO: Objective 1: generate a ray (use the generateRay method) - DONE??
            	Ray ray = new Ray();
            	double[] offset = {0, 0};
            	
            	// compute viewing ray
            	generateRay(j, i, offset, cam, ray);
            	//System.out.println("j:	" + j + "	i: " + i);
            	//System.out.println("eyePoint: " + ray.eyePoint + "	direction:" + ray.viewDirection);
            	//System.out.println("----");
            	
                // TODO: Objective 2: test for intersection with scene surfaces
            	for(Intersectable intersectable: surfaceList) {
            		IntersectResult intersectResult = new IntersectResult();
            		intersectable.intersect(ray, intersectResult);
            	}
            	
                // TODO: Objective 3: compute the shaded result for the intersection point (perhaps requiring shadow rays)
                
            	// TODO: Objective 8: do antialiasing by sampling more than one ray per pixel
            	
            	// Here is an example of how to calculate the pixel value.
            	Color3f c = new Color3f(render.bgcolor);
            	int r = (int)(255*c.x);
                int g = (int)(255*c.y);
                int b = (int)(255*c.z);
                int a = 255;
                int argb = (a<<24 | r<<16 | g<<8 | b);    
                
                // update the render image
                render.setPixel(j, i, argb);
                
                
                
            }
        }
        
        // save the final render image
        render.save();
        
        // wait for render viewer to close
        render.waitDone();
        
    }
    
    /**
     * Generate a ray through pixel (i,j).
     * 
     * @param i The pixel row.
     * @param j The pixel column.
     * @param offset The offset from the center of the pixel, in the range [-0.5,+0.5] for each coordinate. 
     * @param cam The camera.
     * @param ray Contains the generated ray.
     */
	public static void generateRay(final int i, final int j, final double[] offset, final Camera cam, Ray ray) {
		
		// TODO: Objective 1: generate rays given the provided parmeters
		
		//S = E + uU + vV - dW
		//P = E
		//D = S - E
		//R(t) = P + tD
		
		//Pixel to image mapping
		//u = l + (r - l)(i + 0.5)/n_x
		//v = b + (t - b)(j + 0.5)/n_y
		
		ray.eyePoint.set(cam.from);

		double w = cam.imageSize.width;
		double h = cam.imageSize.height;
		double d  = cam.from.distance(cam.to);
		double screentop = Math.tan(Math.toRadians(cam.fovy)/2.0) * d;

		//Get view area dimensions
		double ratio = w/h;
		double left = -ratio*screentop;
		double right = ratio*screentop;
		double top = screentop;
		double bottom = -screentop;
		double u = left + (right - left)*(i + offset[0])/w; 
		double v = top - (top - bottom)*(j + offset[1])/h; 
		
		//calculate the view Direction of the ray
		Vector3d cameraZ = new Vector3d(cam.from);
		cameraZ.sub(cam.to);
		cameraZ.normalize();
		Vector3d cameraX = new Vector3d();
		cameraX.cross(cam.up, cameraZ);
		cameraX.normalize();	
		Vector3d cameraY = new Vector3d();
		cameraY.cross(cameraX, cameraZ);
		cameraY.negate();
		cameraY.normalize();	
		cameraX.scale(u);
		cameraY.scale(v);
		cameraZ.scale(d);

		Vector3d s = new Vector3d(cam.from);
		s.add(cameraX);
		s.add(cameraY);
		s.sub(cameraZ);
		s.sub(cam.from);
		s.normalize();
		ray.viewDirection.set(s);
		

	}

	/**
	 * Shoot a shadow ray in the scene and get the result.
	 * 
	 * @param result Intersection result from raytracing. 
	 * @param light The light to check for visibility.
	 * @param root The scene node.
	 * @param shadowResult Contains the result of a shadow ray test.
	 * @param shadowRay Contains the shadow ray used to test for visibility.
	 * 
	 * @return True if a point is in shadow, false otherwise. 
	 */
	public static boolean inShadow(final IntersectResult result, final Light light, final SceneNode root, IntersectResult shadowResult, Ray shadowRay) {
		
		// TODO: Objective 5: check for shdows and use it in your lighting computation
		
		return false;
	}    
}
