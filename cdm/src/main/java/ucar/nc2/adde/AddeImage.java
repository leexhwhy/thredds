package ucar.nc2.adde;

import ucar.ma2.*;
import ucar.nc2.dt.image.ImageArrayAdapter;
import java.util.*;

public class AddeImage {
  private static int initialCapacity = 100;
  private static LinkedHashMap hash = new LinkedHashMap(initialCapacity, 0.75f, true); // LRU cache
  private static double memUsed = 0.0; // bytes
  private static int maxCacheSize = 30; // MB
  private static boolean debugCache = false;

  public static AddeImage factory( String urlName) throws java.io.IOException, java.net.MalformedURLException {
    //debugCache = Debug.isSet("ADDE/AddeImage/ShowCache");
    AddeImage image = (AddeImage) hash.get( urlName);
    if (image == null) {
      if (debugCache) System.out.println("ADDE/AddeImage/ShowCache: cache miss "+urlName);
      image = new AddeImage( urlName);
      hash.put( urlName, image);
      memUsed += image.getSize();
      adjustCache();
    } else {
      if (debugCache) System.out.println("ADDE/AddeImage/ShowCache: cache hit "+urlName);
    }
    if (debugCache) System.out.println("  memUsed = "+(memUsed*1.e-6)+" Mb");
    return image;
  }

  public static int getMaxCacheSize() { return maxCacheSize; }
  public static void setMaxCacheSize( int max) { maxCacheSize = max; }
  public static int getCacheSize() { return (int) (memUsed*1.e-6); }

  private static void adjustCache() {
    double max = maxCacheSize * 1.e6;
    if (memUsed <= max) return;

    Iterator iter = hash.values().iterator();
    while (iter.hasNext() && (memUsed > max)) {
      AddeImage image = (AddeImage) iter.next();
      memUsed -= image.getSize();
      if (debugCache) System.out.println("  remove = "+image.getName()+" size= "+
        (image.getSize()*1.e-6)+"  memUsed = "+(memUsed*1.e-6));
      iter.remove();
    }

  }

  //////////////////////////////////////////////////////////////////////////////////

  private String urlName;
  private int nelems = 0, nlines = 0;
  private java.awt.image.BufferedImage image = null;
  private Array ma;
  private boolean debug = false;

  public AddeImage( String urlName) throws java.io.IOException, java.net.MalformedURLException {
    this.urlName = urlName;

    long timeStart = System.currentTimeMillis();
    debug = false; // Debug.isSet("ADDE/AddeImage/MA");

    AreaFile3 areaFile2 = new AreaFile3( urlName);
    ma = areaFile2.getData();

    if (ma.getRank() == 3)
      ma = ma.slice( 0, 0); // we need 2D

    nlines = ma.getShape()[0];
    nelems = ma.getShape()[1];
    long timeEnd = System.currentTimeMillis();
    // if (Debug.isSet("ADDE/AddeImage/createTiming")) System.out.println("ADDE/AddeImage/createTiming AddeImage = "+ .001*(timeEnd - timeStart)+" sec");
  }

  private long getSize() { return getSize( ma); }

  private long getSize( Array ma) {
    long size = ma.getSize();
    java.lang.Class maType = ma.getElementType();
    if (maType == byte.class) return size;
    if (maType == short.class) return 2*size;
    if (maType == int.class) return 4*size;
    if (maType == float.class) return 4*size;
    if (maType == double.class) return 8*size;
    throw new IllegalArgumentException("DataBufferMultiArray ma has illegal data type = "+maType.getName());
  }

  public java.awt.Dimension getPreferredSize() {
    return new java.awt.Dimension(nelems, nlines);
  }

  public java.awt.image.BufferedImage getImage() {
    if (image == null) {
      image = ImageArrayAdapter.makeGrayscaleImage( ma);
    }
    return image;
  }

  public String getName() {
    return urlName;
  }

      /** System-triggered redraw.
  public void paintComponent(Graphics g) {
    if (currentImage == null) return;
    if (debug) System.out.println(" paintComponent = "+currentImage.getWidth()+" "+currentImage.getHeight());

    g.drawImage( currentImage, 0, 0, new java.awt.image.ImageObserver() {
      public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return true;
      }
    });
  }


  public void clear() {
    Graphics2D g = (Graphics2D) getGraphics();
    if (g == null) return;
    Rectangle bounds = getBounds();
    g.setBackground(Color.white);
    g.clearRect(0, 0, bounds.width, bounds.height);
    g.dispose();
  } */



  private class LRUCache extends java.util.LinkedHashMap {
    public LRUCache(int maxsize) {
	    super(maxsize*4/3 + 1, 0.75f, true);
	    this.maxsize = maxsize;
    }
    protected int maxsize;
    protected boolean removeEldestEntry() { return size() > maxsize; }
  }


}
/* Change History:
   $Log: AddeImage.java,v $
   Revision 1.6  2005/02/23 20:01:01  caron
   *** empty log message ***

   Revision 1.5  2004/11/16 23:35:37  caron
   no message

   Revision 1.4  2004/11/07 02:55:10  caron
   no message

   Revision 1.3  2004/10/23 21:36:10  caron
   no message

   Revision 1.2  2004/09/30 00:33:40  caron
   *** empty log message ***

   Revision 1.1  2004/09/25 00:09:44  caron
   add images, thredds tab

   Revision 1.2  2003/03/17 21:12:37  john
   new viewer

   Revision 1.1  2002/12/13 00:53:09  caron
   pass 2

   Revision 1.3  2002/10/18 18:20:39  caron
   thredds server

   Revision 1.2  2002/04/29 22:48:54  caron
   add image caching

   Revision 1.1.1.1  2002/02/26 17:24:48  caron
   import sources

*/

