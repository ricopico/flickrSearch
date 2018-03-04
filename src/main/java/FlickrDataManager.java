import com.flickr4java.flickr.photos.Photo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by rich on 1/31/2018.
 */
public class FlickrDataManager {
    private Set<FlickrDataObject> flickrDataSet;
    private Set<JSONObject> flickrDataSetJSON;
    private Set<JSONObject> flickrDataSetGeoJSON;
    private LinkedList<String> flickrDataSetCSV;
    private String header = "TITLE,DESCRIPTIONS,IMAGEID,IMAGEHYPERLINK,USERID,USERLOCATIONID,PLACEID,LATITUDE,LONGITUDE,ACCURACY,TAGS";

    public FlickrDataManager() {
        this.flickrDataSet = new HashSet<FlickrDataObject>();
        this.flickrDataSetJSON = new HashSet<JSONObject>();
        this.flickrDataSetGeoJSON = new HashSet<JSONObject>();
        this.flickrDataSetCSV = new LinkedList<String>();
        this.flickrDataSetCSV.add(0, this.header);
    }
    public Set<FlickrDataObject> getFlickrDataSet() {
        return flickrDataSet;
    }
    public void addImage(Photo p) {
        this.flickrDataSet.add(new FlickrDataObject(p));
    }
    public void writeToGeoJSON() {
        //TODO: this
    }
    public void writeToCSV(String outputPath) {
//        String header = "TITLE,DESCRIPTIONS,USERID,PLACEID,LATITUDE,LONGITUDE,ACCURACY,TAGS";
//        System.out.println(header);
//        this.flickrDataSetCSV.add(0, header);
        //TODO:write formatting function for this.
        for(FlickrDataObject fdo : flickrDataSet) {
            flickrDataSetCSV.add(fdo.toCSVString());
        }
        try {
            this.writeFlickrCSVToFile(outputPath + "/images.csv");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
        }
    }

    public void writeToJSON() {

        for(FlickrDataObject fdo : flickrDataSet) {
            JSONObject imgDat = new JSONObject();
            imgDat.put("title", fdo.getTitle());
            imgDat.put("description", fdo.getDescription());
            imgDat.put("userID", fdo.getUserID());
            imgDat.put("placeID", fdo.getPlaceID());
            imgDat.put("latitude", fdo.getLatitude());
            imgDat.put("longitude", fdo.getLongitude());
            imgDat.put("accuracy", fdo.getAccuracy());

            JSONArray tags = new JSONArray();

            for(String tag : fdo.getTags()) {
                tags.put(tag);
            }
            imgDat.put("tags", tags);

            flickrDataSetJSON.add(imgDat);
        }

    }

    public void writeFlickrDataObjectToFile(Photo photo, String pathToOutputFile) {
        FlickrDataObject fdo = new FlickrDataObject(photo);
        MyUtilities.appendStringToEndOfFile(fdo.toCSVString(), pathToOutputFile);

    }
    private void writeFlickrCSVToFile(String path) throws Exception {
        MyUtilities.writeStringListToFile(this.flickrDataSetCSV, path);
    }
    private String formatStringForCSV(String toFormat) {
        String toReturn = toFormat;

        toReturn = toReturn.replace(",", "_");
        toReturn = toReturn.replace("\n", " ");
        toReturn = toReturn.trim();

        return toReturn;
    }
    public String getHeader() {
        return header;
    }

}