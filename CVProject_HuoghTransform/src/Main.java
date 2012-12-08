import static com.googlecode.javacv.cpp.opencv_highgui.*;

import javax.swing.RowFilter;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.*;

public class Main {
	public static void main(String[] args) {
		String filename = "/home/swordfish/Programming/Workspaces/ComputerVision-Workspace/CVProject_HuoghTransform/Polygons and straight lines.jpg";
		GreyImage image = PGMTools.importImage(filename);
		
		PGMTools.exportGreyImage("/home/swordfish/ASAH.pgm", ImageEffects.applyKernelToImage(image, ImageEffects.SOBEL_HORIZONTAL));
		PGMTools.exportGreyImage("/home/swordfish/ASAV.pgm", ImageEffects.applyKernelToImage(image, ImageEffects.SOBEL_VERTICAL));
		
		PGMTools.exportGreyImage("/home/swordfish/ASASDASDASDASDASDASDAS.pgm", ImageEffects.binarySobel(image, 0)[0]);
		
		
//		PGMTools.exportGreyImage("/home/swordfish/sdasdasdasdasdasdasd.pgm", new GreyImage(intPixels, imageMat.cols(), imageMat.rows()));
//		
//		int height = edgesModule.getHeight();
//		int width = edgesModule.getWidth();
//		int[] modulePixels = edgesModule.getPixels();
//		int[] phasePixels = edgesPhase.getPixels();
//		
//		GreyImage accumulator = new GreyImage(new int[width*height], width, height);
//		int[] pixelsAccumulator = accumulator.getPixels();
//		
//		//Example: searching a Square of edge L
//		int L = 50;
//		int apotheme = L/2;
//		
//		for (int i = 0; i < height; i++) {
//			for (int j = 0; j < width; j++) {
//				int index = i*width + j;
//				if(modulePixels[index] == 255){
//					double phase = (phasePixels[index]/255f) * 2 *  Math.PI;
//					double q1 = apotheme * (Math.cos(phase) + Math.tan(phase)*Math.sin(phase));
//					double q2 = -q1;
//				}
//			}
//		}
		
	}
}
