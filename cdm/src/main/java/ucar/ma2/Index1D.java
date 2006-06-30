// $Id: Index1D.java,v 1.4 2005/12/15 00:29:08 caron Exp $
/*
 * Copyright 1997-2004 Unidata Program Center/University Corporation for
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
package ucar.ma2;

/**
 * Specialization of Index for rank 1 arrays.
 *
 * @see Index
 * @author caron
 * @version $Revision: 1.4 $ $Date: 2005/12/15 00:29:08 $
 */
public class Index1D extends Index {

  /** current element's indices */
  private int curr0;
  /** array strides */
  private int stride0;
  /** array shapes */
  private int shape0;

  Index1D() { super(1);}
  public Index1D( int[] shape) {
    super(shape);
    precalc();
  }

   public String toString() {
     return Integer.toString(curr0);
   }

  protected void precalc() {
    shape0 = shape[0];
    stride0 = stride[0];
    curr0 = current[0];
  }

 public int [] getCurrentCounter() {
    current[0] = curr0;
    return (int []) current.clone();
  }

  public int currentElement() {
    return offset + curr0*stride0;
  }

  /* public int element(int [] index) {
    int val0 = index[0];
    if (val0 < 0 || val0 >= shape0)  // check index here
      throw new ArrayIndexOutOfBoundsException();

    return offset + val0*stride0;
  } */

  protected int incr() {
    if (++curr0 >= shape0)
      curr0 = 0;    // rollover !
    return offset + curr0*stride0;
  }


  public void setDim(int dim, int value) {
    if (value < 0 || value >= shape[dim])  // check index here
      throw new ArrayIndexOutOfBoundsException();
    curr0 = value;
  }

  public Index set0(int v) {
    if (v < 0 || v >= shape0)  // check index here
      throw new ArrayIndexOutOfBoundsException();
    curr0 = v;
    return this;
  }

  public Index set(int v0) {
    set0(v0);
    return this;
  }

  public Index set(int[] index){
    if (index.length != rank)
      throw new ArrayIndexOutOfBoundsException();
    set0(index[0]);
    return this;
  }

 public Object clone() {
   return super.clone();
 }

 int setDirect(int v0) {
    if (v0 < 0 || v0 >= shape0)
      throw new ArrayIndexOutOfBoundsException();
    return offset + v0*stride0;
 }

}

/* Change History:
   $Log: Index1D.java,v $
   Revision 1.4  2005/12/15 00:29:08  caron
   *** empty log message ***

   Revision 1.3  2005/12/09 04:24:34  caron
   Aggregation
   caching
   sync

   Revision 1.2  2004/07/12 23:40:13  caron
   2.2 alpha 1.0 checkin

 */
