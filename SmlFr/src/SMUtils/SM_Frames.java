package SMUtils;

import processing.core.PImage;


public class SM_Frames {

	private PImage frame_wood_dark_brown;
	private PImage frame_wood_medium_brown;
	private PImage frame_wood_light_brown;
	private PImage frame_wood_black;
	private PImage frame_wood_dark_grey;
	private PImage frame_wood_light_grey;
	private PImage frame_pomp_gold_1;
	private PImage frame_pomp_gold_2;
	private PImage frame_pomp_gold_3;
	private PImage frame_pomp_gold_black;
	private PImage frame_pomp_gold_and_black;

	public void setFrameImg(FrameStyle _style, PImage _i) {
	
		switch (_style) {
		case WOOD_DARK_BROWN:
			frame_wood_dark_brown = _i;
			break;
		case WOOD_MEDIUM_BROWN:
			frame_wood_medium_brown = _i;
			break;
		case WOOD_LIGHT_BROWN:
			frame_wood_light_brown = _i;
			break;
		case WOOD_BLACK:
			frame_wood_black = _i;
			break;
		case WOOD_LIGHT_GREY:
			frame_wood_light_grey = _i;
			break;
		case WOOD_DARK_GREY:
			frame_wood_dark_grey = _i;
			break;
		case POMP_GOLD_1:
			frame_pomp_gold_1 = _i;
			break;
		case POMP_GOLD_2:
			frame_pomp_gold_2 = _i;
			break;
		case POMP_GOLD_3:
			frame_pomp_gold_3 = _i;
			break;
		case POMP_GOLD_BLACK:
			frame_pomp_gold_black = _i;
			break;
		case POMP_GOLD_AND_BLACK:
			frame_pomp_gold_and_black =_i;
			break;
		default:
			// STANDART and NONE don't need this...
			break;
		}
	}

	public PImage getFrameImg(FrameStyle _style) {
		switch (_style) {


		case STANDART:
			return frame_pomp_gold_1;

		case WOOD_DARK_BROWN:
			return frame_wood_dark_brown;

		case WOOD_MEDIUM_BROWN:
			return frame_wood_medium_brown;

		case WOOD_LIGHT_BROWN:
			return frame_wood_light_brown;

		case WOOD_BLACK:
			return frame_wood_black;

		case WOOD_LIGHT_GREY:
			return frame_wood_light_grey;

		case WOOD_DARK_GREY:
			return frame_wood_dark_grey;

		case POMP_GOLD_1:
			return frame_pomp_gold_1;

		case POMP_GOLD_2:
			return frame_pomp_gold_2;

		case POMP_GOLD_3:
			return frame_pomp_gold_3;

		case POMP_GOLD_BLACK:
			return frame_pomp_gold_black;

		case POMP_GOLD_AND_BLACK:
			return frame_pomp_gold_and_black;

		default: return null;
		}

	}

}
