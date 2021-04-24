package DtedData;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dted {

    protected List<String> filenames;
    protected DataSet dted;
    static final int nullvalue = -32767;

    public Dted(){
        filenames = new ArrayList<String>();
        dted = new DataSet();
    }

    public Dted(String directory) throws IOException {
        filenames = new ArrayList<String>();
        addFile(directory);
        dted = new DataSet(filenames);
        dted.arrange();
    }

    public void addNewDataFile(String directory) throws IOException {      //add new files to DataSet and parse
        addFile(directory);
        dted.setFilenames(filenames);
        dted.arrange();
    }

    private void addFile(@NotNull String directory){
        if(directory.substring(directory.length() - 4).equals(".DT1")){
            filenames.add(directory);
        } else{
            File dirFolder = new File(directory);
            String[] contents = dirFolder.list();
            if(contents==null){
                System.out.println("There is no data in directory");
            } else {
                for (String content : contents) {
                    if(content.substring(content.length() - 4).equals(".DT1")){
                        filenames.add(directory + "/" +content);
                    }
                }
            }
        }
    }

    public Integer getElevation(int longitude , int latitude){   //queries one elevation value
        int ans = dted.query(longitude,latitude);
        if(ans == nullvalue){
            return null;
        } else{
            return ans;
        }
    }

    public ArrayList<int[][]> getElevationGrids(){      //return all grid arrays for writing DB
        ArrayList<int[][]> grids = new ArrayList<int[][]>(filenames.size());
        for(int i=0;i<filenames.size();i++){
            grids.add(dted.getGrid(i));
        }
        return grids;
    }

    public ArrayList<ArrayList<Integer>> getBorders(){  //return borders of all files
        List<BorderGrid> temp = dted.getBorders();
        ArrayList<ArrayList<Integer>> values = new ArrayList<ArrayList<Integer>>();
        for(int i=0;i<temp.size();i++){
            ArrayList<Integer> current = new ArrayList<Integer>();
            current.add(temp.get(i).getSw_long());
            current.add(temp.get(i).getSw_lat());
            current.add(temp.get(i).getNe_long());
            current.add(temp.get(i).getNe_lat());
            current.add(temp.get(i).getTotal_long_line());
            current.add(temp.get(i).getTotal_lat_line());
            values.add(current);
        }
        return values;
    }
}

