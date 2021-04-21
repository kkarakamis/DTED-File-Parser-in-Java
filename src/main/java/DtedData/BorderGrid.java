package DtedData;

//This class holds the border for querying borders of elevation grids.
class BorderGrid {
    int sw_lat;
    int sw_long;
    int ne_lat;
    int ne_long;
    int total_lat_line;
    int total_long_line;


    public BorderGrid() {
    }

    public BorderGrid(int sw_long, int sw_lat, int ne_long, int ne_lat, int total_long_line, int total_lat_line) {
        this.sw_lat = sw_lat;
        this.sw_long = sw_long;
        this.ne_lat = ne_lat;
        this.ne_long = ne_long;
        this.total_lat_line = total_lat_line;
        this.total_long_line = total_long_line;
    }

    public int getTotal_lat_line() {
        return total_lat_line;
    }

    public void setTotal_lat_line(int total_lat_line) {
        this.total_lat_line = total_lat_line;
    }

    public int getTotal_long_line() {
        return total_long_line;
    }

    public void setTotal_long_line(int total_long_line) {
        this.total_long_line = total_long_line;
    }

    public int getSw_lat() {
        return sw_lat;
    }

    public void setSw_lat(int sw_lat) {
        this.sw_lat = sw_lat;
    }

    public int getSw_long() {
        return sw_long;
    }

    public void setSw_long(int sw_long) {
        this.sw_long = sw_long;
    }

    public int getNe_lat() {
        return ne_lat;
    }

    public void setNe_lat(int ne_lat) {
        this.ne_lat = ne_lat;
    }

    public int getNe_long() {
        return ne_long;
    }

    public void setNe_long(int ne_long) {
        this.ne_long = ne_long;
    }
}