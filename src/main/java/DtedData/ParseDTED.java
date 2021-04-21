package DtedData;

import java.io.FileInputStream;
import java.io.IOException;

class ParseDTED{

    protected FileInputStream dtedData;
    protected int sw_lat;     //southwest corner latitude
    protected int sw_long;    //Southwest corner longitude
    protected int ne_lat;     //northeast corner latitude
    protected int ne_long;    //northeast corner longitude
    protected int total_long_line;
    protected  int total_lat_line;
    protected double long_data_interval;
    protected double lat_data_interval;
    int[][] elevation;      //extracted elevation data

    ParseDTED(String fileName){
        try {
            dtedData = new FileInputStream(fileName);
        } catch (IOException e){
            System.out.println("Error occured during reading dtedt file");
            e.printStackTrace();
        }
    }
    public int getSw_lat(){return sw_lat;}
    public int getSw_long(){return sw_long;}
    public int getNe_lat(){return ne_lat;}
    public int getNe_long(){return ne_long;}

    public void parse() throws IOException {    //removes unnecessary bytes and take intervals and line counts
        try{
            moveCursorBy(20);
            byte[] cursor = new byte[4];
            dtedData.read(cursor, 0, 4);
            long_data_interval = byteToInt(cursor);
            long_data_interval /= 10;

            dtedData.read(cursor, 0, 4);
            lat_data_interval = byteToInt(cursor);
            lat_data_interval /=10;
            moveCursorBy(19);

            dtedData.read(cursor, 0, 4);
            total_long_line = byteToInt(cursor);
            dtedData.read(cursor, 0, 4);
            total_lat_line = byteToInt(cursor);

            elevation = new int[total_long_line][total_lat_line];

            moveCursorBy(229);
            setCorner();
            moveCursorBy(3099);

        } catch (IOException e) {
            System.out.println("Parse error");
            e.printStackTrace();
        }
        try{
            setElevationData();     //to fill the elevation[][] array
        } catch (IOException ignored) {

        }
    }

    private void moveCursorBy(int byte_val) throws IOException {
        for(int i=0;i<byte_val;i++){
            dtedData.read();
        }
    }   //removes unnecessary bytes

    private int byteToInt(byte[] byt_arr){
        return Integer.parseInt(new String(byt_arr));
    }

    private void setCorner() throws IOException {
        byte[] cursor = new byte[6];
        byte[] signval = new byte[1];

        //Arrangement for setting sw_lat
        dtedData.read(cursor, 0, 6);
        int curr_val = byteToInt(cursor);
        dtedData.read(signval, 0, 1);
        if(new String(signval).equals("S")){
            curr_val = -curr_val;
        }
        sw_lat = curr_val;

        //Arrangement for setting sw_long
        cursor = new byte[7];
        dtedData.read(cursor,0,7);
        curr_val = byteToInt(cursor);
        dtedData.read(signval, 0, 1);
        if(new String(signval).equals("W")){
            curr_val = -curr_val;
        }
        sw_long = curr_val;

        moveCursorBy(15);   //removing nw coordinates
        //Arrangement for setting ne_lat
        cursor = new byte[6];
        dtedData.read(cursor, 0, 6);
        curr_val = byteToInt(cursor);
        dtedData.read(signval, 0, 1);
        if(new String(signval).equals("S")){
            curr_val = -curr_val;
        }
        ne_lat = curr_val;

        //Arrangement for setting ne_long
        cursor = new byte[7];
        dtedData.read(cursor,0,7);
        curr_val = byteToInt(cursor);
        dtedData.read(signval, 0, 1);
        if(new String(signval).equals("W")){
            curr_val = -curr_val;
        }
        ne_long = curr_val;
    }

    void setElevationData() throws IOException {    //reads the data until end and set elevation matrix
        for(int i=0;i<total_long_line;i++){
            byte[] cursor = new byte[8];
            dtedData.read(cursor,0,8);

            cursor = new byte[2];
            for(int j=0;j<total_lat_line;j++) {
                dtedData.read(cursor, 0, 2);
                int elev = (short) (((cursor[0] & 0xFF) << 8) | (cursor[1] & 0xFF));
                elevation[i][j] = elev;
            }

            cursor = new byte[4];
            dtedData.read(cursor,0,4);
        }
    }

    boolean checkInterval(int longi, int lati ){    //checks wanted lat,long is in this file
        if(lati <= ne_lat && lati >= sw_lat){
            return longi <= ne_long && longi >= sw_long;
        }
        return false;
    }

    int[][] getGrid(){
        return elevation;
    }

    int getElevation(double longitude, double latitude ){    //calculates grid of elevation matrix and return
        int lat = (int) (((latitude-sw_lat)*(total_lat_line-1)/(ne_lat-sw_lat)));
        int lon = (int) (((longitude-sw_long)*(total_long_line-1)/(ne_long-sw_long)));
        return elevation[lon][lat];
    }
}