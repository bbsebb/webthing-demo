package iot.webthing;


import java.io.IOException;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.iot.webthing.Property;
import org.mozilla.iot.webthing.Thing;
import org.mozilla.iot.webthing.Value;
import org.mozilla.iot.webthing.WebThingServer;

public class SimplySingleThing {
	public static Thing makeThing(String name) {
        Thing thing = new Thing("urn:dev:ops:"+name,
                                "My Lamp",
                                new JSONArray(Arrays.asList("OnOffSwitch",
                                                            "Light")),
                                "A web connected lamp");

        JSONObject onDescription = new JSONObject();
        onDescription.put("@type", "OnOffProperty");
        onDescription.put("title", "On/Off");
        onDescription.put("type", "boolean");
        onDescription.put("description", "Whether the lamp is turned on");
        thing.addProperty(new Property<Boolean>(thing,"on",new Value<Boolean>(true),onDescription));
        return thing;
    }

    public static void main(String[] args) {
    	String nom = "Lamp";
    	int port = 8889;
    	if(args.length == 2) {
    		nom = args[0];
    		port = Integer.valueOf(args[1]);
    	} else if(args.length == 1){
    		nom = args[0];
    	}
        Thing thing = makeThing(nom);
        final WebThingServer server;

        try {
            // If adding more than one thing, use MultipleThings() with a name.
            // In the single thing case, the thing's name will be broadcast.
            server = new WebThingServer(new WebThingServer.SingleThing(thing),
            		port);

            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    server.stop();
                }
            });

            server.start(false);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }



   
}
