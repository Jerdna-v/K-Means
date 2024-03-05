package waypoint;

import java.awt.*;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

public class MyWaypoint extends DefaultWaypoint {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

public MyWaypoint(String name, GeoPosition coord, Color color) {
    super(coord);
    this.name = name;
    this.color= color;
}

    private String name;
    public final Color color;

}
