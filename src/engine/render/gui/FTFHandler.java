package engine.render.gui;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import org.json.JSONObject;
import org.lwjgl.BufferUtils;

import engine.render.Texture;
import engine.render.shapes.Shape;
import engine.render.shapes.ShapeLoader;
import engine.render.utils.Font;

public class FTFHandler implements IFontHandler{
	int default_char;
	Texture t;
	
	Font font = new Font(1,0);
	
	int width, height;
	
	char[] chars; 
	int[] locs; 
	
	

	public FTFHandler(String file) {
		try {
			JSONObject obj = new JSONObject(Files.readString(Paths.get(file)));
			t = new Texture(obj.getString("atlas"), false);
			width = obj.getInt("width");
			height = obj.getInt("height");
		
			String mapping = obj.getString("mapping");
			mapping = mapping.replaceAll("[\\t\\r  *\\n]", "");
			String[] pairs = mapping.split(",");
			chars = new char[pairs.length];
			locs = new int[pairs.length+1];
			locs[0] = obj.getInt("default");
			
			for(int i = 0; i < chars.length; i++) {
				String[] tuple = pairs[i].split(":");
				chars[i] = (char) Byte.valueOf(tuple[0]).byteValue();
				locs[i+1] = Integer.valueOf(tuple[1]);
			}			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public int indexOf(char x) {
		/*int first = 0;
		int index = chars.length/2;
		int last = chars.length-1;
				
		if(chars[first] == x) {
			return first;
		}
		
		if(chars[last] == x) {
			return last;
		}
			
		while (first <= index && last >= index) {
			char chr = chars[index];
			
			if(chr == x) {
				return index;
			} else if (chr < x){
				first = index;
				index = (last - first)/2;
				
			} else {
				last = index;
				index = (last - first)/2;
			}
			
		}
		
		
		return -1;*/
		return Math.max(Arrays.binarySearch(chars, x), -1);	
	}
	
	public int getLoc(char x) {
		return locs[indexOf(x) + 1];
		
	}


	@Override
	public Shape generateShape(String text) {
		int valid_length = 0;
		int index_pointer = 0;
				
		for(int i = 0; i < text.length(); i++) {
			if(!Character.isWhitespace(text.charAt(i))) {
				valid_length++;
			}
		}
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(valid_length * 20);
		IntBuffer indxs = BufferUtils.createIntBuffer(valid_length * 6);
		float offsetx = 0;
		float offsety = 0;
		
		for(int i = 0 ; i < text.length(); i++) {

			char chr = text.charAt(i);
			
			switch(chr) {
			
			case 32:
			//SPACE
				offsetx += 1 + font.spacing;
				break;
			case 9:
			//TAB
				offsetx += 4 * (1 + font.spacing);
				break;
			case 10:
			//NEWLINE
				offsety -= 1 + font.spacing;
				offsetx = 0;
				break;
			case 13:
			//RETURN
				offsetx = 0;
				break;
			case 8:
			//BACKSPACE
				offsetx -= 1 + font.spacing;
				break;
				
			default:

				if(!Character.isWhitespace(chr)) {
					
					loadTexQuad(buffer, offsetx, offsety, getLoc(chr));
					
					indxs.put(new int[] {index_pointer, index_pointer+1, index_pointer+2, index_pointer, index_pointer+2, index_pointer+3});
					offsetx += 1 + font.spacing;
					index_pointer+=4;
				}
				break;
				
			}
			
		}
		

		
		buffer.flip();
		indxs.flip();
		
		return ShapeLoader.createTexShape(buffer, indxs);
	}
	
	public void loadTexQuad(FloatBuffer buffer, float offsetx, float offsety, int loc) {
		
		int col = loc % width;
		int row = loc / width;
				
		float high_x = col * 1.0f / this.width;
		float low_y = row * 1.0f/ this.height;
		float low_x = (col+1.0f) / this.width;
		float high_y = (row+1f) / this.height;
	
		buffer.put(new float[] {offsetx, offsety, 0, high_x, low_y});
		buffer.put(new float[] {offsetx, offsety-1, 0, high_x, high_y });
		buffer.put(new float[] {offsetx+1, offsety-1, 0, low_x, high_y });
		buffer.put(new float[] {offsetx+1, offsety, 0, low_x, low_y });
	}


	@Override
	public float getSize() {
		return font.scale;
	}


	@Override
	public Texture getTexture() {
		return t;
	}


	@Override
	public void setFont(Font f) {
		this.font = f;
	}
	
	
	
}
