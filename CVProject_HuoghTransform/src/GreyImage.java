public class GreyImage {
	private int[] pixels;
	private int width;
	private int height;
	
	public GreyImage(int[] pixels, int width, int height){
		this.pixels = pixels;
		this.width = width;
		this.height = height;
	}
	
	public int[] getPixels() {
		return pixels;
	}
	public void setPixels(int[] pixels) {
		this.pixels = pixels;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	
	public GreyImage imageCopy(){
		int[] pixelsCopy = new int[pixels.length];
		System.arraycopy(pixels, 0, pixelsCopy, 0, pixels.length);
		return new GreyImage(pixelsCopy, width, height);
	}
}
