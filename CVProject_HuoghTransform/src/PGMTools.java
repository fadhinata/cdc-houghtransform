import static com.googlecode.javacv.cpp.opencv_highgui.CV_LOAD_IMAGE_GRAYSCALE;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class PGMTools {	
	public static void exportGreyImage(String filePath, GreyImage image){
		try {
			FileWriter w = new FileWriter(filePath);
			BufferedWriter out = new BufferedWriter(w, 8192);
			StringBuilder builder = new StringBuilder();
			
			builder.append("P2");
			builder.append('\n');
			builder.append("#Authors: CDC");
			builder.append('\n');
			builder.append(image.getWidth());
			builder.append(" ");
			builder.append(image.getHeight());
			builder.append('\n');
			builder.append("255");
			
			for (int px : image.getPixels()) {
				builder.append('\n');
				builder.append(px);
			}
			
			out.write(builder.toString());
			
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static GreyImage importImage(String filepath){
		IplImage image = cvLoadImage(filepath, CV_LOAD_IMAGE_GRAYSCALE);
		CvMat imageMat = image.asCvMat();
		
		int[] intPixels = new int[image.width()*image.height()];
		
		System.out.println(imageMat.channels());
		
		int cont = 0;
	
		System.out.println(imageMat.cols() + " " + imageMat.step());
		
		for (int i = 0; i < imageMat.rows(); i++) {
			for (int j = 0; j < imageMat.cols(); j++) {
				int index = i*imageMat.step() + j;
				intPixels[cont] = (int) imageMat.get(index);
				cont++;
			}
		}
		
		return new GreyImage(intPixels, imageMat.cols(), imageMat.rows());
	}
	
	public static GreyImage importPGMImage(String filePath){
		try {
			InputStream f = new FileInputStream(filePath);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
            d.readLine();
            String line = d.readLine();            
            line = d.readLine();     // second line contains height and width
            int width = Integer.parseInt(line.split(" ")[0]);
            int height = Integer.parseInt(line.split(" ")[1]);
            int maxVal = Integer.parseInt(d.readLine());
            
            int[] pixels = new int[width*height];
            int i = 0;
            
            while ((line = d.readLine()) != null) {
            	pixels[i] = Integer.parseInt(line);
            	i++;
			}
            
            d.close();
            return new GreyImage(pixels, width, height);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void showImage(IplImage image){
		CanvasFrame canvas = new CanvasFrame("My Image", 1);
        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        canvas.showImage(image);
	}
	
	private static String renderFilepath(String base, String suffix){
		return base + suffix + ".pgm";
	}
}