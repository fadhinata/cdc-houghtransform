import java.util.Arrays;


public class ImageEffects {
	public static double[] ISOTROPIC_VERTICAL = new double[]{-1, 0, 1, -Math.sqrt(2), 0, Math.sqrt(2), -1, 0, 1};
	public static double[] ISOTROPIC_HORIZONTAL = new double[]{1, Math.sqrt(2), 1, 0, 0, 0, -1, -Math.sqrt(2), -1};
	public static double[] PREWITT_VERTICAL = new double[]{-1, 0, 1, -1, 0, 1, -1, 0, 1};
	public static double[] PREWITT_HORIZONTAL = new double[]{1, 1, 1, 0, 0, 0, -1, -1, -1};
	
	
	public static double[] SOBEL_VERTICAL = new double[]{-1, 0, 1, 
														 -2, 0, 2, 
														 -1, 0, 1};
	
	public static double[] SOBEL_HORIZONTAL = new double[]{-1, -2, -1,
														   0, 0, 0, 
														   1, 2, 1};
	
	private static int[][] NAGAO = new int[][]{
		new int[]{	0,1,1,1,0,
					0,1,1,1,0,
					0,0,1,0,0,
					0,0,0,0,0,
					0,0,0,0,0},
		
		new int[]{	0,0,0,0,0,
					1,1,0,0,0,
					1,1,1,0,0,
					1,1,0,0,0,
					0,0,0,0,0},
					  
		new int[]{	0,0,0,0,0,
				    0,0,0,0,0,
				    0,0,1,0,0,
				    0,1,1,1,0,
				    0,1,1,1,0},
				      
		new int[]{	0,0,0,0,0,
				    0,0,0,1,1,
				    0,0,1,1,1,
				    0,0,0,1,1,
				    0,0,0,0,0},

		new int[]{	1,1,0,0,0,
		      		1,1,1,0,0,
		      		0,1,1,0,0,
		      		0,0,0,0,0,
		      		0,0,0,0,0},

		new int[]{	0,0,0,1,1,
					0,0,1,1,1,
					0,0,1,1,0,
					0,0,0,0,0,
					0,0,0,0,0},
						
		new int[]{	0,0,0,0,0,
					0,0,0,0,0,
					0,1,1,0,0,
					1,1,1,0,0,
					1,1,0,0,0},
						
		new int[]{	0,0,0,0,0,
      				0,0,0,0,0,
      				0,0,1,1,0,
      				0,0,1,1,1,
      				0,0,0,1,1},
      	
      	new int[]{	0,0,0,0,0,
					0,1,1,1,0,
					0,1,1,1,0,
					0,1,1,1,0,
					0,0,0,0,0}
	};
	
	//NOISE FILTERS
	
	/**
	 * Apply the average filter to the given image for a given number of 
	 * iterations.
	 * 
	 * @param image - original image
	 * @param cycles - number of iterations
	 * @return a filtered copy of the image
	 */
	
	public static GreyImage multipleAverageFilter(GreyImage image, int cycles){	
		GreyImage temp = image.imageCopy();
		
		for (int i = 0; i < cycles; i++) {
			temp = averageFilter(temp);
		}
		
		return temp;
	}
	
	/**
	 * Apply the median filter to the given image.
	 * 
	 * @param image - original image
	 * @return a filtered copy of the image
	 */
	
	public static GreyImage medianFilter(GreyImage image){
		GreyImage imageCopy = image.imageCopy();
		colorImage(imageCopy, 0);
		
		int height = image.getHeight();
		int width = image.getWidth();
		int[] resultPixels = imageCopy.getPixels();
		int[] neighbours = new int[9];
		
		for (int i = 1; i < height - 1; i++) {
			for (int j = 1; j < width - 1; j++) {
				int index = i*image.getWidth() + j;
				resultPixels[index] = getMedian(getNeighbors(index, image, neighbours));
			}
		}
		
		return imageCopy;
	}

	/**
	 * Apply the rank filter to the given image.
	 * 
	 * @param image - original image
	 * @param range - size of the range on which calculate the average
	 * @return a filtered copy of the image
	 */
	
	public static GreyImage rankFilter(GreyImage image, int range){
		GreyImage imageCopy = image.imageCopy();
		colorImage(imageCopy, 0);
		int[] neighbors = new int[9];
		
		for (int i = 1; i < image.getHeight() - 1; i++) {
			for (int j = 1; j < image.getWidth() - 1; j++) {
				int index = i*image.getWidth() + j;
				neighbors = getNeighbors(index, image, neighbors);
				Arrays.sort(neighbors);
				imageCopy.getPixels()[index] = getAverage(neighbors, range);
				
			}
		}
		
		return imageCopy;
	}
	
	/**
	 * Apply the nagao operator on the given image.
	 * 
	 * @param image - original image
	 * @return a copy of the image with the filter applied
	 */
	
	public static GreyImage nagaoFiltering(GreyImage image){
		int newWidth = image.getWidth() - 4;
		int newHeight = image.getHeight() - 4;
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		int[] newPixels = new int[newHeight*newWidth];
		int index = 0;
		
		int[] neighbours = new int[25];
		
		for (int i = 2; i < height - 2; i++) {
			for (int j = 2; j < width - 2; j++) {
				neighbours = getNeighbors(i*width + j, image, neighbours);
				
				int minIndex = 0;
				double minVariance = Double.MAX_VALUE;

				for (int k = 0; k < NAGAO.length; k++) {
					double temp = variance(neighbours, NAGAO[k]);
					
					if (temp < minVariance) {
						minIndex = k;
						minVariance = temp;
					}
				}
				
				newPixels[index] = (int) average(neighbours, NAGAO[minIndex]);
				index++;
			}
		}
		
		return new GreyImage(newPixels, newWidth, newHeight);
	}
	
	private static GreyImage averageFilter(GreyImage image){
		GreyImage imageCopy = image.imageCopy(); 
		colorImage(imageCopy, 0);
		int[] neighbours = new int[9];
		
		for (int i = 1; i < image.getHeight() - 1; i++) {
			for (int j = 1; j < image.getWidth() - 1; j++) {
				int index = i*image.getWidth() + j;
				imageCopy.getPixels()[index] = getAverage(getNeighbors(index, image, neighbours), 9);
			}
		}
		
		return imageCopy;
	}
	
	//EDGE DETECTION
	
	/**
	 * Apply the sobel filter to the given image. Returns an array with module
	 * and phase.
	 * 
	 * @param image - original image
	 * @return an array of GreyImage: the first element is the module and  the
	 * second is the phase of the sobel.
	 */
	
	public static GreyImage[] binarySobel(GreyImage image, float threshold){
		GreyImage imageVertical = applyKernelToImageNoBounds(image, SOBEL_HORIZONTAL);
		GreyImage imageHorizontal = applyKernelToImageNoBounds(image, SOBEL_VERTICAL);
		
		GreyImage moduleImage = imageVertical.imageCopy();
		GreyImage phaseImage = imageVertical.imageCopy();
		
		for (int i = 0; i < imageHorizontal.getPixels().length; i++) {
			int a = imageVertical.getPixels()[i];
			int b = imageHorizontal.getPixels()[i];
			moduleImage.getPixels()[i] = (int) Math.sqrt(a*a + b*b);
			if (moduleImage.getPixels()[i] > 255) moduleImage.getPixels()[i] = 255;
			if (moduleImage.getPixels()[i] < 0) moduleImage.getPixels()[i] = 0;
			phaseImage.getPixels()[i] = (int)(255*(Math.atan2(imageVertical.getPixels()[i], imageHorizontal.getPixels()[i]) + Math.PI)/(2*Math.PI));
		}
		
		return new GreyImage[]{moduleImage, phaseImage};
	}	
	
	
	/**
	 * Apply the convolution of the given filter to the given image. Without checking boudaries.
	 * 
	 * @param image
	 * @param kernel
	 * @return
	 */
	
	private static GreyImage applyKernelToImageNoBounds(GreyImage image, double[] kernel){
		int N = (int) Math.sqrt(kernel.length);
		int width = image.getWidth() - N/2 - N/2;
		int height = image.getHeight() - N/2 - N/2;
		
		int[] newPixels = new int[width*height];
		GreyImage newImage = new GreyImage(newPixels, width, height);
		
		ImageEffects.colorImage(newImage, 0);
		
		int[] neighbors = new int[kernel.length];
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int oldIndex = (i+N/2)*image.getWidth() + (j+N/2);
				neighbors = getNeighbors(oldIndex, image, neighbors);
				
				for (int k = 0; k < neighbors.length; k++) {
					newPixels[i*width + j] += neighbors[k]*kernel[k];
				}
			}
		}
		
		return newImage;
	}
	
	/**
	 * Make the convolution of the given kernel and given image.
	 * 
	 * @param image - original image
	 * @param kernel
	 * @return a GreyImage: the convolution
	 */
	
	public static GreyImage applyKernelToImage(GreyImage image, double[] kernel){
		GreyImage temp = applyKernelToImageNoBounds(image, kernel);
		
		int[] pixels = temp.getPixels();
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = Math.abs(pixels[i]);
			if(pixels[i] > 255) pixels[i] = 255;
		}
		
		return temp;
	}
	
	
	//TOOLS
	
	public static int[] getNeighbors(int index, GreyImage image, int[] result){
		int N = result.length;
		int rad = (int) Math.sqrt(N) - 1;
		int cont = 0;
		int[] imgPixels = image.getPixels();
		int width = image.getWidth();
		
		for (int i = -rad/2; i <= rad/2; i++) {
			for (int j = -rad/2; j <= rad/2; j++) {
				result[cont] = imgPixels[index + i*width + j];
				cont++;
			}
		}
		
		return result;
	}
	
	/**
	 * Return an array containing the N neighbors of the pixel of given index.
	 * 
	 * @param index - index of the pixel
	 * @param image . original image
	 * @param N - number of neighbors
	 * @return an array with all the neighbors
	 */
	
	public static int[] getNeighbors(int index, GreyImage image, int N){
		return getNeighbors(index, image, new int[N]);
	}
	
	private static void colorImage(GreyImage image, int color){
		int[] pixels = image.getPixels();
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	private static int getAverage(int[] vector, int range){
		int sum = 0;
		
		for (int i = (vector.length - range)/2; i < (vector.length + range)/2; i++) {
			sum += vector[i];
		}
		
		return sum/vector.length;
	}	
	
	private static int getMedian(int[] vector){
		Arrays.sort(vector);
		return vector[vector.length/2];
	}
	

	private static double average(int[] array, int[] kernel){
		double sum = 0;
		int cont = 0;
		
		for (int i = 0; i < array.length; i++) {
			if(kernel[i] == 1) {
				sum += array[i];
				cont++;
			}
		}
		return sum/cont;
	}
	
	private static double variance(int[] array, int[] kernel){
		double mu = average(array, kernel);
	    double sumsq = 0.0;
	    int cont = 0;
	    
	    for (int i = 0; i < array.length; i++){
	    	if(kernel[i] == 1){
	    		cont++;
	    		sumsq += (array[i] - mu) * (array[i] - mu);
	    	}
	    }
	       
	    return sumsq / cont;
	}
	
	
	/**
	 * Normalize the given image in the range [0,255]. Modify the original image.
	 * 
	 * @param image - original image
	 */
	public static void normalizeImage(GreyImage image){
		int[] pixels = image.getPixels();
		int min = pixels[0];
		int max = pixels[0];
		
		
		for (int i = 1; i < pixels.length; i++) {
			if(pixels[i] < min) min = pixels[i];
			if(pixels[i] > max) max = pixels[i];
		}
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = (int) (255*((double)(pixels[i] - min)/(double)(max - min)));
		}
	}
	
	private static GreyImage resizeImage(GreyImage image, int Delta){
		int[] pixels = new int[(image.getHeight()-Delta*2)*(image.getWidth()-Delta*2)];
		int cont = 0;
		
		for (int i = Delta; i < image.getHeight() - Delta; i++) {
			for (int j = Delta; j < image.getWidth() - Delta; j++) {
				pixels[cont] = image.getPixels()[i*image.getWidth() + j];
				cont++;
			}
		}
		
		return new GreyImage(pixels, image.getWidth()-Delta*2, image.getHeight()-Delta*2);
	}
	
	/**
	 * Compute the mean square error between two images. If the size isn't the
	 * same one of them is resized. (Works only on images that come from the 
	 * same original image).
	 * 
	 * @param i1 - first image
	 * @param i2 - second image
	 * @return the MSE value
	 */
	
	public static double MSE(GreyImage i1, GreyImage i2){
		GreyImage image1 = i1;
		GreyImage image2 = i2;
		
		int height = Math.min(i1.getHeight(), i2.getHeight());
		int width = Math.min(i1.getWidth(), i2.getWidth());
		
		if(i1.getWidth() > i2.getWidth()){
			image1 = resizeImage(i1, Math.abs(i1.getWidth() - i2.getWidth()));
		} 
		else if (i1.getWidth() < i2.getWidth()){
			image2 = resizeImage(i2, Math.abs(i1.getWidth() - i2.getWidth()));
		}
		
		int[] pixels1 = image1.getPixels();
		int[] pixels2 = image2.getPixels();
	
		double sum = 0;
		
		for (int i = 0; i < pixels2.length; i++) {
			sum += (pixels1[i] - pixels2[i])*(pixels1[i] - pixels2[i]);
		}
		
		return sum/(width*height);
	}
}
