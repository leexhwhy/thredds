/*
 * Copyright 1997-2008 Unidata Program Center/University Corporation for
 * Atmospheric Research, P.O. Box 3000, Boulder, CO 80307,
 * support@unidata.ucar.edu.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package ucar.nc2.dt2.point;

import ucar.nc2.dt2.*;
import ucar.nc2.units.DateRange;
import ucar.nc2.constants.FeatureType;
import ucar.unidata.geoloc.LatLonRect;

import java.util.List;
import java.io.IOException;

/**
 * Abstract superclass for implementations of StationFeatureCollection.
 * Subclass must supply getPointFeatureCollectionIterator().
 *
 * @author caron
 * @since Feb 5, 2008
 */
public abstract class StationCollectionImpl extends OneNestedPointCollectionImpl implements StationFeatureCollection {

  protected StationHelper stationHelper;

  public StationCollectionImpl(String name) {
    super( name, FeatureType.STATION);
    stationHelper = new StationHelper();
  }

  protected void setStationHelper(StationHelper stationHelper) {
    this.stationHelper = stationHelper;
  }

  public List<Station> getStations() {
    return stationHelper.getStations();
  }

  public List<Station> getStations(LatLonRect boundingBox) throws IOException {
    return stationHelper.getStations(boundingBox);
  }

  public Station getStation(String name) {
    return stationHelper.getStation(name);
  }

  public LatLonRect getBoundingBox() {
    return stationHelper.getBoundingBox();
  }

  public StationFeatureCollection subset(List<Station> stations) throws IOException {
    if (stations == null) return this;
    return new StationFeatureCollectionSubset(this, stations);
  }

  public StationFeature getStationFeature(Station s) throws IOException {
    return (StationFeature) s;  // subclasses override if not true
  }

  public NestedPointFeatureCollectionIterator getNestedPointFeatureCollectionIterator(int bufferSize) throws IOException {
    throw new UnsupportedOperationException("StationFeatureCollection does not implement getNestedPointFeatureCollection()");
  }

  // LOOK subset by filtering on the stations, but it would be easier if we could get the StationFeature from the Station
  private class StationFeatureCollectionSubset extends StationCollectionImpl {
    StationCollectionImpl from;

    StationFeatureCollectionSubset(StationCollectionImpl from, List<Station> stations) {
      super( from.getName());
      this.from = from;
      stationHelper = new StationHelper();
      stationHelper.setStations(stations);
    }

    public PointFeatureCollectionIterator getPointFeatureCollectionIterator(int bufferSize) throws IOException {
      return new PointCollectionIteratorFiltered( from.getPointFeatureCollectionIterator(bufferSize), new Filter());
    }

    private class Filter implements PointFeatureCollectionIterator.Filter {

      public boolean filter(PointFeatureCollection pointFeatureCollection) {
        StationFeature stationFeature = (StationFeature) pointFeatureCollection;
        return stationHelper.getStation(stationFeature.getName()) != null;
      }
    }
  }
}
