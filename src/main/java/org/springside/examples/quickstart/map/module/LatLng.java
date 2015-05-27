package org.springside.examples.quickstart.map.module;

public final class LatLng {
	  private static final String a = LatLng.class.getSimpleName();
	  public final double latitude;
	  public final double longitude;

	  public LatLng(double paramDouble1, double paramDouble2)
	  {
	    int i = (int)(paramDouble1 * 1000000.0D);
	    int j = (int)(paramDouble2 * 1000000.0D);
	    this.latitude = (i / 1000000.0D);
	    this.longitude = (j / 1000000.0D);
	  }

	  public String toString()
	  {
	    String str = new String("latitude: ");
	    str = str + this.latitude;
	    str = str + ", longitude: ";
	    str = str + this.longitude;
	    return str;
	  }
}
