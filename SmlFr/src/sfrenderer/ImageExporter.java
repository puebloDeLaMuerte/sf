package sfrenderer;

public class ImageExporter extends Thread {

	private SM_Renderer renderer;
	private String		filename;
	
	public ImageExporter( SM_Renderer renderer, String filename) {
		super();
		this.renderer = renderer;
		this.filename = filename;
	}
	
	@Override
	public void run() {
		
		renderer.loop();
		renderer.renderPreviewImage(filename);
		renderer.noLoop();
		
		System.err.println("DONE");
		
	}
	
}
