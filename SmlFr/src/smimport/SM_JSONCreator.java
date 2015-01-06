package smimport;


import java.util.Iterator;

import SMUtils.FrameStyle;
import processing.data.*;
import smlfr.SM_FileManager;


public class SM_JSONCreator  {

	private SM_FileManager fm;

	public SM_JSONCreator(SM_FileManager _fm){
		fm = _fm;
	}


	public JSONObject makeNewProjectFile( String _projectName, String[] _selectedRooms ) {

//		System.out.println("making");

		JSONObject arch = fm.getMuseum().getJSONObject("architecture");

		JSONObject proj = new JSONObject();

		proj.setString("projectName", _projectName);
		proj.setString("museum", fm.getMuseumName() );

		JSONArray lib = new JSONArray();
		proj.setJSONArray("artLibrary", lib);
		
		
		
		
		JSONArray rooms = new JSONArray();

		for(String r : _selectedRooms) {

			JSONObject thisSourceRoom = arch.getJSONObject(r);

			JSONObject thisRoom = new JSONObject();
			thisRoom.setString("roomName", r);

			// this Rooms walls
			Iterator<?> keys = thisSourceRoom.keyIterator();
			while( keys.hasNext() ) {
				String k = (String)keys.next();
				if(k.startsWith("w_")) {

					JSONObject thisWall = new JSONObject();
					thisWall.setString("colorInt", "white" );
					thisWall.setString("colorBrillux", "white");
					thisWall.setJSONArray("artworks", new JSONArray() );

					thisRoom.setJSONObject( k, thisWall );
				}

			}
			rooms.append(thisRoom);
		}

		proj.setJSONArray("rooms", rooms);

//		System.out.println("\nTHE NEWLY GENERATED FRESH PROJECT FILE\n\n" + proj );

		return proj;

	}
	

	public JSONObject makeNewArtworkFile( String _invNr, String _artist, String _title, int[] _size, int[] _frameSize, int[] _pptSize, FrameStyle _framsStyle ) {
		JSONObject aw = new JSONObject();
		
		aw.setString("invNr", _invNr);
		aw.setString("title", _title);
		aw.setString("artist", _artist);
		aw.setString("frameStyle", _framsStyle.toString());
		
		JSONArray fs = new JSONArray();
		if( _frameSize.length > 0 ) {
			fs.append(_frameSize[0]);
			fs.append(_frameSize[1]);
			fs.append(_frameSize[2]);
			fs.append(_frameSize[3]);
		}
		aw.setJSONArray("frameSize", fs );
		
		JSONArray ps = new JSONArray();
		if( _pptSize.length > 0 ) {
			ps.append(_pptSize[0]);
			ps.append(_pptSize[1]);
			ps.append(_pptSize[2]);
			ps.append(_pptSize[3]);
		}
		aw.setJSONArray("pasSize", ps );
		
		JSONArray sz = new JSONArray();
		sz.append(_size[0]);
		sz.append(_size[1]);
		aw.setJSONArray("size", sz );
		
		
		System.out.println("RETURNING THIS JSON ARTWORK:\n"+aw.toString());
		
		
		return aw;
	}
	

	public JSONObject makeNewMuseumFile() {

		boolean areYouShure = false;



		if(areYouShure){
			//			IntList l8 = new IntList(list8);
			//			IntList l2 = new IntList(list2);
			//			IntList l4 = new IntList(list4);

			JSONArray viewSkew = new JSONArray();
			JSONArray viewCrop = new JSONArray();

			JSONObject view1 = new JSONObject();
			view1.setString("viewName", "v_S1_ABCD");
			view1.setJSONArray("viewSkew", viewSkew);
			view1.setJSONArray("viewCrop", viewCrop);

			JSONObject view2 = new JSONObject();
			view2.setString("viewName", "v_S1_ABCD");
			view2.setString("viewName", "v_S1_DE");
			view2.setJSONArray("viewCrop", null);

			JSONArray relatedViews = new JSONArray();
			relatedViews.append(view1);
			relatedViews.append(view2);


			JSONArray measures = new JSONArray();

			JSONArray navBound = new JSONArray();

			JSONObject wall = new JSONObject();
			wall.setJSONArray("navigatorBoundingBox", navBound);
			wall.setJSONArray("wallSize", measures);
			wall.setJSONArray("relatedViews", relatedViews);

			JSONObject room = new JSONObject();
			room.setString("roomRealName", "Saal 1");
			room.setJSONObject("w_S1_A", wall);
			room.setJSONObject("w_S1_B", wall);

			JSONObject architecture = new JSONObject();
			architecture.setJSONObject("S1", room);
			//room.setString("roomRealName", "Saal 2");
			//architecture.setJSONObject("S2", room);

			JSONObject museum = new JSONObject();
			museum.setString("museumName", "Museum Kunst Der Westküste");
			museum.setJSONObject("architecture", architecture);

			return museum;
		} else {
			return null;
		}
	}

}
