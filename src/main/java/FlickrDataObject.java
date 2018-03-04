import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.tags.Tag;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rich on 1/31/2018.
 */
public class FlickrDataObject {

    private Photo image;
    private String title;
    private String imageID; //the name of the image if downloaded
    private String imageHyperlink;  //the hyperlink to the image in the flickr repository
    private String description;
    private String userID;
    private String userLocation;
    private String placeID;
    private float latitude;
    private float longitude;
    private Set<String> tags = new HashSet<String>();
    private int accuracy;
    private boolean hasGeoData;


    public FlickrDataObject(Photo image) {
        //TODO: check for null fields
        this.image=image;
        this.setTitle(image.getTitle());
        this.setDescription(image.getDescription());
        this.setImageID(MyUtilities.convertToFileSystemChar(image.getTitle()) + "_" + image.getId() + "_o." + image.getOriginalFormat());
        this.setImageHyperlink(image.getLargeUrl());
        this.setUserID(image.getOwner().getId());
        this.setPlaceID(image.getPlaceId());
        this.setUserLocation(image.getOwner().getLocation());
        if(image.getGeoData() != null)
        {
            this.hasGeoData = true;
            this.setLatitude(image.getGeoData().getLatitude());
            this.setLongitude(image.getGeoData().getLongitude());
            this.setAccuracy(image.getGeoData().getAccuracy());
        } else {
            this.hasGeoData = false;
        }


        for(Tag tag : image.getTags()) {
            this.tags.add(tag.getValue());
        }
    }

    public FlickrDataObject() {
        //empty object
        this.image=null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = this.checkForNullString(title);}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = this.checkForNullString(description);    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = this.checkForNullString(userID);
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = this.checkForNullString(placeID);
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public Photo getImage() {
        return image;
    }

    public void setImage(Photo image) {
        this.image = image;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(String tags) {
        //assumes space delimiter
        for(String tag : tags.split(" ")) {
            this.tags.add(tag);
        }
    }
    public boolean isHasGeoData() {
        return hasGeoData;
    }
    public void setUserLocation(String userLocation) {
        this.userLocation = this.checkForNullString(userLocation);
    }
    public String getUserLocation() {
        return this.userLocation;
    }
    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getImageHyperlink() {
        return imageHyperlink;
    }

    public void setImageHyperlink(String imageHyperlink) {
        this.imageHyperlink = imageHyperlink;
    }
    private String checkForNullString(String toCheck) {
        if(toCheck==null){
            return "empty_field";
        }
        return toCheck;
    }
    public String toCSVString() {
        String toReturn = "";
        toReturn += this.formatStringForCSV(this.getTitle());
        toReturn += ",";
        toReturn += this.formatStringForCSV(this.getDescription());
        toReturn += ",";
        toReturn += this.formatStringForCSV(this.getImageID());
        toReturn += ",";
        toReturn += this.getImageHyperlink();
        toReturn += ",";
        toReturn += this.formatStringForCSV(this.getUserID());
        toReturn += ",";
        toReturn += this.formatStringForCSV(this.getUserLocation());
        toReturn += ",";
        toReturn += this.formatStringForCSV(this.getPlaceID());
        toReturn += ",";
        toReturn += this.getLatitude();
        toReturn += ",";
        toReturn += this.getLongitude();
        toReturn += ",";
        toReturn += this.getAccuracy();
        toReturn += ",";
        String tagsToAdd = "";
        for (String tag : this.getTags()) {
            tagsToAdd += this.formatStringForCSV(tag);
            tagsToAdd += ";";
        }
        toReturn += this.formatStringForCSV(tagsToAdd);
        return toReturn;
    }
    private String formatStringForCSV(String toFormat) {
        String toReturn = toFormat;

        toReturn = toReturn.replace(",", "_");
        toReturn = toReturn.replace("\n", " ");
        toReturn = toReturn.trim();

        return toReturn;
    }
}