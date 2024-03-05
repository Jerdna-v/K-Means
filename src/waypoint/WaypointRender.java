package waypoint;

import java.awt.*;
import java.awt.geom.Point2D;


import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointPainter;

public class WaypointRender extends WaypointPainter<MyWaypoint> {

    @Override
    protected void doPaint(Graphics2D g, JXMapViewer map, int width, int height) {
        for (MyWaypoint wp : getWaypoints()) {
            g.setColor(wp.color);
            Point2D p = map.getTileFactory().geoToPixel(wp.getPosition(), map.getZoom());
            Rectangle rec = map.getViewportBounds();
            int x = (int) (p.getX() - rec.getX());
            int y = (int) (p.getY() - rec.getY());
            int radius = 3;
            int diameter = radius *2;
            g.fillOval(x - radius, y - radius, diameter, diameter);

        }
    }
}
