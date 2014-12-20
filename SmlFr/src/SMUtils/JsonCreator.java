package SMUtils;


import java.util.Iterator;

import processing.data.*;
import smlfr.SM_FileManager;


public class JsonCreator  {

	private SM_FileManager fm;

	public JsonCreator(SM_FileManager _fm){
		fm = _fm;
	}


	public JSONObject makeNewProjectFile( String _projectName, String[] _selectedRooms ) {

		System.out.println("making");

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

		System.out.println("\nTHE NEWLY GENERATED FRESH PROJECT FILE\n\n" + proj );

		return proj;

	}

	public void makeNewMuseumFile() {

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

		}
	}

}
