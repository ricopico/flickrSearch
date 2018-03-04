import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.test.TestInterface;

public class FlickrSearch {

    public static void main(String[] args) {

        /*
        keys acquired from: https://www.flickr.com/services/apps
        keys updated on 1/27/2018, 5:13PM
        App Name: Sacred Site Finder

        for the blackfeet, this site works for sacred sites: http://nativeamericannetroots.net/diary/1875
         */

        //input keys here
        String apiKey = "1572ea6178ed93558686043967fc08d3";
        String apiSecret = "9e71a3a1def862e4";

        FlickrDataManager flickrData = new FlickrDataManager();

        try{
            //Testing interface
            System.out.println("BEGINNING SEARCH");
            Flickr f = new Flickr(apiKey, apiSecret, new REST());
            TestInterface testInterface = f.getTestInterface();

            int numberOfImages = 10;  //defaults to 1000 images.

            boolean downloadImages=true;
            boolean printToCSV=true;
            boolean printToGeoJSON=false;

            //process the args
            for(int i=0; i<args.length; i++) {
                if(args[i].equals("-numberOfImages")) {
                    String numImages = args[i+1];
                    numberOfImages = Integer.parseInt(numImages);

                    //adjust the number of arguments
                    args = MyUtilities.removeEltFromStringArray(args, i);
                    args = MyUtilities.removeEltFromStringArray(args, i);
                }
            }

            //create flickr crawler

            FlickrCrawler flickrCrawler = new FlickrCrawler(f, numberOfImages, "path", downloadImages, printToCSV, printToGeoJSON);

            //activate!
            flickrCrawler.activateCrawler(flickrData, args);

            //activate and download pics!
            //flickrCrawler.activateCrawler(flickrData, args, true);


        } catch (Exception e) {
            System.err.println("oops: " + e.getMessage());
            System.err.println(e.getStackTrace());
        }

        System.out.println("testing complete.");

    }

}
