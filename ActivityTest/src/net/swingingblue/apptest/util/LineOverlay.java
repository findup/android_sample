package net.swingingblue.apptest.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class LineOverlay extends Overlay {

	private ArrayList<GeoPoint> geoPointList = new ArrayList<GeoPoint>();
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		
		if (!shadow) {
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStyle(Paint.Style.STROKE);
			paint.setAntiAlias(true);
			paint.setStrokeWidth(8);
			paint.setColor(Color.MAGENTA);
			paint.setAlpha(128);
			paint.setStrokeCap(Paint.Cap.BUTT);
			
			Path path = new Path();
			Projection projection = mapView.getProjection();
			
			Iterator<GeoPoint> it = geoPointList.iterator();
			
			Point point;
			GeoPoint geo;

			geo = it.next();
			point = projection.toPixels(geo, null);
			path.moveTo(point.x, point.y);
			canvas.drawPath(path, paint);
			
			while (it.hasNext()) {
				geo = it.next();
				point = projection.toPixels(geo, null);
				path.lineTo(point.x, point.y);
			}
			
			canvas.drawPath(path, paint);
			path.close();
		}
	}

	public void addPoint(GeoPoint geo) {
		this.geoPointList.add(geo);
	}
	
	public void clearPoint() {
		this.geoPointList.clear();
	}
	
}
