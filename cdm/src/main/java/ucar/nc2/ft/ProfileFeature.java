/*
 * Copyright 1998-2015 John Caron and University Corporation for Atmospheric Research/Unidata
 *
 *  Portions of this software were developed by the Unidata Program at the
 *  University Corporation for Atmospheric Research.
 *
 *  Access and use of this software shall impose the following obligations
 *  and understandings on the user. The user is granted the right, without
 *  any fee or cost, to use, copy, modify, alter, enhance and distribute
 *  this software, and any derivative works thereof, and its supporting
 *  documentation for any purpose whatsoever, provided that this entire
 *  notice appears in all copies of the software, derivative works and
 *  supporting documentation.  Further, UCAR requests that the user credit
 *  UCAR/Unidata in any publications that result from the use of this
 *  software or in any product that includes this software. The names UCAR
 *  and/or Unidata, however, may not be used in any advertising or publicity
 *  to endorse or promote any products or commercial entity unless specific
 *  written permission is obtained from UCAR/Unidata. The user also
 *  understands that UCAR/Unidata is not obligated to provide the user with
 *  any support, consulting, training or assistance of any kind with regard
 *  to the use, operation and performance of this software nor to provide
 *  the user with any updates, revisions, new versions or "bug fixes."
 *
 *  THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 *  IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 *  INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 *  FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 *  NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 *  WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package ucar.nc2.ft;

import ucar.ma2.StructureData;
import ucar.nc2.time.CalendarDate;
import ucar.unidata.geoloc.LatLonPoint;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * A set of observations along the vertical (z) axis.
 * A profile has a nominal lat/lon and time.
 * Actual time may be constant, or vary with z.
 * The z coordinates are monotonic, and may be increasing or decreasing.
 *
 * @author caron
 * @since Feb 8, 2008
 */
public interface ProfileFeature extends PointFeatureCollection, Iterable<PointFeature> {

  /**
   * Nominal location of this profile
   */
  @Nonnull
  LatLonPoint getLatLon();

  /**
   * Nominal time of the profile
   */
  @Nonnull
  CalendarDate getTime();

  /**
   * The number of points along the z axis. May not be known until after iterating through the collection.
   * @return number of points along the z axis, or -1 if not known.
   */
  int size();

  /**
   * The data associated with the profile feature.
   */
  @Nonnull
  StructureData getFeatureData() throws IOException;
}
