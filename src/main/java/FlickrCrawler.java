import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.SearchParameters;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.prefs.Preferences;

public class FlickrCrawler {

    private Flickr myFlickr;
    private int numberOfImagesToGet = 1000;
    private boolean downloadImages;
    private boolean printToCSV;
    private boolean printtToGeoJSON;
    private String outputPath;
    private Preferences userPrefs = Preferences.userNodeForPackage(FlickrCrawler.class);

    public FlickrCrawler(Flickr flickrObj, int numberOfImagesToGet, String outputPath, boolean downloadImages, boolean printToCSV, boolean printtToGeoJSON) {
        myFlickr = flickrObj;
        this.numberOfImagesToGet = numberOfImagesToGet;
        this.downloadImages = downloadImages;
        this.printToCSV = printToCSV;
        this.printtToGeoJSON = printtToGeoJSON;
    }

    public void activateCrawler(FlickrDataManager flickrDataManager, String[] searchTerms) {

        try {
        //TODO: this
            Set<String> extras = new HashSet<String>();
            extras.add("description");
            extras.add("comments");
            extras.add("tags");
            extras.add("geo");
            extras.add("locality");
            extras.add("county");
            extras.add("region");
            extras.add("country");
            extras.add("date");

            SearchParameters searchParameters = new SearchParameters();
            //searchParameters.setExtras(Stream.of("description").collect(Collectors.toSet()));   //requires 1.8 or higher
            searchParameters.setAccuracy(1);
            searchParameters.setExtras(extras);

            StringBuilder tagsBuilder = new StringBuilder();
            for (String tmp : searchTerms) {
                tagsBuilder.append(" " + tmp);
            }

            this.outputPath="pics" + File.separator + tagsBuilder.toString().substring(1);

            new File(this.outputPath).mkdirs();
            searchParameters.setTags(searchTerms);

            if(this.printToCSV) {
                //Prep csv headers
                MyUtilities.appendStringToEndOfFile(flickrDataManager.getHeader(), this.outputPath + "/imageData.csv");
            }

            int imageCounter=0;
            for (int i = userPrefs.getInt(this.outputPath, 0); true; i++) {
                userPrefs.putInt(this.outputPath, i);
                System.out.println("\tcurrent page: " + userPrefs.getInt(this.outputPath, 0));

                PhotoList<Photo> list = this.myFlickr.getPhotosInterface().search(searchParameters, 500, i);

                Iterator itr = list.iterator();
                while (itr.hasNext()) {
                    Photo photo = (Photo) itr.next();

                    if(this.downloadImages) {
                        saveImage(this.myFlickr, photo);
                    }

                    if(this.printToCSV) {
                        flickrDataManager.writeFlickrDataObjectToFile(photo, this.outputPath + "/imageData.csv");
                    }

                    //TODO: json/geojson

                    //increase counter
                    imageCounter++;
                    if(imageCounter>this.numberOfImagesToGet) {
                        break;
                    }
                }
                if(imageCounter>this.numberOfImagesToGet) {
                    break;
                }
            }

//            if(this.printToCSV){
//                flickrDataManager.writeToCSV(this.outputPath);
//            }

        } catch (FlickrException e) {
            e.printStackTrace();
        } catch(Exception e) {
                e.printStackTrace();
        }

    }

    public boolean saveImage(Flickr f, Photo p) {

        String cleanTitle = MyUtilities.convertToFileSystemChar(p.getTitle());

        File orgFile = new File(this.outputPath + File.separator + cleanTitle + "_" + p.getId() + "_o." + p.getOriginalFormat());
        File largeFile = new File(this.outputPath + File.separator + cleanTitle + "_" + p.getId() + "_b." + p.getOriginalFormat());

        if (orgFile.exists() || largeFile.exists()) {
            System.out.println(p.getTitle() + "\t" + p.getLargeUrl() + " skipped!");
            return false;
        }

        try {
            Photo nfo = f.getPhotosInterface().getInfo(p.getId(), null);
            if (nfo.getOriginalSecret().isEmpty()) {
                if(this.downloadImages) {
                    ImageIO.write(p.getLargeImage(), p.getOriginalFormat(), largeFile);
                    System.out.println(p.getTitle() + "\t" + p.getLargeUrl() + " was written to " + largeFile.getName());
                }

//                MyUtilities.writeStringListToFile(getPhotoInfo(p), this.outputPath + File.separator + p.getTitle() + "_" + p.getId() + ".txt");
//                MyUtilities.writeStringListToFile(getPhotoInfo(p), this.outputPath + File.separator + cleanTitle + "_" + p.getId() + ".txt");


            } else {
                if(this.downloadImages) {
                    p.setOriginalSecret(nfo.getOriginalSecret());
                    ImageIO.write(p.getOriginalImage(), p.getOriginalFormat(), orgFile);
                    System.out.println(p.getTitle() + "\t" + p.getOriginalUrl() + " was written to " + orgFile.getName());
                }

//                MyUtilities.writeStringListToFile(getPhotoInfo(p), this.outputPath + File.separator + p.getTitle() + "_" + p.getId() + ".txt");
//                MyUtilities.writeStringListToFile(getPhotoInfo(p), this.outputPath + File.separator + cleanTitle + "_" + p.getId() + ".txt");
            }
        } catch (FlickrException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
